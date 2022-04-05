package dto;

import java.io.Serializable;

public class DTOArbitru implements Serializable {
    String username;
    String password;

    public DTOArbitru(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
