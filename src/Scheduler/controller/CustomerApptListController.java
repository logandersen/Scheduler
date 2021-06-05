package Scheduler.controller;

import Scheduler.DAO.DBConnector;
import Scheduler.DAO.DBService;
import Scheduler.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.TimeZone;
/** Controller used for the CustomerApptList FXML form*/
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
    @FXML
    private TextField searchTxt;

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
    private ObservableList<Appointment> allAppts;

    @FXML
    private TableView<ApptGroupCount> reportTableView;
    @FXML
    private TableColumn<ApptGroupCount, String> repMonth;
    @FXML
    private TableColumn<ApptGroupCount, String> repType;
    @FXML
    private TableColumn<ApptGroupCount, String> repCount;
    private ObservableList<ApptGroupCount> apptsGroupCount;

    @FXML
    private TableView<Appointment> repApptTableView;
    @FXML
    private TableColumn<Appointment, String> repId;
    @FXML
    private TableColumn<Appointment, String> repTitle;
    @FXML
    private TableColumn<Appointment, String> repDescription;
    @FXML
    private TableColumn<Appointment, String> repLocation;
    @FXML
    private TableColumn<Appointment, String> repApptType;
    @FXML
    private TableColumn<Appointment, String> repStart;
    @FXML
    private TableColumn<Appointment, String> repEnd;
    @FXML
    private TableColumn<Appointment, String> repCustID;
    @FXML
    private ComboBox<ReportOption> reportSelectCB;
    @FXML
    private Label selectContactLbl;
    @FXML
    private ComboBox<Contact> reportContactCB;
    private ObservableList<Contact> contacts;

    @FXML
    private TableView<CustByDiv> custDivTableView;
    @FXML
    private TableColumn<CustByDiv,String> repDiv;
    @FXML
    private TableColumn<CustByDiv,String> repCustomerCount;


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
    private LocalDate dateRange;
    private Calendar weekRange;
    private LocalDate firstDay;
    private LocalDate lastDay;
    private ObservableList<Customer> allCustomers;
    /** Initialize main form by loading customer, appointment, and report views
     * <p></p>
     * Customer and Appointment tableviews use lambda functions to set the double click events on each of the rows*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.rs = resourceBundle;
        this.conn = LoginController.getConn();
        try {
            //Customer Table setup
            allCustomers = DBService.getAllCustomers(conn);
            customerTableView.setItems(allCustomers);
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
            allAppts = DBService.getAllAppointments(conn);
            appointmentTableView.setItems(allAppts);
            apptId.setCellValueFactory(new PropertyValueFactory<Appointment, String>("id"));
            title.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
            description.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));
            location.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));
            contact.setCellValueFactory(new PropertyValueFactory<Appointment, String>("contactName"));
            type.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));
            start.setCellValueFactory(new PropertyValueFactory<Appointment, String>("start"));
            end.setCellValueFactory(new PropertyValueFactory<Appointment, String>("end"));
            apptCustomerName.setCellValueFactory(new PropertyValueFactory<Appointment, String>("customerName"));

            //Appointment Double Click
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
            //Report Selector
            ObservableList<ReportOption> reportOption = FXCollections.observableArrayList();
            var apptByMonth = new ReportOption(rs.getString("report.ApptByMonth"),1);
            reportOption.add(apptByMonth);
            reportOption.add(new ReportOption(rs.getString("report.ApptByContact"),2));
            reportOption.add(new ReportOption(rs.getString("report.CustomerByDivision"),3));
            reportSelectCB.setItems(reportOption);
            reportSelectCB.setValue(apptByMonth);

            //Report Month Type Table setup
            apptsGroupCount = DBService.apptGroupCount(conn);
            reportTableView.setItems(apptsGroupCount);
            repMonth.setCellValueFactory(new PropertyValueFactory<ApptGroupCount, String>("Month"));
            repType.setCellValueFactory(new PropertyValueFactory<ApptGroupCount, String>("Type"));
            repCount.setCellValueFactory(new PropertyValueFactory<ApptGroupCount, String>("Count"));

            //Report Contact Appointment Table Setup
            repId.setCellValueFactory(new PropertyValueFactory<Appointment, String>("id"));
            repTitle.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
            repDescription.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));
            repLocation.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));
            repApptType.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));
            repStart.setCellValueFactory(new PropertyValueFactory<Appointment, String>("start"));
            repEnd.setCellValueFactory(new PropertyValueFactory<Appointment, String>("end"));
            repCustID.setCellValueFactory(new PropertyValueFactory<Appointment, String>("customerName"));
            contacts = DBService.getAllContacts(conn);
            reportContactCB.setItems(contacts);

            //Report Customer by Division Table setup
            custDivTableView.setItems(DBService.custDivCount(conn));
            repDiv.setCellValueFactory(new PropertyValueFactory<CustByDiv, String>("division"));
            repCustomerCount.setCellValueFactory(new PropertyValueFactory<CustByDiv, String>("count"));

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        displayBy = new ToggleGroup();
        monthToggle.setToggleGroup(displayBy);
        weekToggle.setToggleGroup(displayBy);
        displayBy.selectToggle(monthToggle);
        setCurrentMonth();
    }
    @FXML
    private void searchCustomers(ActionEvent event) throws SQLException {
        String searchStr = searchTxt.getText();
        if(searchStr.isEmpty()){
            customerTableView.setItems(allCustomers);
            return;
        }
        customerTableView.setItems(lookupCustomer(searchStr));
    }
    private ObservableList<Customer> lookupCustomer (String searchStr){
        ObservableList<Customer> filteredCustomers = FXCollections.observableArrayList();
        for(Customer cust: allCustomers){
            if(cust.getCustomerName().toLowerCase().contains(searchStr.toLowerCase())){
                filteredCustomers.add(cust);
            }
        }
        return filteredCustomers;
    }

    /** Load customer form with selected customer data populated */
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
    /** Load appointment form with selected appointment data populated */
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
    /** Loads the customer form to create a new customer */
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
    /** Loads the appointment form to create a new appointment */
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

    /** Sets the appointment tab to the active tab */
    @FXML
    public void setApptTab(){
        tabPane.getSelectionModel().select(1);
    }
    /** Sets the current month for the date range selector */
    @FXML
    private void setCurrentMonth(){
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault(),rs.getLocale());
        calendar.set(Calendar.DAY_OF_MONTH,1);
        dateRange = LocalDate.ofInstant(calendar.toInstant(), ZoneId.systemDefault());
        setMonthFilter();
    }
    /** Filters appointment list by the selected month */
    private void setMonthFilter(){
        var newMonth = dateRange.getMonth();
        dateSelectionText.setText(newMonth.getDisplayName(TextStyle.FULL,rs.getLocale()) + " " + dateRange.getYear());
        ObservableList<Appointment> filteredAppts = FXCollections.observableArrayList();
        for (Appointment appt:allAppts) {
            var startDate = appt.getStartZDT().toLocalDate();
            var sameMonth = startDate.getMonth().getValue() == dateRange.getMonth().getValue();
            var sameYear = startDate.getYear() == dateRange.getYear();
            if(sameMonth && sameYear){
                filteredAppts.add(appt);
            }
        }
        appointmentTableView.setItems(filteredAppts);
    }
    /** Sets the current week for the date range selector */
    @FXML
    private void setCurrentWeek(){
        var today = LocalDate.now();
        WeekFields weekFields = WeekFields.of(rs.getLocale());
        weekRange = Calendar.getInstance(TimeZone.getDefault(),rs.getLocale());
        setWeekFilter();

    }
    /** Filters appointment list by the selected week */
    private void setWeekFilter(){
        var calendar = weekRange;
        calendar.set(Calendar.DAY_OF_WEEK,1);
        var firstDate = calendar.getTime();
        calendar.set(Calendar.DAY_OF_WEEK,7);
        var LastDate = calendar.getTime();
        firstDay = LocalDate.ofInstant(firstDate.toInstant(), ZoneId.systemDefault());
        lastDay = LocalDate.ofInstant(LastDate.toInstant(),ZoneId.systemDefault());
        var monthDayYear = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        dateSelectionText.setText(firstDay.format(monthDayYear) + " - " + lastDay.format(monthDayYear));

        ObservableList<Appointment> filteredAppts = FXCollections.observableArrayList();
        for (Appointment appt:allAppts) {
            var startDate = appt.getStartZDT().toLocalDate();
            if(startDate.compareTo(firstDay) > -1 && startDate.compareTo(lastDay) < 1){
                filteredAppts.add(appt);
            }
        }
        appointmentTableView.setItems(filteredAppts);
    }
    /** Sets the date range to the previous week or month */
    @FXML
    private void prevDate(ActionEvent event){
        var selectedToggle = (ToggleButton)displayBy.getSelectedToggle();
        if(selectedToggle.getText().equals("Month")){
            dateRange = dateRange.minusMonths(1);
            setMonthFilter();
        }
        else{
            weekRange.add(Calendar.WEEK_OF_YEAR,-1);
            setWeekFilter();
        }
    }
    /** Sets the date range to the previous week or month */
    @FXML
    private void nextDate(ActionEvent event){
        var selectedToggle = (ToggleButton)displayBy.getSelectedToggle();
        if(selectedToggle.getText().equals("Month")){
            dateRange = dateRange.plusMonths(1);
            setMonthFilter();
        }
        else{
            weekRange.add(Calendar.WEEK_OF_YEAR,1);
            setWeekFilter();
        }
    }
    /** Alerts user of upcoming appointments upon login
     * <p></p>
     * Lambda expression for each appointment returned. Builds the appointment notification string*/
    public void appointmentAlert() throws SQLException {
        var appts = DBService.apptWithinFifteen(conn);
        var alertString = new StringBuilder();
        if(appts.size() > 0) {
            appts.forEach(appt -> {
                var customer = appt.getCustomerName();
                var apptId = Integer.toString(appt.getId());
                var apptDateTime = appt.getStart();
                alertString.append(
                        rs.getString("appt.heading") + ":" + apptId + " "
                                + rs.getString("appt.With") + " " + customer + " "
                                + rs.getString("appt.IsAt") + " " + apptDateTime + "\n"
                );
            });
            var alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(alertString.toString());
            alert.getDialogPane().setMinWidth(500);
            alert.setResizable(true);
            alert.showAndWait();
        }
        else{
            var alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(rs.getString("appt.NoAppt"));
            alert.getDialogPane().setMinWidth(500);
            alert.setResizable(true);
            alert.showAndWait();
        }

    }
    /** Toggles visibility of report views based on selected report */
    @FXML
    private void reportSelected(){
        var selectedReport = reportSelectCB.getValue();
        switch (selectedReport.getValue()){
            case 1 :
                reportTableView.setVisible(true);
                reportContactCB.setVisible(false);
                repApptTableView.setVisible(false);
                selectContactLbl.setVisible(false);
                custDivTableView.setVisible(false);
                break;
            case 2 :
                reportTableView.setVisible(false);
                reportContactCB.setVisible(true);
                repApptTableView.setVisible(true);
                selectContactLbl.setVisible(true);
                custDivTableView.setVisible(false);
                break;
            case 3 :
                reportTableView.setVisible(false);
                reportContactCB.setVisible(false);
                repApptTableView.setVisible(false);
                selectContactLbl.setVisible(false);
                custDivTableView.setVisible(true);
        }
    }
    /** Filters the appointment report based on the selected contact */
    @FXML
    private void contactSelected(){
        var selectedContact = reportContactCB.getValue();
        ObservableList<Appointment> contactAppts = FXCollections.observableArrayList();
        for(Appointment appt: allAppts){
            if(appt.getContactId() == selectedContact.getId()) {
                contactAppts.add(appt);
            }
        }
        repApptTableView.setItems(contactAppts);
    }
}
