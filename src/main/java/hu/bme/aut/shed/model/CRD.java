package hu.bme.aut.shed.model;

import javax.persistence.*;

@Entity
@Table(name = "CRD")
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

    @ManyToOne
    @MapsId("ruleId")
    @JoinColumn(name = "ruleId")
    Rule rule;
}
