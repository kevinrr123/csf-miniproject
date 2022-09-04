package vttp2022.com.ssfminiproject.model;

import java.io.Serializable;
import java.util.Random;


import org.springframework.stereotype.Component;

@Component
public class User implements Serializable {
    
    private String username;
    private String id;

    private static final long serialVersionUID = 1L;

    public User() {
        this.id = generateId(8);
    }

    public User(String username) {
        this.id = generateId(8);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private synchronized String generateId(int numchars) {
        Random r = new Random();
        StringBuilder strBuilder = new StringBuilder();
        while (strBuilder.length() < numchars) {
            strBuilder.append(Integer.toHexString(r.nextInt()));
        }
        return strBuilder.toString().substring(0, numchars);
    }

}
