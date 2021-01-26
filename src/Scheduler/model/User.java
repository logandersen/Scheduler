package Scheduler.model;
/** Class model for user object */
public class User {
    private int id;
    private String username;
    private String password;

    /** Constructor for user class */
    public void User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    //getters
    /** Get id int */
    public int getId() {
        return id;
    }
    /** Get username string */
    public String getUsername() {
        return username;
    }
    /** Get password string */
    public String getPassword() {
        return password;
    }

    //setters
    /** Set id int */
    public void setId(int id){
        this.id = id;
    }
    /** Set username string */
    public void setUsername(String username){
        this.username = username;
    }
    /** Set password string */
    public void setPassword(String password){
        this.password = password;
    }
    /** To string override to return the username instead */
    @Override
    public String toString(){
        return(username);
    }
}