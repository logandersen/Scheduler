package Scheduler.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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

    public void Customer() {
    }

    //getters
    public int getId(){return id;};
    public String getCustomerName(){return customerName;}
    public String getAddress(){return address;}
    public String getPostalCode(){return postalCode;}
    public String getPhone(){return phone;}
    public String getCreatedDate() {
        var fullFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a");
        return createdDate.format(fullFormat);
    }
    public String getCreatedBy(){return createdBy;}
    public String getLastUpdate() {
        var fullFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a");
        return lastUpdate.format(fullFormat);
    }
    public String getLastUpdatedBy(){return lastUpdatedBy;}
    public int getDivisionID(){return divisionID;}
    public String getDivisionName(){return divisionName;}
    public String getCountryName(){return countryName;}

    //setters
    public void setId(int id) {
        this.id = id;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setLastUpdate(ZonedDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    @Override
    public String toString(){
        return(customerName);
    }
}