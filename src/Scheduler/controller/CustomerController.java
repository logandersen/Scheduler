package Scheduler.controller;

import Scheduler.DAO.DBService;
import Scheduler.model.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

public class CustomerController implements Initializable{

    @FXML
    private TextField Id;
    @FXML
    private TextField Name;
    @FXML
    private TextField Phone;
    @FXML
    private TextField Address;
    @FXML
    private TextField PostalCode;
    @FXML
    private Label CreatedOn;
    @FXML
    private Label CreatedBy;
    @FXML
    private Label LastUpdated;
    @FXML
    private Label LastUpdatedBy;
    @FXML
    private ComboBox Division;
    @FXML
    private ComboBox Country;


    private Connection conn;
    private Locale locale;
    private ResourceBundle rs;
    private String pageType;
    private Customer customer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.conn = LoginController.getConn();
        this.rs = resourceBundle;
    }
    public void setPageType(String pageType){
        this.pageType = pageType;
    }
    public void setSelectedCustomer(Customer selectedCustomer) {
        Id.setText(String.valueOf(selectedCustomer.getId()));
        Name.setText(selectedCustomer.getCustomerName());
        Phone.setText(String.valueOf(selectedCustomer.getPhone()));
        Address.setText(String.valueOf(selectedCustomer.getAddress()));
        PostalCode.setText(String.valueOf(selectedCustomer.getPostalCode()));
        CreatedOn.setText(String.valueOf(selectedCustomer.getCreatedDate().toString()));
        CreatedBy.setText(String.valueOf(selectedCustomer.getCreatedBy()));
        LastUpdated.setText(String.valueOf(selectedCustomer.getLastUpdate().toString()));
        LastUpdatedBy.setText(String.valueOf(selectedCustomer.getLastUpdatedBy()));

        customer = selectedCustomer;
    }
}
