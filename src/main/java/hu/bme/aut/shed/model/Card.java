package hu.bme.aut.shed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity(name = "card")
@Table(name = "card")
@Getter
public class Card {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    Shape shape;

    @Column()
    @Min(2)
    @Max(14)
    int number;

    @Override
    public String toString() {
        return "Card{" +
                "shape=" + shape +
                ", number=" + number +
                '}';
    }
}
