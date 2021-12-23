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
    private int amountOfCards;
    private int amountOfDecks;
    private int maxPlayers;

    public Game(Player player, int amountOfCards, int amountOfDecks) {
        this.gameId = UUID.randomUUID().toString();
        this.players = new PriorityQueue<>();
        this.players.add(player);
        this.deck = new Deck();
        this.status = GameStatus.NEW;
        this.amountOfCards = amountOfCards;
        this.amountOfDecks = amountOfDecks;
        this.maxPlayers = 5 * amountOfDecks;
    }

    public void initGame() {
        getDeck().createCards();
        getDeck().shuffleDeck();
        for (Player player : getPlayers()) {
            for (int i = 0; i < amountOfCards; i++)
            player.getHiddenCards().add(getDeck().getCards().pop());
            player.getHiddenCards().add(getDeck().getCards().pop());
            player.getHiddenCards().add(getDeck().getCards().pop());
        }

        setStatus(GameStatus.IN_PROGRESS);
    }
}
