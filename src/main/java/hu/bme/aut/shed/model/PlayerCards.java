package hu.bme.aut.shed.model;

import javax.persistence.*;


@Entity
@Table(name = "PlayerCardsState")
public class PlayerCards {

    @EmbeddedId
    PlayerCardsKey Id;

    @ManyToOne
    @MapsId("cardConfigId")
    @JoinColumn(name = "cardconfigId")
    CardConfig cardConfig;

    @ManyToOne
    @MapsId("playerId")
    @JoinColumn(name = "playerId")
    Player player;

    @Column(name = "state")
    CardState state;
}
