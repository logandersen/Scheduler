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

import java.io.IOException;
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

    private static Connection conn;
    private Locale locale;
    private ResourceBundle rs;

    @FXML
    private void login(ActionEvent event) throws SQLException, IOException {
        ErrorText.setVisible(false);
        var username = Username.getText();
        var password= Password.getText();
        var authenticated = DBService.authenticate(conn,username,password);
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.rs = resourceBundle;
        Zone.setText(ZoneId.systemDefault().toString());
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    public static Connection getConn(){return conn;}

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
