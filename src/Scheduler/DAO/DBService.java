package Scheduler.DAO;
import Scheduler.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Date;


public class DBService {

    private static String Username;

    public static boolean authenticate(Connection conn, String username, String password) throws SQLException{
        String qry = "SELECT * FROM users WHERE User_Name = ? AND BINARY Password = ?";
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
            Username = username;
            return true;
        }
        return false;
    }
    public static String getUserName(){
        return Username;
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

    public static void updateCustomer(Connection conn, Customer customer) throws SQLException {
        String qry = "UPDATE customers SET Customer_Name=?,Address=?,Phone=?,Postal_Code=?,Last_Update=?,Last_Updated_By=?,Division_ID=? WHERE Customer_ID =?;";
        PreparedStatement statement = conn.prepareStatement(qry);
        statement.setString(1,customer.customerName);
        statement.setString(2,customer.address);
        statement.setString(3, customer.phone);
        statement.setString(4, customer.postalCode);
        var utilDate = new java.util.Date();
        statement.setDate(5, new java.sql.Date(utilDate.getTime()));
        statement.setString(6,customer.lastUpdatedBy);
        statement.setInt(7,customer.divisionID);
        statement.setInt(8,customer.id);

        statement.execute();
    }
    public static void insertCustomer(Connection conn, Customer customer) throws SQLException {
        String qry = "INSERT INTO customers (Customer_Name,Address,Phone,Postal_Code,Create_Date,Created_By,Last_Update,Last_Updated_By,Division_ID) " +
                "VALUES(?,?,?,?,?,?,?,?,?)";
        PreparedStatement statement = conn.prepareStatement(qry);
        statement.setString(1,customer.customerName);
        statement.setString(2,customer.address);
        statement.setString(3, customer.phone);
        statement.setString(4, customer.postalCode);
        var utilDate = new java.util.Date();
        statement.setTimestamp(5,new Timestamp(System.currentTimeMillis()));
        statement.setString(6,customer.createdBy);
        statement.setDate(7, new java.sql.Date(utilDate.getTime()));
        statement.setString(8,customer.lastUpdatedBy);
        statement.setInt(9,customer.divisionID);

        statement.execute();
}
    public static boolean deleteCustomer(Connection conn, Customer customer) throws SQLException{
        String qry = "DELETE FROM customers WHERE Customer_ID =?";
        PreparedStatement statement = conn.prepareStatement(qry);
        statement.setInt(1,customer.id);
        statement.execute();
        if(statement.getUpdateCount() > 0){
           return true;
        };
        return false;
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
    public static ObservableList<Division> getAllDivisions(Connection conn) throws SQLException{
        String qry = "SELECT * FROM first_level_divisions";
        PreparedStatement statement = conn.prepareStatement(qry);
        statement.execute();
        var rs = statement.getResultSet();
        ObservableList<Division> Divisions = FXCollections.observableArrayList();
        while(rs.next()){
            var div = new Division();
            div.id = rs.getInt("Division_ID");
            div.name = rs.getString("Division");
            div.countryID = rs.getInt("COUNTRY_ID");
            Divisions.add(div);
        }
        return Divisions;
    }
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
    public static ObservableList<Country> getAllCountries(Connection conn) throws SQLException{
        String qry = "SELECT * FROM countries";
        PreparedStatement statement = conn.prepareStatement(qry);
        statement.execute();
        var rs = statement.getResultSet();
        ObservableList<Country> countries = FXCollections.observableArrayList();
        while(rs.next()){
            var country = new Country();
            country.id = rs.getInt("Country_ID");
            country.name = rs.getString("Country");
            countries.add(country);
        }
        return countries;
    }
    public static ObservableList<Appointment> getAllAppointments(Connection conn) throws SQLException {
        String qry = "SELECT * FROM appointments";
        PreparedStatement statement = conn.prepareStatement(qry);
        statement.execute();
        var rs = statement.getResultSet();
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        while(rs.next()){
            var appt = new Appointment();
            appt.setId(rs.getInt("Appointment_ID"));
            appt.setTitle(rs.getString("Title"));
            appt.setDescription(rs.getString("Description"));
            appt.setLocation(rs.getString("Location"));
            appt.setType(rs.getString("Type"));
            appt.setStart(rs.getTimestamp("Start").toLocalDateTime());
            appt.setEnd(rs.getTimestamp("End").toLocalDateTime());
            appt.setCreatedDate(rs.getDate("Create_Date"));
            appt.setCreatedBy(rs.getString("Created_By"));
            appt.setLastUpdate(rs.getTimestamp("Last_Update"));
            appt.setLastUpdatedBy(rs.getString("Last_Updated_By"));
            appt.setCustomerId(rs.getInt("Customer_ID"));
            appt.setUserId(rs.getInt("User_ID"));
            appt.setContactId(rs.getInt("Contact_ID"));
            appointments.add(appt);
        }
        return appointments;
    }
}
