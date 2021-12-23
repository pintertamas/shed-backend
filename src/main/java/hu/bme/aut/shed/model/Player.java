package hu.bme.aut.shed.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Player {
    private String username;
    private ArrayList<Card> hiddenCards;
    private ArrayList<Card> visibleCards;
    private ArrayList<Card> hand;

    public Player(String username, int amountOfCards) {
        this.hiddenCards = new ArrayList<>(amountOfCards);
        this.visibleCards = new ArrayList<>(amountOfCards);
        this.hand = new ArrayList<>(amountOfCards);
    }
}
