package Scheduler.model;
/** Class model for appointment group count object */
public class ApptGroupCount {
    private String Month;
    private String Type;
    private int Count;
    /** Get month string */
    public String getMonth() {
        return Month;
    }
    /** Set month string */
    public void setMonth(String month) {
        Month = month;
    }
    /** Get type string */
    public String getType() {
        return Type;
    }
    /** Set type string */
    public void setType(String type) {
        Type = type;
    }
    /** Get count int */
    public int getCount() {
        return Count;
    }
    /** Set count int */
    public void setCount(int count) {
        Count = count;
    }
}
