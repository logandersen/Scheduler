package Scheduler.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Country {
    public int id;
    public String name;
    public Date createdDate;
    public String createdBy;
    public Timestamp lastUpdate;
    public String lastUpdatedBy;
    public int countryID;

    public void Country() {
    }
}