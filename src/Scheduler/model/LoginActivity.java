package Scheduler.model;
/** Class model for login activity object */
public class LoginActivity {
    private String Username;
    private String Result;
    private String Timestamp;
    /** Constructor for login activity. Converts log string to LoginActivity object */
    public LoginActivity(String logString){
        var logValues =logString.split(", ");
        Username = logValues[0].substring(10);
        Result = logValues[1].substring(13);
        Timestamp = logValues[2].substring(11);
    };
    /** Get username string */
    public String getUsername() {
        return Username;
    }
    /** Set username string */
    public void setUsername(String username) {
        Username = username;
    }
    /** Get result string */
    public String getResult() {
        return Result;
    }
    /** Set result string */
    public void setResult(String result) {
        Result = result;
    }
    /** Get timestamp string */
    public String getTimestamp() {
        return Timestamp;
    }
    /** Set Timestamp string */
    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }
}
