package hu.bme.aut.shed.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class GameOptionsRequest {
    String creator;
    int numberOfDecks;
    int numberOfCards;

}
