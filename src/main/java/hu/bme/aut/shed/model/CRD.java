package hu.bme.aut.shed.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "CRD")
@Setter
@Getter
public class CRD {

    @EmbeddedId
    CRD_Key Id;

    @ManyToOne
    @MapsId("cardId")
    @JoinColumn(name = "cardId")
    Card card;

    @ManyToOne
    @MapsId("deckId")
    @JoinColumn(name = "deckId")
    Deck deck;

    @Column(name = "rule")
    Rule rule;
}
