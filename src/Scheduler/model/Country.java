package Scheduler.model;

import java.sql.Date;
import java.sql.Timestamp;
/** Class model for country object */
public class Country {
    private int id;
    private String name;
    private Date createdDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;
    /** Constructor for country class */
    public void Country() {
    }
    /** To string override to return name instead */
    @Override
    public String toString(){
        return(name);
    }
    /** Get id int */
    public int getId() {
        return id;
    }
    /** Set id int */
    public void setId(int id) {
        this.id = id;
    }
    /** Get name string */
    public String getName() {
        return name;
    }
    /** Set name string */
    public void setName(String name) {
        this.name = name;
    }
    /** Get created date Date*/
    public Date getCreatedDate() {
        return createdDate;
    }
    /** Set created date Date*/
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    /** Get created by string */
    public String getCreatedBy() {
        return createdBy;
    }
    /** Set created by string */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    /** Get last update Timestamp */
    public Timestamp getLastUpdate() {
        return lastUpdate;
    }
    /** Set last update string */
    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    /** Get last updated by string */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }
    /** Set last updated by string*/
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
}