package hu.bme.aut.shed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "card")
@Table(name = "cards")
@Getter
@Setter
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "deck")
    private Set<CRD> deckId;

    @OneToMany(mappedBy = "rule")
    private Set<CRD> ruleId;

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
