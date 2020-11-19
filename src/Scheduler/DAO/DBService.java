package Scheduler.DAO;
import Scheduler.model.Country;
import Scheduler.model.Customer;
import Scheduler.model.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


public class DBService {

    public static boolean authenticate(Connection conn, String username, String password) throws SQLException{
        String qry = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";
        PreparedStatement statement = conn.prepareStatement(qry);
        statement.setString(1,username);
        statement.setString(2,password);
        statement.execute();
        var rs = statement.getResultSet();
        int count = 0;
        while (rs.next()) {
            ++count;
            // Get data from the current row and use it
        }
        if(count == 1){
            return true;
        }
        return false;
    }
    public static ObservableList<Customer> getAllCustomers(Connection conn) throws SQLException {
        String qry = "SELECT * FROM customers";
        PreparedStatement statement = conn.prepareStatement(qry);
        statement.execute();
        var rs = statement.getResultSet();
        ObservableList<Customer> customers = FXCollections.observableArrayList();

        while(rs.next()){
            var customer = new Customer();
            customer.customerName = rs.getString("Customer_Name");
            customer.id = rs.getInt("Customer_ID");
            customer.address = rs.getString("Address");
            customer.phone = rs.getString("Phone");
            customer.postalCode = rs.getString("Postal_Code");
            customer.createdDate = rs.getDate("Create_Date");
            customer.createdBy = rs.getString("Created_By");
            customer.lastUpdate = rs.getTimestamp("Last_Update");
            customer.lastUpdatedBy = rs.getString("Last_Updated_By");
            customer.divisionID = rs.getInt("Division_ID");

            //Get Division
            var division = getDivision(conn, customer.divisionID);
            customer.divisionName = division.name;
            //Get Country
            var country = getCountry(conn,division.countryID);
            customer.countryName = country.name;
            customers.add(customer);
        }
        return customers;
    }
    private static Division getDivision(Connection conn, int id) throws SQLException {
        String qry = "SELECT * FROM first_level_divisions WHERE Division_ID = ?";
        PreparedStatement statement = conn.prepareStatement(qry);
        statement.setInt(1,id);
        statement.execute();
        var rs = statement.getResultSet();
        rs.next();
        var division = new Division();
        division.name = rs.getString("Division");
        division.countryID = rs.getInt("COUNTRY_ID");
        return division;
    };
    private static Country getCountry(Connection conn, int id) throws SQLException {
        String qry = "SELECT * FROM countries WHERE Country_ID = ?";
        PreparedStatement statement = conn.prepareStatement(qry);
        statement.setInt(1,id);
        statement.execute();
        var rs = statement.getResultSet();
        rs.next();
        var country = new Country();
        country.name = rs.getString("Country");
        return country;
    };
}
