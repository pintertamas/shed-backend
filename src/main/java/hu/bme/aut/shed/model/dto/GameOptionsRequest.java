package hu.bme.aut.shed.model.dto;

import lombok.Data;

@Data
public class GameOptionsRequest {
    String creator;
    int numberOfDecks;
    int numberOfCards;

}
