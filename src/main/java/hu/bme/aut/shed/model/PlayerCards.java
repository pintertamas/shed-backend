package hu.bme.aut.shed.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "PlayerCardsState")
public class PlayerCards {

    @EmbeddedId
    PlayerCardsKey id;

    @ManyToOne
    @MapsId("cardConfigId")
    @JoinColumn(name = "cardConfig_id")
    CardConfig cardConfig;

    @ManyToOne
    @MapsId("playerId")
    @JoinColumn(name = "player_id")
    Player player;

    CardState state;
}
