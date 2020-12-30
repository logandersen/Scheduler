package Scheduler.controller;

import Scheduler.DAO.DBConnector;
import Scheduler.DAO.DBService;
import Scheduler.model.Appointment;
import Scheduler.model.Customer;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

import javax.swing.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.ResourceBundle;

public class CustomerApptListController implements Initializable{

    //Customer
    @FXML
    private TableView<Customer> customerTableView;
    @FXML
    private TableColumn<Customer, String> customerName;
    @FXML
    private TableColumn<Customer, String> address;
    @FXML
    private TableColumn<Customer, String> postalCode;
    @FXML
    private TableColumn<Customer, String> phone;
    @FXML
    private TableColumn<Customer, String> division;
    @FXML
    private TableColumn<Customer, String> country;
    @FXML
    private TableColumn<Customer, String> createdDate;
    @FXML
    private TableColumn<Customer, String> createdBy;

    //Appointment
    @FXML
    private TableView<Appointment> appointmentTableView;
    @FXML
    private TableColumn<Appointment, String> apptId;
    @FXML
    private TableColumn<Appointment, String> title;
    @FXML
    private TableColumn<Appointment, String> description;
    @FXML
    private TableColumn<Appointment, String> location;
    @FXML
    private TableColumn<Appointment, String> contact;
    @FXML
    private TableColumn<Appointment, String> type;
    @FXML
    private TableColumn<Appointment, String> start;
    @FXML
    private TableColumn<Appointment, String> end;
    @FXML
    private TableColumn<Appointment, String> apptCustomerName;
    @FXML
    private TabPane tabPane;
    @FXML
    private ToggleGroup displayBy;
    @FXML
    private ToggleButton weekToggle;
    @FXML
    private ToggleButton monthToggle;
    @FXML
    private Label dateSelectionText;


