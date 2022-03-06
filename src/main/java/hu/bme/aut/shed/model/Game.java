package hu.bme.aut.shed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.PriorityQueue;
import java.util.UUID;

@Getter
@Setter
public class Game {
    private String gameId;
    private PriorityQueue<Player> players;
    private Deck deck;
    private GameStatus status;
    private int numberOfCards;
    private int numberOfDecks;
    private int maxPlayers;

    public Game(Player player, int numberOfCards, int numberOfDecks) {
        this.gameId = UUID.randomUUID().toString();
        this.players = new PriorityQueue<>();
        this.players.add(player);
        this.numberOfCards = numberOfCards;
        this.numberOfDecks = numberOfDecks;
        this.deck = new Deck(numberOfDecks);
        this.status = GameStatus.NEW;
        this.maxPlayers = 5 * numberOfDecks;
    }

    public void initGame() {
        getDeck().createCards();
        getDeck().shuffleDeck();
        for (Player player : getPlayers()) {
            player.initPlayer(getNumberOfCards());
            for (int i = 0; i < numberOfCards; i++) {
                player.getHiddenCards().add(getDeck().getCards().pop());
                player.getHiddenCards().add(getDeck().getCards().pop());
                player.getHiddenCards().add(getDeck().getCards().pop());
            }
        }

        setStatus(GameStatus.IN_PROGRESS);
    }

    public void addPlayer(Player newPlayer) {
        players.add(newPlayer);
    }
}
