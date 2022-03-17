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
public class PlayerCardsStateKey implements Serializable {

    @Column(name = "cardId")
    Long cardId;

    @Column(name = "playerId")
    Long playerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerCardsStateKey that = (PlayerCardsStateKey) o;
        return Objects.equals(cardId, that.cardId) && Objects.equals(playerId, that.playerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardId, playerId);
    }
}
