package alphalaser.cfc.laser_project.dto;

public class LoginRequest {

    private String email;
    private String password;

    // DEFAULT CONSTRUCTOR
    public LoginRequest() {
    }

    // PARAMETERIZED CONSTRUCTOR
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // GETTER AND SETTER FOR EMAIL
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // GETTER AND SETTER FOR PASSWORD
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}