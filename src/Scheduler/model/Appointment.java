package Scheduler.model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/** Class model for appointment object */
public class Appointment {
    private int id;
    private String title;
    private String description;
    private String location;
    private String type;
    private ZonedDateTime start;
    private ZonedDateTime end;
    private ZonedDateTime createdDate;
    private String createdBy;
    private ZonedDateTime lastUpdate;
    private String lastUpdatedBy;
    private int customerId;
    private String customerName;
    private int userId;
    private String userName;
    private int contactId;
    private String contactName;

    /** Appointment class constructor */
    public void Appointment(){
    }
    /** Get Id int */
    public int getId() {
        return id;
    }
    /** Get title string */
    public String getTitle() {
        return title;
    }
    /** Get description string*/
    public String getDescription() {
        return description;
    }
    /** Get location string */
    public String getLocation() {
        return location;
    }
    /** Get type string */
    public String getType() {
        return type;
    }
    /** Get Start ZonedDateTime */
    public ZonedDateTime getStartZDT() {
        return start;
    }
    /** Get Start string */
    public String getStart(){
        var fullFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a");
        return start.format(fullFormat);
    }
    /** Get End ZonedDateTime */
    public ZonedDateTime getEndZDT() {
        return end;
    }
    /** Get end string */
    public String getEnd(){
        var fullFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a");
        return end.format(fullFormat);
    }
    /** Get created date formatted string*/
    public String getCreatedDate() {
        var fullFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a");
        return createdDate.format(fullFormat);
    }
    /** Get created by string */
    public String getCreatedBy() {
        return createdBy;
    }
    /** Get last update ZonedDateTime */
    public ZonedDateTime getLastUpdateZDT() {
        return lastUpdate;
    }
    /** Get last update formatted string */
    public String getLastUpdate() {
        var fullFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a");
        return lastUpdate.format(fullFormat);
    }
    /** Get last updated by string */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }
    /** Get customer id int */
    public int getCustomerId() {
        return customerId;
    }
    /** Get user id int*/
    public int getUserId() {
        return userId;
    }
    /** Get contact id int*/
    public int getContactId() {
        return contactId;
    }
    /** Get customer name string*/
    public String getCustomerName() {
        return customerName;
    }
    /** Get user name string*/
    public String getUserName() {
        return userName;
    }
    /** Get contact name string */
    public String getContactName() {
        return contactName;
    }
    /** Set id int */
    public void setId(int id) {
        this.id = id;
    }
    /** Set title string */
    public void setTitle(String title) {
        this.title = title;
    }
    /** Set description string */
    public void setDescription(String description) {
        this.description = description;
    }
    /** Set location string */
    public void setLocation(String location) {
        this.location = location;
    }
    /** Set type string */
    public void setType(String type) {
        this.type = type;
    }
    /** Set start ZonedDateTime*/
    public void setStart(ZonedDateTime start) {
        this.start = start;
    }
    /** Set end ZonedDateTime*/
    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }
    /** Set created date ZonedDateTime */
    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }
    /** Set created by string */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    /** Set last update ZonedDateTime */
    public void setLastUpdate(ZonedDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    /** Set last updated by string */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
    /** Set customer id int */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    /** Set user id int */
    public void setUserId(int userId) {
        this.userId = userId;
    }
    /** Set contact id int */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }
    /** Set customer name string */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    /** Set user name string */
    public void setUserName(String userName) {
        this.userName = userName;
    }
    /** Set contact name string */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
}
