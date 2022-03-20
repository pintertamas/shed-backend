package hu.bme.aut.shed.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Game")
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column()
    private String name;

    @Column()
    private GameStatus status;

    @OneToMany(mappedBy = "game")
    private Set<Player> players;

    @Column()
    private int cardsInHand; // amount of card users have in their hands initially

    @Column()
    private int maxPlayers;

    @Column()
    private int numberOfDecks;

    @Column()
    private boolean jokers;

    @Column()
    private boolean visibility;

    public Game(int cardsInHand, int numberOfDecks, String name, boolean visibility) {
        this.name = name;
        this.cardsInHand = cardsInHand;
        this.maxPlayers = 5 * numberOfDecks;
        this.status = GameStatus.NEW;
        this.numberOfDecks = numberOfDecks;
        this.visibility = visibility;
    }
}
