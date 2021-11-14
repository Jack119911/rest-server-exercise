package ninja.parkverbot.restserver.user;

import org.springframework.beans.factory.annotation.Autowired;

public class User {

    public static final int NO_ID = -1;

    private int id = NO_ID;
    private String login;
    private String passwordHash;
    private String fname;
    private String lname;
    private String email;

    public User(String login, String passwordHash, String fname, String lname, String email) {
        this.login = login;
        this.passwordHash = passwordHash;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
