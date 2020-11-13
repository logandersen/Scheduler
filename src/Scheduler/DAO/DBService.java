package Scheduler.DAO;
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

}
