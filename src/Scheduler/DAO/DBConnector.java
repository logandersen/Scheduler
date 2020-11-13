package Scheduler.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnector {
    private static Connection conn = null;
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
    public static void closeConnection(){
        try {
            conn.close();
        }
        catch (SQLException ex){
            System.out.println("Error Closing DB: " + ex.getMessage());
        }
    }

}
