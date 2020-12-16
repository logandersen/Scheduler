package Scheduler.model;

public class TimeOption {
    private String displayText;
    private String timeValue;

    public TimeOption(String displayText, String timeValue){
        setDisplayText(displayText);
        setTimeValue(timeValue);
    }
    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    public String getTimeValue() {
        return timeValue;
    }

    public void setTimeValue(String timeValue) {
        this.timeValue = timeValue;
    }
    @Override
    public String toString(){
        return getDisplayText();
    }
}
