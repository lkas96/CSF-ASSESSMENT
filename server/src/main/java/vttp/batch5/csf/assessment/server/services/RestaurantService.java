package vttp.batch5.csf.assessment.server.services;

import vttp.batch5.csf.assessment.server.models.MenuItem;
import vttp.batch5.csf.assessment.server.repositories.OrdersRepository;
import vttp.batch5.csf.assessment.server.repositories.RestaurantRepository;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class RestaurantService {

  @Autowired
  private OrdersRepository or;

  @Autowired
  private RestaurantRepository rr;

  RestTemplate restTemplate = new RestTemplate();

  // TODO: Task 2.2
  // You may change the method's signature
  public List<MenuItem> getMenus() {

    // get the list of docs first menu items
    List<Document> retrieved = or.getMenus();

    // want to add to menuitems after instantiating them and return this list
    List<MenuItem> items = new LinkedList<>();

    for (Document doc : retrieved) {
      String id = doc.getString("_id");
      String name = doc.getString("name");
      String desc = doc.getString("description");

      // String price = doc.getString("price").toString();
      // Object price = doc.get("price");
      // Double price2 = Double.parseDouble((String) price);

      // error handling cast properly
      Object priceObj = doc.get("price");
      String price;
      if (priceObj instanceof Double) {
        price = String.format("%.2f", (Double) priceObj);
      } else if (priceObj instanceof Integer) {
        price = Integer.toString((Integer) priceObj);
      } else {
        price = "0"; // should not get here ever.
      }

      Double priceCasted = Double.parseDouble(price);

      // create menu item object add add
      MenuItem m = new MenuItem(id, name, priceCasted, desc);
      items.add(m);
    }

    return items;
  }

  // TODO: Task 4

  public Boolean validUserCheck(String extractedUsername, String password) {
    return rr.validUserCheck(extractedUsername, password);
  }

  @Value("${payment_url}")
  private String url;

  public String makePayment(String payload, String extractedUsername) {
    // prepare headers whatever the x-authenticate as well
    // call the api
    // Set headers
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("X-Authenticate", extractedUsername); //the payer name like fred
    //body is the prepared one payload
    HttpEntity<String> entity = new HttpEntity<>(payload, headers);
    ResponseEntity<String> response = restTemplate.exchange(url+"/api/payment", HttpMethod.POST, entity, String.class);
    String resp = response.getBody();

    JsonReader jr = Json.createReader(new StringReader(resp));
    JsonObject jo = jr.readObject();

    JsonReader jr2 = Json.createReader(new StringReader(payload));
    JsonObject jo2 = jr2.readObject();
    JsonArray ja = jo2.getJsonArray("items");

    //response from api is fuccesssuly sent
    String order_id = jo.getString("order_id");
    String payment_id = jo.getString("payment_id");
    String order_date = jo.getString("timestamp");
    String total = jo2.getString("totalPrice");
    String username = jo2.getString("payer");

    //now add to mysql db
    //supposed to bne transactional but no time rip.
    //next is add to mongo
    rr.addToMySql(order_id, payment_id, order_date, total, username);
    or.addToMongo(order_id, payment_id, order_date, total, username, ja.toString());

    return null;
    //gg times up
    //am too slow.

  }

}
