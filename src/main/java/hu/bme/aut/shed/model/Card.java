package hu.bme.aut.shed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity(name = "card")
@Table(name = "cards")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column()
    private Shape shape;

    @Column()
    @Min(1)
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
