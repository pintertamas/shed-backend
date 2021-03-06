package hu.bme.aut.shed.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private List<Player> players;

    @OneToOne()
    private Player currentPlayer;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private List<CardConfig> deck;

    @Column()
    private LocalDateTime creationTime;

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

    public Game(int cardsInHand, int numberOfDecks, String name, boolean visibility, boolean jokers) {
        this.name = name;
        this.cardsInHand = cardsInHand;
        this.maxPlayers = 5 * numberOfDecks;
        this.status = GameStatus.NEW;
        this.jokers = jokers;
        this.numberOfDecks = numberOfDecks;
        this.visibility = visibility;
        this.creationTime = LocalDateTime.now();
        this.players = new ArrayList<>();
        this.deck = new ArrayList<>();
    }

}
