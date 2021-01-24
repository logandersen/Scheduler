package Scheduler.model;

public class LoginActivity {
    private String Username;
    private String Result;
    private String Timestamp;

    public LoginActivity(String logString){
        var logValues =logString.split(", ");
        Username = logValues[0].substring(10);
        Result = logValues[1].substring(13);
        Timestamp = logValues[2].substring(11);
    };

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }
}
