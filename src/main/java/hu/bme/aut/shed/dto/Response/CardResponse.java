package hu.bme.aut.shed.dto.Response;

import hu.bme.aut.shed.model.Shape;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardResponse {
    Shape shape;
    String rule;
    String gameName;
    int number;
}
