package hu.bme.aut.shed.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User implements Comparable {
    String UserID;
    String username;
    String password;

    public User(String userID, String username, String password) {
        UserID = userID;
        this.username = username;
        this.password = password;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
