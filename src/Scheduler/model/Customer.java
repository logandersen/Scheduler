package Scheduler.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Customer {
    public int id;
    public String customerName;
    public String address;
    public String postalCode;
    public String phone;
    public Date createdDate;
    public String createdBy;
    public Timestamp lastUpdate;
    public String lastUpdatedBy;
    public int divisionID;
    public String divisionName;
    public String countryName;

    public void Customer() {
    }

    //getters
    public int getId(){return id;};
    public String getCustomerName(){return customerName;}
    public String getAddress(){return address;}
    public String getPostalCode(){return postalCode;}
    public String getPhone(){return phone;}
    public Date getCreatedDate(){return createdDate;}
    public String getCreatedBy(){return createdBy;}
    public Timestamp getLastUpdate(){return lastUpdate;}
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

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
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
}