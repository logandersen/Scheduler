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
import java.util.ResourceBundle;

public class LoginController implements Initializable{

    @FXML
    private TextField Username;
    @FXML
    private TextField Password;
    @FXML
    private Label ErrorText;

    private Connection conn;

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
            ErrorText.setText("Username or Password is incorrect");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        conn = DBConnector.connectToDB();
    }
}
