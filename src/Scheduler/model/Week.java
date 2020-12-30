package Scheduler.model;

public class Week {
    private String rangeText;
    private int weekNumber;
    public Week(String rangeText, int weekNumber){
        setWeekNumber(weekNumber);
        setRangeText(rangeText);
    }

    public String getRangeText() {
        return rangeText;
    }

    public void setRangeText(String rangeText) {
        this.rangeText = rangeText;
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }
}
