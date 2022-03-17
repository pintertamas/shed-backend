package hu.bme.aut.shed.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Game")
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue
    private Long id;

    @Column()
    private String name;

    @OneToOne
    private Deck deck;

    @Column()
    private GameStatus status;

    @Column()
    private int numberOfCards; // amount of card users have in their hands initially

    @Column()
    private int numberOfDecks;

    @Column()
    private int maxPlayers;

    @Column
    private boolean visibility;

    public Game(int numberOfCards, int numberOfDecks) {
        this.numberOfCards = numberOfCards;
        this.numberOfDecks = numberOfDecks;
        this.maxPlayers = 5 * numberOfDecks;
        this.status = GameStatus.NEW;
        this.deck = new Deck(numberOfDecks);
    }

    public void initGame() {
        //getDeck().createCards();
        //getDeck().shuffleDeck();
        /*for (Player player : getPlayers()) {
            player.initPlayer(getNumberOfCards());
            for (int i = 0; i < numberOfCards; i++) {
                player.getHiddenCards().add(getDeck().getCards().pop());
                player.getHiddenCards().add(getDeck().getCards().pop());
                player.getHiddenCards().add(getDeck().getCards().pop());
            }
        }*/
        setStatus(GameStatus.IN_PROGRESS);
    }
}
