package vttp.batch5.csf.assessment.server.services;
import vttp.batch5.csf.assessment.server.models.MenuItem;
import vttp.batch5.csf.assessment.server.repositories.OrdersRepository;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestaurantService {

  @Autowired
  private OrdersRepository or;


  // TODO: Task 2.2
  // You may change the method's signature
  public List<MenuItem> getMenus() {
    
    //get the list of docs first menu items
    List<Document> retrieved = or.getMenus();

    //want to add to menuitems after instantiating them and return this list
    List<MenuItem> items = new LinkedList<>();

    for (Document doc: retrieved){
      String id = doc.getString("_id");
      String name = doc.getString("name");
      String desc = doc.getString("description");

      // String price = doc.getString("price").toString();
      // Object price = doc.get("price");
      // Double price2 = Double.parseDouble((String) price);

      //error handling cast properly 
      Object priceObj = doc.get("price");
      String price;
      if (priceObj instanceof Double) {
          price = String.format("%.2f", (Double) priceObj);
      } else if (priceObj instanceof Integer) {
          price = Integer.toString((Integer) priceObj);
      } else {
        price = "0"; //should not get here ever.
      }

      Double priceCasted = Double.parseDouble(price);

      //create menu item object add add
      MenuItem m = new MenuItem(id, name, priceCasted, desc);
      items.add(m);
    }

    return items;
  }

  
  // TODO: Task 4


}
