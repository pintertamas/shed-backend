package hu.bme.aut.shed.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;


@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class PlayerCardsKey implements Serializable {

    @Column(name = "cardConfig_id")
    Long cardConfigId;

    @Column(name = "player_id")
    Long playerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerCardsKey that = (PlayerCardsKey) o;
        return Objects.equals(cardConfigId, that.cardConfigId) && Objects.equals(playerId, that.playerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardConfigId, playerId);
    }
}
