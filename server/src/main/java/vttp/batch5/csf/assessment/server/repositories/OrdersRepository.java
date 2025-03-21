package vttp.batch5.csf.assessment.server.repositories;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class OrdersRepository {

  @Autowired
  private MongoTemplate template;

  // TODO: Task 2.2
  // You may change the method's signature
  // Write the native MongoDB query in the comment below
  //
  // Native MongoDB query here
  // db.menus.find().sort({name: 1});
  // get all the items in the menus collection
  public List<Document> getMenus() {

    Query q = new Query().with(Sort.by(Sort.Direction.ASC, "name"));

    List<Document> results = template.find(q, Document.class, "menus");

    System.out.println(results.toString());

    return results;
  }

  // TODO: Task 4
  // Write the native MongoDB query for your access methods in the comment below
  //
  // Native MongoDB query here

  
  public void addToMongo(String order_id, String payment_id, String order_date, String total, String username, String array) {

    Document doc = new Document();
    doc.put("_id", "ObjectId("+order_id+")");
    doc.put("order_id", order_id);
    doc.put("payment_id", payment_id);
    doc.put("username", username);
    doc.put("total", total);
    doc.put("timestamp", order_date);
    doc.put("items", array);

    template.insert(doc, "orders");
  }

}
