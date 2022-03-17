package hu.bme.aut.shed.model;

import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Entity(name = "card")
@Table(name = "cards")
@Getter
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    private List<Rule> ruleId;

    @ManyToMany
    private List<Deck> deckId;

    @Column()
    private Shape shape;

    @Column()
    @Min(2)
    @Max(14)
    private int number;


    @Override
    public String toString() {
        return "Card{" +
                "shape=" + shape +
                ", number=" + number +
                '}';
    }
}
