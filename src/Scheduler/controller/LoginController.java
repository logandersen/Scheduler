package Scheduler.controller;
import Scheduler.DAO.DBConnector;
import Scheduler.DAO.DBService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.*;

import javafx.event.ActionEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController implements Initializable{

    @FXML
    private TextField Username;
    @FXML
    private TextField Password;
    @FXML
    private Label ErrorText;
    @FXML
    private Label Zone;

    private Connection conn;
    private Locale locale;

    @FXML
    private void login(ActionEvent event) throws SQLException {
        ErrorText.setVisible(false);
        var username = Username.getText();
        var password= Password.getText();
        var authenticated = DBService.authenticate(conn,username,password);
        if(authenticated){
            System.out.println("Authenticated");
        }
        else{
            ErrorText.setVisible(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Zone.setText(ZoneId.systemDefault().toString());
        conn = DBConnector.connectToDB();
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
