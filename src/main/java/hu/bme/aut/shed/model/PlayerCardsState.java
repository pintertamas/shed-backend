package hu.bme.aut.shed.model;

import javax.persistence.*;

@Entity
public class PlayerCardsState {

    @EmbeddedId
    PlayerCardsStateKey id;

    @ManyToOne
    @MapsId("cardId")
    @JoinColumn(name = "cardId")
    Card card;

    @ManyToOne
    @MapsId("playerId")
    @JoinColumn(name = "playerId")
    Player player;

    @Column(name = "state")
    Card_State state;
}
