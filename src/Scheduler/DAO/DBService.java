package Scheduler.DAO;
import Scheduler.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;


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
    public static ObservableList<User> getAllUsers(Connection conn) throws SQLException{
        String qry = "SELECT * FROM users";
        PreparedStatement statement = conn.prepareStatement(qry);
        statement.execute();
        var rs = statement.getResultSet();
        ObservableList<User> users = FXCollections.observableArrayList();

        while(rs.next()){
            var user = new User();
            user.setId(rs.getInt("User_ID"));
            user.setUsername(rs.getString("User_Name"));
            users.add(user);
        }
        return users;
    }
    public static ObservableList<Customer> getAllCustomers(Connection conn) throws SQLException {
        String qry = "SELECT * FROM customers";
        PreparedStatement statement = conn.prepareStatement(qry);
        statement.execute();
        var rs = statement.getResultSet();
        ObservableList<Customer> customers = FXCollections.observableArrayList();

        while(rs.next()){
            var customer = new Customer();
            customer.setCustomerName(rs.getString("Customer_Name"));
            customer.setId(rs.getInt("Customer_ID"));
            customer.setAddress(rs.getString("Address"));
            customer.setPhone(rs.getString("Phone"));
            customer.setPostalCode(rs.getString("Postal_Code"));
            customer.setCreatedDate(rs.getDate("Create_Date"));
            customer.setCreatedBy(rs.getString("Created_By"));
            customer.setLastUpdate(rs.getTimestamp("Last_Update"));
            customer.setLastUpdatedBy(rs.getString("Last_Updated_By"));
            customer.setDivisionID(rs.getInt("Division_ID"));

            //Get Division
            var division = getDivision(conn, customer.getDivisionID());
            customer.setDivisionName(division.name);
            //Get Country
            var country = getCountry(conn,division.countryID);
            customer.setCountryName(country.getName());
            customers.add(customer);
        }
        return customers;
    }

    public static void updateCustomer(Connection conn, Customer customer) throws SQLException {
        String qry = "UPDATE customers SET Customer_Name=?,Address=?,Phone=?,Postal_Code=?,Last_Update=?,Last_Updated_By=?,Division_ID=? WHERE Customer_ID =?;";
        PreparedStatement statement = conn.prepareStatement(qry);
        statement.setString(1,customer.getCustomerName());
        statement.setString(2,customer.getAddress());
        statement.setString(3, customer.getPhone());
        statement.setString(4, customer.getPostalCode());
        var utilDate = new java.util.Date();
        statement.setDate(5, new java.sql.Date(utilDate.getTime()));
        statement.setString(6,customer.getLastUpdatedBy());
        statement.setInt(7,customer.getDivisionID());
        statement.setInt(8,customer.getId());

        statement.execute();
    }
    public static void insertCustomer(Connection conn, Customer customer) throws SQLException {
        String qry = "INSERT INTO customers (Customer_Name,Address,Phone,Postal_Code,Create_Date,Created_By,Last_Update,Last_Updated_By,Division_ID) " +
                "VALUES(?,?,?,?,?,?,?,?,?)";
        PreparedStatement statement = conn.prepareStatement(qry);
        statement.setString(1,customer.getCustomerName());
        statement.setString(2,customer.getAddress());
        statement.setString(3, customer.getPhone());
        statement.setString(4, customer.getPostalCode());
        var utilDate = new java.util.Date();
        statement.setTimestamp(5,new Timestamp(System.currentTimeMillis()));
        statement.setString(6,customer.getCreatedBy());
        statement.setDate(7, new java.sql.Date(utilDate.getTime()));
        statement.setString(8,customer.getLastUpdatedBy());
        statement.setInt(9,customer.getDivisionID());

        statement.execute();
}
    public static boolean deleteCustomer(Connection conn, Customer customer) throws SQLException{
        if(foundAssociatedAppointments(conn,customer.getId())){
            return false;
        }
        String qry = "DELETE FROM customers WHERE Customer_ID =?";
        PreparedStatement statement = conn.prepareStatement(qry);
        statement.setInt(1,customer.getId());
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
        country.setName(rs.getString("Country"));
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
            country.setId(rs.getInt("Country_ID"));
            country.setName(rs.getString("Country"));
            countries.add(country);
        }
        return countries;
    }

    public static ObservableList<Appointment> getAllAppointments(Connection conn) throws SQLException {
        String qry = "SELECT appointments.*,Contact_Name,User_Name,Customer_Name " +
                "FROM appointments " +
                "LEFT JOIN contacts ON contacts.Contact_ID=appointments.Contact_ID " +
                "LEFT JOIN users ON users.User_ID=appointments.User_ID " +
                "LEFT JOIN customers ON customers.Customer_ID=appointments.Customer_ID";
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
            var startTimestamp =rs.getTimestamp("Start");
            var startLocal = startTimestamp.toLocalDateTime();
            var startOffset = ZonedDateTime.ofInstant(startLocal,ZoneOffset.UTC,ZoneId.systemDefault());
            appt.setStart(startOffset);
            var endTimestamp =rs.getTimestamp("End");
            var endLocal = endTimestamp.toLocalDateTime();
            var endOffset = ZonedDateTime.ofInstant(endLocal,ZoneOffset.UTC,ZoneId.systemDefault());
            appt.setEnd(endOffset);
            appt.setCreatedDate(rs.getDate("Create_Date"));
            appt.setCreatedBy(rs.getString("Created_By"));
            appt.setLastUpdate(rs.getTimestamp("Last_Update"));
            appt.setLastUpdatedBy(rs.getString("Last_Updated_By"));
            appt.setCustomerId(rs.getInt("Customer_ID"));
            appt.setUserId(rs.getInt("User_ID"));
            appt.setContactId(rs.getInt("Contact_ID"));
            appt.setContactName(rs.getString("Contact_Name"));
            appt.setCustomerName(rs.getString("Customer_Name"));
            appt.setUserName(rs.getString("User_Name"));
            appointments.add(appt);
        }
        return appointments;
    }
    public static boolean foundAssociatedAppointments(Connection conn, Integer custID) throws SQLException {
        String qry = "SELECT * FROM appointments WHERE Customer_ID =" + custID;
        PreparedStatement statement = conn.prepareStatement(qry);
        statement.execute();
        var rs = statement.getResultSet();
        return rs.next();
    }
    public static void updateAppointment(Connection conn, Appointment appointment) throws SQLException {
        String qry = "UPDATE appointments SET Title=?,Description=?," +
                "Location=?,Type=?,Start=?,End=?,Last_Update=?," +
                "Last_Updated_By=?,Customer_ID=?,User_ID=?,Contact_ID=? WHERE Appointment_ID =?";
        PreparedStatement statement = conn.prepareStatement(qry);
        statement.setString(1,appointment.getTitle());
        statement.setString(2,appointment.getDescription());
        statement.setString(3,appointment.getLocation());
        statement.setString(4,appointment.getType());
        var startZDT = appointment.getStartZDT();
        var startInstant = startZDT.toInstant();
        var startLocal = LocalDateTime.ofInstant(startInstant, ZoneOffset.UTC);
        statement.setTimestamp(5,Timestamp.valueOf(startLocal));
        var endZDT = appointment.getEndZDT();
        var endInstant = endZDT.toInstant();
        var endLocal = LocalDateTime.ofInstant(endInstant, ZoneOffset.UTC);
        statement.setTimestamp(6,Timestamp.valueOf(endLocal));
        var utilDate = new java.util.Date();
        statement.setDate(7, new java.sql.Date(utilDate.getTime()));
        statement.setString(8,appointment.getLastUpdatedBy());
        statement.setInt(9,appointment.getCustomerId());
        statement.setInt(10,appointment.getUserId());
        statement.setInt(11,appointment.getContactId());
        statement.setInt(12,appointment.getId());
        statement.execute();
    }
    public static void insertAppointment(Connection conn, Appointment appointment) throws SQLException {
        String qry = "INSERT INTO appointments (Title,Description,Location,Type,Start,End,Create_Date,Created_By,Last_Update,Last_Updated_By,Customer_ID,User_ID,Contact_ID) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement statement = conn.prepareStatement(qry);
        statement.setString(1,appointment.getTitle());
        statement.setString(2,appointment.getDescription());
        statement.setString(3,appointment.getLocation());
        statement.setString(4,appointment.getType());
        var startZDT = appointment.getStartZDT();
        var startInstant = startZDT.toInstant();
        var startLocal = LocalDateTime.ofInstant(startInstant, ZoneOffset.UTC);
        statement.setTimestamp(5,Timestamp.valueOf(startLocal));
        var endZDT = appointment.getEndZDT();
        var endInstant = endZDT.toInstant();
        var endLocal = LocalDateTime.ofInstant(endInstant, ZoneOffset.UTC);
        statement.setTimestamp(6,Timestamp.valueOf(endLocal));
        statement.setTimestamp(7,new Timestamp(System.currentTimeMillis()));
        var utilDate = new java.util.Date();
        statement.setString(8,appointment.getCreatedBy());
        statement.setDate(9, new java.sql.Date(utilDate.getTime()));
        statement.setString(10,appointment.getLastUpdatedBy());
        statement.setInt(11,appointment.getCustomerId());
        statement.setInt(12,appointment.getUserId());
        statement.setInt(13,appointment.getContactId());
        statement.execute();
    }
    public static boolean deleteAppointment(Connection conn, Appointment appointment) throws SQLException{
        String qry = "DELETE FROM appointments WHERE appointment_ID =?";
        PreparedStatement statement = conn.prepareStatement(qry);
        statement.setInt(1,appointment.getId());
        statement.execute();
        if(statement.getUpdateCount() > 0){
            return true;
        };
        return false;
    }

    public static ObservableList<Contact> getAllContacts(Connection conn) throws SQLException{
        String qry = "SELECT * FROM contacts";
        PreparedStatement statement = conn.prepareStatement(qry);
        statement.execute();
        var rs = statement.getResultSet();
        ObservableList<Contact> contacts = FXCollections.observableArrayList();

        while(rs.next()){
            var contact = new Contact();
            contact.setId(rs.getInt("Contact_ID"));
            contact.setName(rs.getString("Contact_Name"));
            contact.setEmail(rs.getString("Email"));
            contacts.add(contact);
        }
        return contacts;
    }
}
