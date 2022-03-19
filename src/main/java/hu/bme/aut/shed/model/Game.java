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

    @OneToOne
    private Deck deck;

    @Column()
    private GameStatus status;

    @OneToMany(mappedBy = "game")
    private Set<Player> players;

    @Column()
    private int numberOfCards; // amount of card users have in their hands initially

    @Column()
    private int numberOfDecks;

    @Column()
    private int maxPlayers;

    @Column()
    private boolean visibility;

    public Game(int numberOfCards, int numberOfDecks , String name) {
        this.name = name;
        this.numberOfCards = numberOfCards;
        this.numberOfDecks = numberOfDecks;
        this.maxPlayers = 5 * numberOfDecks;
        this.status = GameStatus.NEW;
        this.deck = new Deck(numberOfDecks);
        this.visibility = false;
    }

}