    private Connection conn;
    private ResourceBundle rs;
    private boolean apptTabSelected;
    private Month month;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.rs = resourceBundle;
        this.conn = LoginController.getConn();
        if(apptTabSelected){
            setApptTab();
        }
        try {
            //Customer Table setup
            customerTableView.setItems(DBService.getAllCustomers(conn));
            customerName.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));
            address.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
            postalCode.setCellValueFactory(new PropertyValueFactory<Customer, String>("postalCode"));
            phone.setCellValueFactory(new PropertyValueFactory<Customer, String>("phone"));
            createdDate.setCellValueFactory(new PropertyValueFactory<Customer, String>("createdDate"));
            createdBy.setCellValueFactory(new PropertyValueFactory<Customer, String>("createdBy"));
            division.setCellValueFactory(new PropertyValueFactory<Customer, String>("divisionName"));
            country.setCellValueFactory(new PropertyValueFactory<Customer, String>("countryName"));

            //Customer Double Click
            customerTableView.setRowFactory(tv-> {
                var row = new TableRow<Customer>();
                row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && !row.isEmpty()) {
                        Customer cust = row.getItem();
                        try {
                            editCustomer(cust,event);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                return row;
            });

            //Appointment Table setup
            appointmentTableView.setItems(DBService.getAllAppointments(conn));
            apptId.setCellValueFactory(new PropertyValueFactory<Appointment, String>("id"));
            title.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
            description.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));
            location.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));
            contact.setCellValueFactory(new PropertyValueFactory<Appointment, String>("contactName"));
            type.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));
            start.setCellValueFactory(new PropertyValueFactory<Appointment, String>("start"));
            end.setCellValueFactory(new PropertyValueFactory<Appointment, String>("end"));
            apptCustomerName.setCellValueFactory(new PropertyValueFactory<Appointment, String>("customerName"));

            //Customer Double Click
            appointmentTableView.setRowFactory(tv-> {
                var row = new TableRow<Appointment>();
                row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && !row.isEmpty()) {
                        Appointment appt = row.getItem();
                        try {
                            editAppt(appt,event);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                return row;
            });

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        displayBy = new ToggleGroup();
        monthToggle.setToggleGroup(displayBy);
        weekToggle.setToggleGroup(displayBy);
        displayBy.selectToggle(monthToggle);
        setCurrentMonth();
        dateSelectionText.setText(this.month.getDisplayName(TextStyle.FULL,rs.getLocale()));
    }

    private void editCustomer(Customer cust, MouseEvent event) throws Exception{
        FXMLLoader custLoader = new FXMLLoader();
        custLoader.setLocation(getClass().getResource("../view/Customer.fxml"));
        custLoader.setResources(rs);
        Parent custParent = custLoader.load();
        CustomerController custController =  custLoader.getController();
        custController.setPageType("Edit");
        Customer selectedCust = customerTableView.getSelectionModel().getSelectedItem();
        custController.setSelectedCustomer(selectedCust);
        Scene partScene = new Scene(custParent);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(partScene);
        appStage.show();
    }

    private void editAppt(Appointment appt, MouseEvent event) throws Exception{
        FXMLLoader apptLoader = new FXMLLoader();
        apptLoader.setLocation(getClass().getResource("../view/Appointment.fxml"));
        apptLoader.setResources(rs);
        Parent apptParent = apptLoader.load();
        AppointmentController apptController =  apptLoader.getController();
        apptController.setPageType("Edit");
        Appointment selectedAppt = appointmentTableView.getSelectionModel().getSelectedItem();
        apptController.setSelectedAppointment(selectedAppt);
        Scene partScene = new Scene(apptParent);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(partScene);
        appStage.show();
    }

    @FXML
    private void newCustomer(ActionEvent event) throws Exception {
        FXMLLoader custLoader = new FXMLLoader();
        custLoader.setLocation(getClass().getResource("../view/Customer.fxml"));
        custLoader.setResources(rs);
        Parent custParent = custLoader.load();
        CustomerController custController =  custLoader.getController();
        custController.setPageType("New");
        Scene partScene = new Scene(custParent);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(partScene);
        appStage.show();
    }
    @FXML
    private void newAppointment(ActionEvent event) throws Exception {
        FXMLLoader apptLoader = new FXMLLoader();
        apptLoader.setLocation(getClass().getResource("../view/Appointment.fxml"));
        apptLoader.setResources(rs);
        Parent apptParent = apptLoader.load();
        AppointmentController apptController =  apptLoader.getController();
        apptController.setPageType("New");
        Scene partScene = new Scene(apptParent);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(partScene);
        appStage.show();
    }

    private void setApptTab(){
        tabPane.getSelectionModel().select(1);
    }
    public void setApptTabSelected(){
        this.apptTabSelected = true;
    }
    private void setCurrentMonth(){
        this.month = LocalDate.now().getMonth();
    }
    private void setMonth(Integer monthInt){
        this.month = Month.of(monthInt);
        dateSelectionText.setText(this.month.getDisplayName(TextStyle.FULL,rs.getLocale()));
    }
    private void setCurrentWeek(){
        var today = LocalDate.now();
        WeekFields weekFields = WeekFields.of(rs.getLocale());

    }
    private void setWeek(){

    }
    @FXML
    private void prevDate(ActionEvent event){
        var selectedToggle = (ToggleButton)displayBy.getSelectedToggle();
        if(selectedToggle.getText().equals("Month")){
            var currentMonthInt = this.month.getValue();
            var monthInt = currentMonthInt == 1 ? 12 : currentMonthInt -1;
            setMonth(monthInt);
        }
    }
    @FXML
    private void nextDate(ActionEvent event){
        var selectedToggle = (ToggleButton)displayBy.getSelectedToggle();
        if(selectedToggle.getText().equals("Month")){
            var currentMonthInt = this.month.getValue();
            var monthInt = currentMonthInt == 12 ? 1 : currentMonthInt + 1;
            setMonth(monthInt);
        }
    }
}
