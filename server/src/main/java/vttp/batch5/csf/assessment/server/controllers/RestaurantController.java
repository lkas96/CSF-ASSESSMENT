package vttp.batch5.csf.assessment.server.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vttp.batch5.csf.assessment.server.configs.CORSConfig;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import vttp.batch5.csf.assessment.server.models.MenuItem;
import vttp.batch5.csf.assessment.server.services.RestaurantService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.smartcardio.CardTerminal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;


@CrossOrigin(origins="http://localhost:4200")
@RestController
@RequestMapping(path="/api", produces=MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {

  @Autowired
  private RestaurantService rs;

  // TODO: Task 2.2
  // You may change the method's signature
  @GetMapping("/menu") 
  public ResponseEntity<String> getMenus() {
    
    //return the menu items from mongo db
    JsonArrayBuilder jab = Json.createArrayBuilder();

    rs.getMenus().stream().map(MenuItem::toJson).forEach(jab::add);

    JsonArray results = jab.build();

    return ResponseEntity.ok(results.toString());
  }

  // TODO: Task 4
  // Do not change the method's signature
  @PostMapping("/food_order")  
  public ResponseEntity<String> postFoodOrder(@RequestBody String payload) {

    //first validate the user
    //get username and apssword from thee request body.
    JsonReader jr = Json.createReader(new StringReader(payload));
    JsonObject jo = jr.readObject();
    
    String extractedUsername = jo.getString("username");
    System.out.printf("extracted username", extractedUsername);
    String password = jo.getString("password");

    Boolean validUser = rs.validUserCheck(extractedUsername, password);

    if (!validUser){
      //if returned false means wrong password/combo whatever
      //return 401 with message
      String message = "Invalid username and/or password!";
      JsonObject job = Json.createObjectBuilder()
                        .add("message", message)
                        .build();

      return ResponseEntity.status(401).body(job.toString());
    }

    //if valid user then proceeding now....
    //generate random 8 digit id
    String gen8 = UUID.randomUUID().toString().replace("-","").substring(0,8);
    
    //calculate total Amount
    JsonArray itemArray = jo.getJsonArray("items");
    List<MenuItem> items = new ArrayList<>();
    Double totalPrice = 0.0;

    for (int i = 0; i < itemArray.size(); i++ ){
      JsonObject anItem = itemArray.getJsonObject(i);
      String quantity = anItem.getString("quantity");
      String unitPrice = anItem.getString("unitPrice");
      //convert both to numbers
      int quantity2 = Integer.parseInt(quantity);
      double unitPrice2 = Double.parseDouble(unitPrice);
      totalPrice = (quantity2 * unitPrice2) + totalPrice;
    }
    

    //now make payment to the api thingy
    //prepare the payload/response body first
    JsonObject job = Json.createObjectBuilder()
                      .add("order_id", gen8)
                      .add("payer", extractedUsername)
                      .add("payee", "Lawson Kwek An Shi")
                      .add("payment", totalPrice)
                      .build();
    
    String resp = rs.makePayment(job.toString(), extractedUsername);


    return ResponseEntity.ok("{}");
  }
}
