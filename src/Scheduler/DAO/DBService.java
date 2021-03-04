package Scheduler.DAO;
import Scheduler.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/** Class to provide all the service calls to the MySQL database */
public class DBService {

    private static String Username;
    private static int UserID;
    /** Takes the username and password and returns true or false for authentication */
    public static boolean authenticate(Connection conn, String username, String password) throws SQLException{
        String qry = "SELECT * FROM users WHERE User_Name = ? AND BINARY Password = ?";
        PreparedStatement statement = conn.prepareStatement(qry);
        statement.setString(1,username);
        statement.setString(2,password);
        statement.execute();
        var rs = statement.getResultSet();
        int count = 0;
        while (rs.next()) {
            UserID = rs.getInt("User_ID");
            Username = username;
            return true;
        }
        return false;
    }
    /** Gets the username property from the class  */
    public static String getUserName(){
        return Username;
    }
    /** SQL call to return all users from the database */
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
    /** SQL call to return all customers from the database */
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
            var createTimestamp =rs.getTimestamp("Create_Date");
            var createLocal = createTimestamp.toLocalDateTime();
            var createOffset = ZonedDateTime.ofInstant(createLocal,ZoneOffset.UTC,ZoneId.systemDefault());
            customer.setCreatedDate(createOffset);
            customer.setCreatedBy(rs.getString("Created_By"));
            var updateTimestamp =rs.getTimestamp("Last_Update");
            var updateLocal = updateTimestamp.toLocalDateTime();
            var updateOffset = ZonedDateTime.ofInstant(updateLocal,ZoneOffset.UTC,ZoneId.systemDefault());
            customer.setLastUpdate(updateOffset);
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
    /** SQL call to update the specified customer in the database */
    public static void updateCustomer(Connection conn, Customer customer) throws SQLException {
        String qry = "UPDATE customers SET Customer_Name=?,Address=?,Phone=?,Postal_Code=?,Last_Update=?,Last_Updated_By=?,Division_ID=? WHERE Customer_ID =?;";
        PreparedStatement statement = conn.prepareStatement(qry);
        statement.setString(1,customer.getCustomerName());
        statement.setString(2,customer.getAddress());
        statement.setString(3, customer.getPhone());
        statement.setString(4, customer.getPostalCode());
        var nowLocal = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
        statement.setTimestamp(5, Timestamp.valueOf(nowLocal));
        statement.setString(6,customer.getLastUpdatedBy());
        statement.setInt(7,customer.getDivisionID());
        statement.setInt(8,customer.getId());

        statement.execute();
    }
    /** SQL call to create a new customer in the database */
    public static void insertCustomer(Connection conn, Customer customer) throws SQLException {
        String qry = "INSERT INTO customers (Customer_Name,Address,Phone,Postal_Code,Create_Date,Created_By,Last_Update,Last_Updated_By,Division_ID) " +
                "VALUES(?,?,?,?,?,?,?,?,?)";
        PreparedStatement statement = conn.prepareStatement(qry);
        statement.setString(1,customer.getCustomerName());
        statement.setString(2,customer.getAddress());
        statement.setString(3, customer.getPhone());
        statement.setString(4, customer.getPostalCode());
        var nowLocal = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
        statement.setTimestamp(5,Timestamp.valueOf(nowLocal));
        statement.setString(6,customer.getCreatedBy());
        statement.setTimestamp(7, Timestamp.valueOf(nowLocal));
        statement.setString(8,customer.getLastUpdatedBy());
        statement.setInt(9,customer.getDivisionID());

        statement.execute();
}
    /** SQL call to delete the specified customer and all associated appointments from the database*/
    public static boolean deleteCustomer(Connection conn, Customer customer) throws SQLException{
        DeleteAssociatedAppointments(conn, customer.getId());
        String qry = "DELETE FROM customers WHERE Customer_ID =?";
        PreparedStatement statement = conn.prepareStatement(qry);
        statement.setInt(1,customer.getId());
        statement.execute();
        if(statement.getUpdateCount() > 0){
           return true;
        };
        return false;
    }
    /** SQL call to return division by ID from the database */
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
    /** SQL call to return all divisions from the database */
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
    /** SQL call to return country by id from the database */
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
    /** SQL call to return all countries from the database */
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
    /** SQL call to return all appointments from the database */
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
            var createTimestamp =rs.getTimestamp("Create_Date");
            var createLocal = createTimestamp.toLocalDateTime();
            var createOffset = ZonedDateTime.ofInstant(createLocal,ZoneOffset.UTC,ZoneId.systemDefault());
            appt.setCreatedDate(createOffset);
            appt.setCreatedBy(rs.getString("Created_By"));
            var updateTimestamp =rs.getTimestamp("Last_Update");
            var updateLocal = updateTimestamp.toLocalDateTime();
            var updateOffset = ZonedDateTime.ofInstant(updateLocal,ZoneOffset.UTC,ZoneId.systemDefault());
            appt.setLastUpdate(updateOffset);
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
    /** SQL call to check for appointments associated to the specified customer */
    public static void DeleteAssociatedAppointments(Connection conn, Integer custID) throws SQLException {
        String qry = "DELETE FROM appointments WHERE Customer_ID =" + custID;
        PreparedStatement statement = conn.prepareStatement(qry);
        statement.execute();
    }
    /** SQL call to return all  from the database */
    public static boolean apptSameTime(Connection conn, String start, String end,Integer apptId) throws SQLException{
        String qry = "SELECT * FROM appointments " +
            "WHERE Appointment_ID<>? AND ((NOT(End <? OR Start >?) AND Start != ? AND End != ?) OR Start <= ? AND End >= ?)";
        PreparedStatement statement = conn.prepareStatement(qry);
        statement.setInt(1,apptId);
        statement.setString(2,start);
        statement.setString(3,end);
        statement.setString(4,end);
        statement.setString(5,start);
        statement.setString(6,start);
        statement.setString(7,end);
        statement.execute();
        var rs = statement.getResultSet();
        return rs.next();
    }
    /** SQL call to update the specified appointment in the database */
    public static void updateAppointment(Connection conn, Appointment appointment) throws SQLException {
        String qry = "UPDATE appointments SET Title=?,Description=?," +
                "Location=?,Type=?,Start=?,End=?,Last_Update=?," +
                "Last_Updated_By=?,Customer_ID=?,User_ID=?,Contact_ID=? WHERE Appointment_ID =?";
        PreparedStatement statement = conn.prepareStatement(qry);
        statement.setString(1,appointment.getTitle());
        statement.setString(2,appointment.getDescription());
        statement.setString(3,appointment.getLocation());
        statement.setString(4,appointment.getType());
        var timestampFormat =DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        var startZDT = appointment.getStartZDT();
        var startInstant = startZDT.toInstant();
        var startLocal = LocalDateTime.ofInstant(startInstant, ZoneOffset.UTC);
        var startString = startLocal.format(timestampFormat);
        statement.setTimestamp(5,Timestamp.valueOf(startString));
        var endZDT = appointment.getEndZDT();
        var endInstant = endZDT.toInstant();
        var endLocal = LocalDateTime.ofInstant(endInstant, ZoneOffset.UTC);
        var endString = endLocal.format(timestampFormat);
        statement.setTimestamp(6,Timestamp.valueOf(endString));
        var nowLocal = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
        statement.setTimestamp(7,Timestamp.valueOf(nowLocal));
        statement.setString(8,appointment.getLastUpdatedBy());
        statement.setInt(9,appointment.getCustomerId());
        statement.setInt(10,appointment.getUserId());
        statement.setInt(11,appointment.getContactId());
        statement.setInt(12,appointment.getId());
        statement.execute();
    }
    /** SQL call to create a new appointment in the database */
    public static void insertAppointment(Connection conn, Appointment appointment) throws SQLException {
        String qry = "INSERT INTO appointments (Title,Description,Location,Type,Start,End,Create_Date,Created_By,Last_Update,Last_Updated_By,Customer_ID,User_ID,Contact_ID) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement statement = conn.prepareStatement(qry);
        statement.setString(1,appointment.getTitle());
        statement.setString(2,appointment.getDescription());
        statement.setString(3,appointment.getLocation());
        statement.setString(4,appointment.getType());
        var timestampFormat =DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        var startZDT = appointment.getStartZDT();
        var startInstant = startZDT.toInstant();
        var startLocal = LocalDateTime.ofInstant(startInstant, ZoneOffset.UTC);
        var startString = startLocal.format(timestampFormat);
        statement.setTimestamp(5,Timestamp.valueOf(startString));
        var endZDT = appointment.getEndZDT();
        var endInstant = endZDT.toInstant();
        var endLocal = LocalDateTime.ofInstant(endInstant, ZoneOffset.UTC);
        var endString = endLocal.format(timestampFormat);
        statement.setTimestamp(6,Timestamp.valueOf(endString));
        var nowLocal = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
        statement.setTimestamp(7,Timestamp.valueOf(nowLocal));

        statement.setString(8,appointment.getCreatedBy());
        statement.setTimestamp(9, Timestamp.valueOf(nowLocal));
        statement.setString(10,appointment.getLastUpdatedBy());
        statement.setInt(11,appointment.getCustomerId());
        statement.setInt(12,appointment.getUserId());
        statement.setInt(13,appointment.getContactId());
        statement.execute();
    }
    /** SQL call to delete the specified appointment in the database */
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
    /** SQL call to return all contacts from the database */
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
    /** SQL call to return all appointments for the user within 15 minutes of logging in */
    public static ObservableList<Appointment> apptWithinFifteen(Connection conn) throws SQLException {
        String qry = "SELECT appointments.*,Customer_Name FROM appointments " +
                "LEFT JOIN customers ON customers.Customer_ID=appointments.Customer_ID " +
                "WHERE TIMEDIFF(Start,?) <= TIME('00:15:00') AND TIMEDIFF(Start,?) >= TIME('00:00:00') AND User_ID = ?;";
        PreparedStatement statement = conn.prepareStatement(qry);
        var nowLocal = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
        var dateTimeString = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        var nowString = nowLocal.format(dateTimeString);
        statement.setString(1,nowString);
        statement.setString(2,nowString);
        statement.setInt(3,UserID);
        statement.execute();
        var rs = statement.getResultSet();
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        while(rs.next()){
            var appt = new Appointment();
            appt.setId(rs.getInt("Appointment_ID"));
            var startTimestamp =rs.getTimestamp("Start");
            var startLocal = startTimestamp.toLocalDateTime();
            var startOffset = ZonedDateTime.ofInstant(startLocal,ZoneOffset.UTC,ZoneId.systemDefault());
            appt.setStart(startOffset);
            appt.setCustomerName(rs.getString("Customer_Name"));
            appointments.add(appt);
        }
        return appointments;
    }
    /** SQL call to return appointments grouped by month and type with a count */
    public static ObservableList<ApptGroupCount> apptGroupCount(Connection conn) throws SQLException {
        String qry = "SELECT date_format(Start, '%m-%Y') AS Month,Type, count(Appointment_ID) AS Count " +
            "FROM WJ05WDT.appointments " +
            "GROUP BY Month,Type;";
        PreparedStatement statement = conn.prepareStatement(qry);
        statement.execute();
        var rs = statement.getResultSet();
        ObservableList<ApptGroupCount> apptGroupCounts = FXCollections.observableArrayList();
        while(rs.next()){
            var appt = new ApptGroupCount();
            appt.setMonth(rs.getString("Month"));
            appt.setType(rs.getString("Type"));
            appt.setCount(rs.getInt("Count"));
            apptGroupCounts.add(appt);
        }
        return apptGroupCounts;
    }
    /** SQL call to return appointments grouped by customer with a count */
    public static ObservableList<CustByDiv> custDivCount(Connection conn) throws SQLException {
        String qry = "SELECT Division, Count(Customer_ID) as CustomerCount " +
                "FROM customers " +
                "left outer join first_level_divisions on customers.Division_ID = first_level_divisions.Division_ID " +
                "group by Division";
        PreparedStatement statement = conn.prepareStatement(qry);
        statement.execute();
        var rs = statement.getResultSet();
        ObservableList<CustByDiv> custCounts = FXCollections.observableArrayList();
        while(rs.next()){
            var custByDiv = new CustByDiv();
            custByDiv.setDivision(rs.getString("Division"));
            custByDiv.setCount(rs.getInt("CustomerCount"));
            custCounts.add(custByDiv);
        }
        return custCounts;
    }
}
