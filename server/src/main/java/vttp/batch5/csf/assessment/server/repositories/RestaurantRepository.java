package vttp.batch5.csf.assessment.server.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

// Use the following class for MySQL database
@Repository
public class RestaurantRepository {

    @Autowired
    private JdbcTemplate template;

    public Boolean validUserCheck(String extractedUsername, String password) {
        final String SELECT_USER = "SELECT * FROM customers where username = ? and password = ?";

        SqlRowSet rs = template.queryForRowSet(SELECT_USER, extractedUsername, password);

        while (rs.next()){
            //means valid username and password combination, 
            return true;
        }

        return false;
    }

    public void addToMySql(String order_id,String payment_id,String order_date,String total,String username) {
        // // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'addToMySql'");
        final String SQL_ADD = "INSERT INTO place_orders values (?,?,?,?,?)";

        int isAdded = template.update(SQL_ADD, order_id, payment_id, order_date, total, username);

        if (isAdded == 1) {
            System.out.println("Poo entry added successfully");
        } else {
            System.out.println("Poo entry failed to add");
        }
    }


    

}
