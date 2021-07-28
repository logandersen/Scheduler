package Scheduler.controller;

import Scheduler.DAO.DBService;
import Scheduler.model.*;
import javafx.collections.FXCollections;
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
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

/** Controller used for the Appointment FXML new and edit form*/
public class AppointmentController implements Initializable{
    @FXML
    private TextField Id;
    @FXML
    private TextField Title;
    @FXML
    private TextField Description;
    @FXML
    private TextField Location;
    @FXML
    private Label LocationLbl;
    @FXML
    private TextField PhoneNumber;
    @FXML
    private Label PhoneNumberLbl;
    @FXML
    private ComboBox<String> Type;
    @FXML
    private Label CreatedOn;
    @FXML
    private Label CreatedBy;
    @FXML
    private Label LastUpdated;
    @FXML
    private Label LastUpdatedBy;
    @FXML
    private ComboBox<Contact> ContactCB;
    @FXML
    private ComboBox<Customer> CustomerCB;
    @FXML
    private ComboBox<User> UserCB;
    @FXML
    private DatePicker StartDate;
    @FXML
    private ComboBox<TimeOption> StartTimeCB;
    @FXML
    private DatePicker EndDate;
    @FXML
    private ComboBox<TimeOption> EndTimeCB;


    private Connection conn;
    private ResourceBundle rs;
    private String pageType;
    private Appointment appointment;
    private ObservableList<Contact> contacts;
    private ObservableList<Customer> customers;
    private ObservableList<User> users;
    private ObservableList<TimeOption> timeOptions;


    /** Initialized the appointment controller by populating the contact user and customer combo boxes*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        conn = LoginController.getConn();
        rs = resourceBundle;
        timeOptions = getTimeOptions();
        StartTimeCB.setItems(timeOptions);
        EndTimeCB.setItems(timeOptions);

        try {
            contacts = DBService.getAllContacts(conn);
            ContactCB.setItems(contacts);
            users = DBService.getAllUsers(conn);
            UserCB.setItems(users);
            customers = DBService.getAllCustomers(conn);
            CustomerCB.setItems(customers);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    /** Sets the page type form as edit or new */
    public void setPageType(String pageType){
        this.pageType = pageType;
    }
    /** Populates all appointment fields from selected appointment */
    public void setSelectedAppointment(Appointment selectedAppointment) {

        appointment = selectedAppointment;
        Id.setText(String.valueOf(selectedAppointment.getId()));
        Title.setText(selectedAppointment.getTitle());
        Description.setText(String.valueOf(selectedAppointment.getDescription()));
        if(appointment instanceof OnSiteMeeting){
            Location.setText(String.valueOf(((OnSiteMeeting)selectedAppointment).getLocation()));
        }
        if(appointment instanceof PhoneCall){
            PhoneNumber.setText(String.valueOf(((PhoneCall)selectedAppointment).getPhoneNumber()));
        }
        Type.getSelectionModel().select(String.valueOf(selectedAppointment.getType()));
        typeSelected();
        CreatedOn.setText(selectedAppointment.getCreatedDate());
        CreatedBy.setText(String.valueOf(selectedAppointment.getCreatedBy()));
        LastUpdated.setText(selectedAppointment.getLastUpdate());
        LastUpdatedBy.setText(String.valueOf(selectedAppointment.getLastUpdatedBy()));
        ContactCB.setValue(selectContact(selectedAppointment.getContactId()));
        UserCB.setValue(selectUser(selectedAppointment.getUserId()));
        CustomerCB.setValue(selectCustomer(selectedAppointment.getCustomerId()));

        var timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
        StartDate.setValue(selectedAppointment.getStartZDT().toLocalDate());
        StartTimeCB.setValue(
                selectTimeOption(
                        selectedAppointment.getStartZDT().format(timeFormat)
                )
        );
        EndDate.setValue(selectedAppointment.getEndZDT().toLocalDate());
        EndTimeCB.setValue(
                selectTimeOption(
                        selectedAppointment.getEndZDT().format(timeFormat)
                )
        );
    }

    /** Takes the time string and selects the time combo box option */
    private TimeOption selectTimeOption(String timeValue){
        for(TimeOption timeOption: timeOptions){
            var optionValue = timeOption.getTimeValue();
            if( optionValue.equals(timeValue)){
                return timeOption;
            }
        }
        return  null;
    }

    /** Takes the contactId and selects the contact combo box option*/
    private Contact selectContact(int contactId){
        for(Contact contact: contacts){
            if(contact.getId() == contactId){
                return contact;
            }
        }
        return  null;
    }
    /** Takes the  userId and selects the user combo box option*/
    private User selectUser(int userId){
        for(User user: users){
            if(user.getId() == userId){
                return user;
            }
        }
        return  null;
    }
    /** Takes the customerId and selects the user combo box option */
    private Customer selectCustomer(int customerId){
        for(Customer customer: customers){
            if(customer.getId() == customerId){
                return customer;
            }
        }
        return null;
    }
    @FXML
    private void typeSelected(){
        String type = Type.getSelectionModel().getSelectedItem();
        if(type.equals("Phone Call")) {
            PhoneNumber.setVisible(true);
            PhoneNumberLbl.setVisible(true);
            Location.setVisible(false);
            LocationLbl.setVisible(false);
        }
        else{
            PhoneNumber.setVisible(false);
            PhoneNumberLbl.setVisible(false);
            Location.setVisible(true);
            LocationLbl.setVisible(true);
        }

    }

