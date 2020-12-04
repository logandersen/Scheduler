package Scheduler.controller;

import Scheduler.DAO.DBService;
import Scheduler.model.Country;
import Scheduler.model.Customer;
import Scheduler.model.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Optional;
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
    private ComboBox<Division> DivisionCB;
    @FXML
    private ComboBox<Country> CountryCB;


    private Connection conn;
    private Locale locale;
    private ResourceBundle rs;
    private String pageType;
    private Customer customer;
    private ObservableList<Division> divisions;
    private ObservableList<Country> countries;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.conn = LoginController.getConn();
        this.rs = resourceBundle;

        try {
            countries = DBService.getAllCountries(conn);
            CountryCB.setItems(countries);
            divisions = DBService.getAllDivisions(conn);
            DivisionCB.setItems(divisions);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void setPageType(String pageType){
        this.pageType = pageType;
    }
    public void setSelectedCustomer(Customer selectedCustomer) {
        customer = selectedCustomer;
        Id.setText(String.valueOf(selectedCustomer.getId()));
        Name.setText(selectedCustomer.getCustomerName());
        Phone.setText(String.valueOf(selectedCustomer.getPhone()));
        Address.setText(String.valueOf(selectedCustomer.getAddress()));
        PostalCode.setText(String.valueOf(selectedCustomer.getPostalCode()));
        CreatedOn.setText(String.valueOf(selectedCustomer.getCreatedDate().toString()));
        CreatedBy.setText(String.valueOf(selectedCustomer.getCreatedBy()));
        LastUpdated.setText(String.valueOf(selectedCustomer.getLastUpdate().toString()));
        LastUpdatedBy.setText(String.valueOf(selectedCustomer.getLastUpdatedBy()));
        var selectedDiv = selectDivision(selectedCustomer.getDivisionID());
        CountryCB.setValue(selectCountry(selectedDiv.countryID));
        countrySelected();

    }
    private Division selectDivision(int divId){
        for(Division division: divisions){
            if(division.id == divId){
                return division;
            }
        }
        return  null;
    }
    private Country selectCountry(int countryId){
        for(Country country: countries){
            if(country.id == countryId){
                return country;
            }
        }
        return  null;
    }

    @FXML
    private void countrySelected(){
        var selectedCountry = CountryCB.getValue();
        ObservableList<Division> filterDivs = FXCollections.observableArrayList();
        Division selectedDiv = null;
        for(Division div: divisions){
            if(div.countryID == selectedCountry.id) {
                filterDivs.add(div);
                if(customer != null && div.id == customer.divisionID){
                    selectedDiv = div;
                }
            }

        }
        DivisionCB.setItems(filterDivs);
        if(selectedDiv != null){
            DivisionCB.setValue(selectedDiv);
        }

    }

    @FXML
    private void cancel(ActionEvent event) throws IOException {
        loadCustomerList(event);
    }
    @FXML
    private void delete(ActionEvent event) throws Exception{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(rs.getString("cust.DeleteText"));
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() != ButtonType.OK){return;}

        var deleted = DBService.deleteCustomer(conn,customer);
        if(deleted){
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(rs.getString("cust.Deleted"));
            alert.showAndWait();
            loadCustomerList(event);
        }
        else{
            Alert failAlert = new Alert(Alert.AlertType.ERROR);
            failAlert.setContentText(rs.getString("cust.DeleteFailed"));
            failAlert.show();
        }
    }

    @FXML
    private void upsert(ActionEvent event) throws  Exception{
        if(pageType == "Edit"){
            update(event);
        }
        else {
            saveNew(event);
        }
    }
    private void update(ActionEvent event) throws Exception{
        var updateCust = new Customer();
        updateCust.customerName = Name.getText();
        updateCust.phone =  Phone.getText();
        updateCust.address = Address.getText();
        updateCust.postalCode = PostalCode.getText();
        updateCust.divisionID = DivisionCB.getValue().id;
        updateCust.lastUpdatedBy = DBService.getUserName();
        updateCust.id = Integer.parseInt(Id.getText());
        DBService.updateCustomer(conn,updateCust);
        loadCustomerList(event);
    }
    private void saveNew(ActionEvent event) throws Exception{
        var userName = DBService.getUserName();
        var newCust = new Customer();
        newCust.customerName = Name.getText();
        newCust.phone =  Phone.getText();
        newCust.address = Address.getText();
        newCust.postalCode = PostalCode.getText();
        newCust.divisionID = DivisionCB.getValue().id;
        newCust.lastUpdatedBy = userName;
        newCust.createdBy = userName;
        DBService.insertCustomer(conn,newCust);
        loadCustomerList(event);
    }

    private void loadCustomerList(ActionEvent event) throws IOException {
        FXMLLoader custListLoader = new FXMLLoader();
        custListLoader.setLocation(getClass().getResource("../view/CustomerApptList.fxml"));
        custListLoader.setResources(rs);
        Parent custListParent = custListLoader.load();

        Scene partScene = new Scene(custListParent);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(partScene);
        appStage.show();
    }
}
