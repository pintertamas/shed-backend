package hu.bme.aut.shed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;

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

    @Column()
    private String username;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;


    public Player(User user) {
        this.user = user;
        this.username = user.getUsername();
    }

    public void initPlayer(int numberOfCards) {
        /*setHiddenCards(new ArrayList<>(numberOfCards));
        setVisibleCards(new ArrayList<>(numberOfCards));
        setHand(new ArrayList<>(numberOfCards));*/
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

}
