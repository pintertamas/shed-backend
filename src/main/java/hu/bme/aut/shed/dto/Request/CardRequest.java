package hu.bme.aut.shed.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardRequest {
    Long id;
    int number;
    String shape;
    String rule;
    String gameName;
    String state;
}