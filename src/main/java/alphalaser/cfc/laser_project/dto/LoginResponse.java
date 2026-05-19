package alphalaser.cfc.laser_project.dto;

public class LoginResponse {

    private String token;
    private String role;
    private Long id;

    // DEFAULT CONSTRUCTOR

    public LoginResponse() {
    }

    // PARAMETERIZED CONSTRUCTOR

    public LoginResponse(

            String token,
            String role,
            Long id) {

        this.token = token;
        this.role = role;
        this.id = id;
    }

    // TOKEN

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    // ROLE

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // ID

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}