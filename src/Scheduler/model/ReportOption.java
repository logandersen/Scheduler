package Scheduler.model;
/** Class model for report option object */
public class ReportOption {
    private String name;
    private int value;
    /** Constructor for report option class */
    public ReportOption(String name, int value){
        this.name = name;
        this.value = value;
    };
    /** Get name string*/
    public String getName() {
        return name;
    }
    /** Set name string */
    public void setName(String name) {
        this.name = name;
    }
    /** Get value int */
    public int getValue() {
        return value;
    }
    /** Set value int*/
    public void setValue(int value) {
        this.value = value;
    }
    /** To string override to return the name instead */
    @Override
    public String toString(){ return name;}
}
