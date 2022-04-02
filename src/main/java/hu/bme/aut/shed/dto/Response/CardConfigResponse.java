package hu.bme.aut.shed.dto.Response;

import hu.bme.aut.shed.model.Rule;
import hu.bme.aut.shed.model.Shape;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CardConfigResponse {
    Long GameId;
    Shape shape;
    Rule rule;
    int number;
}
