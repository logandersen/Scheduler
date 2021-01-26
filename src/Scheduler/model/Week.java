package Scheduler.model;
/** Class model for week object */
public class Week {
    private String rangeText;
    private int weekNumber;
    /** Constructor for week class */
    public Week(String rangeText, int weekNumber){
        setWeekNumber(weekNumber);
        setRangeText(rangeText);
    }
    /** Get range text */
    public String getRangeText() {
        return rangeText;
    }
    /** Set range text string */
    public void setRangeText(String rangeText) {
        this.rangeText = rangeText;
    }
    /** Get week number int */
    public int getWeekNumber() {
        return weekNumber;
    }
    /** Set week number int */
    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }
}
