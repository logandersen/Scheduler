package Scheduler.model;

import java.sql.Date;
import java.sql.Timestamp;
/** Class model for division object */
public class Division {
    public int id;
    public String name;
    public Date createdDate;
    public String createdBy;
    public Timestamp lastUpdate;
    public String lastUpdatedBy;
    public int countryID;

    /** Constructor for division class */
    public void Division() {
    }
    /** To string override to return the name instead */
    @Override
    public String toString(){
        return(name);
    }
}