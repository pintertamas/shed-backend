package hu.bme.aut.shed.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class CRD_Key implements Serializable {
    @Column(name = "cardId")
    Long cardId;
    @Column(name = "deckId")
    Long deckId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CRD_Key crd_key = (CRD_Key) o;
        return Objects.equals(cardId, crd_key.cardId) && Objects.equals(deckId, crd_key.deckId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardId, deckId);
    }
}
