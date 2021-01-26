package Scheduler.model;
/** Class model for contact object */
public class Contact {
    private int id;
    private String name;
    private String email;
    /** Contact class constructor */
    public Contact() {
    }
    /** Get id int */
    public int getId() {
        return id;
    }
    /** Set id int */
    public void setId(int id) {
        this.id = id;
    }
    /** Get name string */
    public String getName() {
        return name;
    }
    /** Set name string*/
    public void setName(String name) {
        this.name = name;
    }
    /** Get email string */
    public String getEmail() {
        return email;
    }
    /** Set email string*/
    public void setEmail(String email) {
        this.email = email;
    }
    /** To string override to return the name instead */
    @Override
    public String toString(){
        return(name);
    }
}
