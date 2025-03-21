package vttp.batch5.csf.assessment.server.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import vttp.batch5.csf.assessment.server.models.MenuItem;
import vttp.batch5.csf.assessment.server.services.RestaurantService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

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
  public ResponseEntity<String> postFoodOrder(@RequestBody String payload) {
    return ResponseEntity.ok("{}");
  }
}
