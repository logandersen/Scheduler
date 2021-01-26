package Scheduler.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
/** Class model for customer object */
public class Customer {
    private int id;
    private String customerName;
    private String address;
    private String postalCode;
    private String phone;
    private ZonedDateTime createdDate;
    private String createdBy;
    private ZonedDateTime lastUpdate;
    private String lastUpdatedBy;
    private int divisionID;
    private String divisionName;
    private String countryName;
    /** Constructor for customer class */
    public void Customer() {
    }

    //getters
    /** Get id int */
    public int getId(){return id;};
    /** Get customer name string */
    public String getCustomerName(){return customerName;}
    /** Get address string */
    public String getAddress(){return address;}
    /** Get postal code string */
    public String getPostalCode(){return postalCode;}
    /** Get phone string */
    public String getPhone(){return phone;}
    /** Get created date string*/
    public String getCreatedDate() {
        var fullFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a");
        return createdDate.format(fullFormat);
    }
    /** Get created by string */
    public String getCreatedBy(){return createdBy;}
    /** Get last update formatted string */
    public String getLastUpdate() {
        var fullFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a");
        return lastUpdate.format(fullFormat);
    }
    /** Get last updated by string */
    public String getLastUpdatedBy(){return lastUpdatedBy;}
    /** Get division id int */
    public int getDivisionID(){return divisionID;}
    /** Get division name string */
    public String getDivisionName(){return divisionName;}
    /** Get country name string */
    public String getCountryName(){return countryName;}

    //setters
    /** Set set id int*/
    public void setId(int id) {
        this.id = id;
    }
    /** Set customer name string */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    /** Set address string */
    public void setAddress(String address) {
        this.address = address;
    }
    /** Set postal code string */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    /** Set phone string */
    public void setPhone(String phone) {
        this.phone = phone;
    }
    /** Set created date ZonedDateTime */
    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }
    /** Set created by string */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    /** Set last update ZonedDateTime*/
    public void setLastUpdate(ZonedDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    /** Set last updated by string */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
    /** Set division id int */
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }
    /** Set division name string */
    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }
    /** Set country name string */
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
    /** To string override to return the customer name instead */
    @Override
    public String toString(){
        return(customerName);
    }
}