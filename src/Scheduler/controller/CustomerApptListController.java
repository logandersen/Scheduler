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
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
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
    private TableColumn<Appointment, String> customerId;


    private Connection conn;
    private ResourceBundle rs;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.rs = resourceBundle;
        this.conn = LoginController.getConn();
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
            contact.setCellValueFactory(new PropertyValueFactory<Appointment, String>("contact"));
            type.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));
            start.setCellValueFactory(new PropertyValueFactory<Appointment, String>("start"));
            end.setCellValueFactory(new PropertyValueFactory<Appointment, String>("end"));
            customerId.setCellValueFactory(new PropertyValueFactory<Appointment, String>("customerId"));

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
}