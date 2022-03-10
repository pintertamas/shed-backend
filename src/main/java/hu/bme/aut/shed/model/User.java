package hu.bme.aut.shed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User implements Comparable {
    @Id
    Long ID;

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
