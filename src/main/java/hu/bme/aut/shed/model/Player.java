package hu.bme.aut.shed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "player")
@Table(name = "players")
public class Player implements Comparable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    @Column(name = "username")
    private String username;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @OneToMany(mappedBy = "player")
    List<PlayerCard> cards = new ArrayList<PlayerCard>();

    public Player(User user) {
        this.user = user;
        this.username = user.getUsername();
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

}
