package Scheduler.controller;
import Scheduler.DAO.DBConnector;
import Scheduler.DAO.DBService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;
/** Controller used for the customer FXML edit and new form */
public class LoginController implements Initializable{

    @FXML
    private TextField Username;
    @FXML
    private TextField Password;
    @FXML
    private Label ErrorText;
    @FXML
    private Label Zone;

    private static Connection conn;
    private Locale locale;
    private ResourceBundle rs;

    /** Action called by the login button to validate the username and password to get into the app */
    @FXML
    private void login(ActionEvent event) throws SQLException, IOException {
        ErrorText.setVisible(false);
        var username = Username.getText();
        var password= Password.getText();
        var authenticated = DBService.authenticate(conn,username,password);
        recordLoginAttempt(username,authenticated);
        if(authenticated){
            System.out.println("Authenticated");
            FXMLLoader custListLoader = new FXMLLoader();
            custListLoader.setLocation(getClass().getResource("../view/CustomerApptList.fxml"));
            custListLoader.setResources(rs);
            Parent custListParent = custListLoader.load();
            CustomerApptListController custController = custListLoader.getController();
            custController.appointmentAlert();
            Scene partScene = new Scene(custListParent);
            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            appStage.setScene(partScene);
            appStage.show();
        }
        else{
            ErrorText.setVisible(true);
        }
    }

    /** set the resource button and system default zone id  */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.rs = resourceBundle;
        Zone.setText(ZoneId.systemDefault().toString());
    }
    /** Sets the database connection to use in the form */
    public void setConn(Connection conn) {
        this.conn = conn;
    }
    /** Gets the database connection to use in the form */
    public static Connection getConn(){return conn;}
    /** Sets the locale for the form */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }
    /** Records the login attempt in the login activity txt file */
    private void recordLoginAttempt(String username, boolean authed) throws IOException {
        String fileName = "login_activity.txt";
        FileWriter fw = new FileWriter(fileName,true);
        PrintWriter pw = new PrintWriter(fw);
        var result = authed?"Success":"Fail";
        pw.println("Username: " + username +", Auth Result: " + result + ", Timestamp: " + Instant.now().toString());
        pw.close();
    }
}
