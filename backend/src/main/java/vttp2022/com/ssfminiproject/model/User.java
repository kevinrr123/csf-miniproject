package vttp2022.com.ssfminiproject.model;

import java.io.Serializable;

public class User implements Serializable {
    
    private String username;
    private String password;
    private String email;

    private static final long serialVersionUID = 1L;

    public User(){}

    public User(String username, String password){
        this.setUsername(username);
        this.setPassword(password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
