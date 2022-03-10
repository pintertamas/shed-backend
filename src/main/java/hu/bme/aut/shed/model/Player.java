package hu.bme.aut.shed.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Player implements Comparable {
    private User user;
    private String username;
    private ArrayList<Card> hiddenCards;
    private ArrayList<Card> visibleCards;
    private ArrayList<Card> hand;

    public Player(User user) {
        this.user = user;
        this.username = user.getUsername();
        this.hiddenCards = new ArrayList<>();
        this.visibleCards = new ArrayList<>();
        this.hand = new ArrayList<>();
    }

    public void initPlayer(int numberOfCards) {
        setHiddenCards(new ArrayList<>(numberOfCards));
        setVisibleCards(new ArrayList<>(numberOfCards));
        setHand(new ArrayList<>(numberOfCards));
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