    /** Action for the cancel button that calls the function to take the user back to the appointment list */
    @FXML
    private void cancel(ActionEvent event) throws IOException {
        loadAppointmentList(event);
    }
    /** Deletes the user record after confirming deletion */
    @FXML
    private void delete(ActionEvent event) throws Exception{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(rs.getString("appt.DeleteText"));
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() != ButtonType.OK){return;}

        var deleted = DBService.deleteAppointment(conn,appointment);
        if(deleted){
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(rs.getString("appt.Deleted") + ". ID: " + appointment.getId() + " " + rs.getString("apptList.type") + ": " + appointment.getType());
            alert.showAndWait();
            loadAppointmentList(event);
        }
        else{
            Alert failAlert = new Alert(Alert.AlertType.ERROR);
            failAlert.setContentText(rs.getString("appt.DeleteFailed"));
            failAlert.show();
        }
    }

    /** Action called by the save button to save or update the appointment record */
    @FXML
    private void upsert(ActionEvent event) throws  Exception{
        if(pageType == "Edit"){
            update(event);
        }
        else {
            saveNew(event);
        }
    }
    /** Takes user input fields and updates the appointment record */
    private void update(ActionEvent event) throws Exception {
        var userName = DBService.getUserName();
        Appointment appt;
        var type = Type.getSelectionModel().getSelectedItem();
        if(type.equals("On Site")){
            appt = new OnSiteMeeting();
            ((OnSiteMeeting)appt).setLocation(Location.getText());
        }
        else{
            appt = new PhoneCall();
            ((PhoneCall) appt).setPhoneNumber(PhoneNumber.getText());
        }
        appt.setId(appointment.getId());
        appt.setTitle(Title.getText());
        appt.setDescription(Description.getText());

        appt.setType(type);
        var timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
        var startTime = LocalTime.parse(StartTimeCB.getValue().getTimeValue(),timeFormat);
        appt.setStart(ZonedDateTime.of(StartDate.getValue(),startTime, ZoneId.systemDefault()));
        var endTime = LocalTime.parse(EndTimeCB.getValue().getTimeValue(),timeFormat);
        appt.setEnd(ZonedDateTime.of(EndDate.getValue(),endTime, ZoneId.systemDefault()));
        appt.setLastUpdatedBy(userName);
        appt.setCustomerId(CustomerCB.getValue().getId());
        appt.setUserId(UserCB.getValue().getId());
        appt.setContactId(ContactCB.getValue().getId());
        if(!validateAppointmentTimes(appt.getStartZDT(),appt.getEndZDT(),appt.getCustomerId(),appt.getId())){
            return;
        }
        DBService.updateAppointment(conn,appt);
        loadAppointmentList(event);
    }
    /** Takes user input and saves a new appointment record */
    private void saveNew(ActionEvent event) throws Exception {
        var userName = DBService.getUserName();
        Appointment appt;
        var type = Type.getSelectionModel().getSelectedItem();
        if(type.equals("On Site")){
            appt = new OnSiteMeeting();
            ((OnSiteMeeting)appt).setLocation(Location.getText());
        }
        else{
            appt = new PhoneCall();
            ((PhoneCall) appt).setPhoneNumber(PhoneNumber.getText());
        }
        appt.setTitle(Title.getText());
        appt.setDescription(Description.getText());
        appt.setType(type);
        var timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
        var startTime = LocalTime.parse(StartTimeCB.getValue().getTimeValue(),timeFormat);
        appt.setStart(ZonedDateTime.of(StartDate.getValue(),startTime, ZoneId.systemDefault()));
        var endTime = LocalTime.parse(EndTimeCB.getValue().getTimeValue(),timeFormat);
        appt.setEnd(ZonedDateTime.of(EndDate.getValue(),endTime, ZoneId.systemDefault()));
        appt.setCreatedBy(userName);
        appt.setLastUpdatedBy(userName);
        appt.setCustomerId(CustomerCB.getValue().getId());
        appt.setUserId(UserCB.getValue().getId());
        appt.setContactId(ContactCB.getValue().getId());
        if(!validateAppointmentTimes(appt.getStartZDT(),appt.getEndZDT(),appt.getCustomerId(), appt.getId())){
            return;
        }
        DBService.insertAppointment(conn,appt);
        loadAppointmentList(event);
    }

    /** Checks appointments for business hours and overlapping */
    private boolean validateAppointmentTimes(ZonedDateTime start, ZonedDateTime end, Integer custID, Integer apptId) throws SQLException {
        var startEst = start.withZoneSameInstant(ZoneId.of("America/New_York"));
        var startTimeEst = startEst.toLocalTime();

        var endEst = end.withZoneSameInstant(ZoneId.of("America/New_York"));
        var endTimeEst = endEst.toLocalTime();

        var busStart = LocalTime.of(8,0,0);
        var busEnd = LocalTime.of(22,0,0);

        if(startTimeEst.isBefore(busStart) || startTimeEst.isAfter(busEnd) || endTimeEst.isBefore(busStart) || endTimeEst.isAfter(busEnd)) {// outside of business hours
            Alert failAlert = new Alert(Alert.AlertType.ERROR);
            failAlert.setContentText(rs.getString("appt.NotBusinessHours"));
            failAlert.show();
            return false;
        }
        //Check for appointments same customer overlapping
        var startInstant = start.toInstant();
        var startLocal = LocalDateTime.ofInstant(startInstant, ZoneOffset.UTC);
        var sqlDateTimeString = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        var startString = startLocal.format(sqlDateTimeString);
        var endInstant = end.toInstant();
        var endLocal = LocalDateTime.ofInstant(endInstant, ZoneOffset.UTC);
        var endString = endLocal.format(sqlDateTimeString);
        if(DBService.apptSameTime(conn,startString,endString,apptId)){
            Alert failAlert = new Alert(Alert.AlertType.ERROR);
            failAlert.setContentText(rs.getString("appt.Overlapping"));
            failAlert.show();
            return false;
        }
        return true;
    }
    /** Switches from the appointment form to the appointment list */
    private void loadAppointmentList(ActionEvent event) throws IOException {
        FXMLLoader custListLoader = new FXMLLoader();
        custListLoader.setLocation(getClass().getResource("../view/CustomerApptList.fxml"));
        custListLoader.setResources(rs);
        Parent custListParent = custListLoader.load();
        CustomerApptListController controller = custListLoader.getController();
        controller.setApptTab();
        Scene partScene = new Scene(custListParent);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(partScene);
        appStage.show();
    }

    /** Populates Time combo box with half hour increments */
    private ObservableList<TimeOption> getTimeOptions(){
        ObservableList<TimeOption> timeOptions = FXCollections.observableArrayList();
        timeOptions.addAll(
            new TimeOption("12:00 AM","00:00:00"),
            new TimeOption("12:30 AM","00:30:00"),
            new TimeOption("1:00 AM","01:00:00"),
            new TimeOption("1:30 AM","01:30:00"),
            new TimeOption("2:00 AM","02:00:00"),
            new TimeOption("2:30 AM","02:30:00"),
            new TimeOption("3:00 AM","03:00:00"),
            new TimeOption("3:30 AM","03:30:00"),
            new TimeOption("4:00 AM","04:00:00"),
            new TimeOption("4:30 AM","04:30:00"),
            new TimeOption("5:00 AM","05:00:00"),
            new TimeOption("5:30 AM","05:30:00"),
            new TimeOption("6:00 AM","06:00:00"),
            new TimeOption("6:30 AM","06:30:00"),
            new TimeOption("7:00 AM","07:00:00"),
            new TimeOption("7:30 AM","07:30:00"),
            new TimeOption("8:00 AM","08:00:00"),
            new TimeOption("8:30 AM","08:30:00"),
            new TimeOption("9:00 AM","09:00:00"),
            new TimeOption("9:30 AM","09:30:00"),
            new TimeOption("10:00 AM","10:00:00"),
            new TimeOption("10:30 AM","10:30:00"),
            new TimeOption("11:00 AM","11:00:00"),
            new TimeOption("11:30 AM","11:30:00"),
            new TimeOption("12:00 PM","12:00:00"),
            new TimeOption("12:30 PM","12:30:00"),
            new TimeOption("1:00 PM","13:00:00"),
            new TimeOption("1:30 PM","13:30:00"),
            new TimeOption("2:00 PM","14:00:00"),
            new TimeOption("2:30 PM","14:30:00"),
            new TimeOption("3:00 PM","15:00:00"),
            new TimeOption("3:30 PM","15:30:00"),
            new TimeOption("4:00 PM","16:00:00"),
            new TimeOption("4:30 PM","16:30:00"),
            new TimeOption("5:00 PM","17:00:00"),
            new TimeOption("5:30 PM","17:30:00"),
            new TimeOption("6:00 PM","18:00:00"),
            new TimeOption("6:30 PM","18:30:00"),
            new TimeOption("7:00 PM","19:00:00"),
            new TimeOption("7:30 PM","19:30:00"),
            new TimeOption("8:00 PM","20:00:00"),
            new TimeOption("8:30 PM","20:30:00"),
            new TimeOption("9:00 PM","21:00:00"),
            new TimeOption("9:30 PM","21:30:00"),
            new TimeOption("10:00 PM","22:00:00"),
            new TimeOption("10:30 PM","22:30:00"),
            new TimeOption("11:00 PM","23:00:00"),
            new TimeOption("11:30 PM","23:30:00")
        );
        return timeOptions;
    }
}
