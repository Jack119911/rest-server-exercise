package ninja.parkverbot.restserver.user;

public class User {

    public static final int NO_ID = -1;

    private int id = NO_ID;
    private String login;
    private String password;
    private String fname;
    private String lname;
    private String email;

    public User(String login, String password, String fname, String lname, String email) {
        this.login = login;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
