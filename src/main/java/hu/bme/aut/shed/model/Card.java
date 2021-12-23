package hu.bme.aut.shed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@AllArgsConstructor
@Getter
public class Card {
    Shape shape;
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
