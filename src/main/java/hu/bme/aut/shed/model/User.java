package hu.bme.aut.shed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "User")
@NoArgsConstructor
@AllArgsConstructor
public class User implements Comparable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long ID;

    @Column(name = "username")
    String username;
    @Column(name = "password")
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
