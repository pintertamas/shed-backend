package hu.bme.aut.shed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "User")
@Table(name = "users")
public class User implements Comparable {
    @Id
    @GeneratedValue
    private Long id;

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

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
