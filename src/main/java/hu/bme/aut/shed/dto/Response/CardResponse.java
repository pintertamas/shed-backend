package hu.bme.aut.shed.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardResponse {
    Long id;
    int number;
    String shape;
    String rule;
    String gameName;
    String state;
}
