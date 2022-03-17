package hu.bme.aut.shed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user")
public class User implements Comparable {
    @Id
    String id;

    String username;
    String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
