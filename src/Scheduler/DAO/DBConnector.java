package Scheduler.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
/** Class to authenticate and connect to the MySQL database */
public class DBConnector {
    private static Connection conn = null;
    /** Creates the Connection to the MySQL database */
    public static Connection connectToDB(){
        final var password =  "53688624176";
        final var username = "U05WDT";
        final var url = "jdbc:mysql://wgudb.ucertify.com:3306/WJ05WDT";
        var properties = new Properties();
        properties.put("user", username);
        properties.put("password", password);
        try {

            conn = DriverManager.getConnection(url, properties);
            System.out.println("Connection Successful");
        }
        catch(SQLException ex) {
            System.out.println("Sql Exception: " + ex.getMessage());
        }
        return conn;
    }
    /** Close the connection to the database */
    public static void closeConnection(){
        try {
            conn.close();
        }
        catch (SQLException ex){
            System.out.println("Error Closing DB: " + ex.getMessage());
        }
    }

}
