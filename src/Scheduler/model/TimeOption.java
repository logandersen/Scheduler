package Scheduler.model;
/** Class model for time option object */
public class TimeOption {
    private String displayText;
    private String timeValue;
    /** Constructor for TimeOption class*/
    public TimeOption(String displayText, String timeValue){
        setDisplayText(displayText);
        setTimeValue(timeValue);
    }
    /** Get display text string */
    public String getDisplayText() {
        return displayText;
    }
    /** Set display text string */
    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }
    /** Get time value string*/
    public String getTimeValue() {
        return timeValue;
    }
    /** Set timevalue string */
    public void setTimeValue(String timeValue) {
        this.timeValue = timeValue;
    }
    /** To string override to return time display text instead */
    @Override
    public String toString(){
        return getDisplayText();
    }
}
