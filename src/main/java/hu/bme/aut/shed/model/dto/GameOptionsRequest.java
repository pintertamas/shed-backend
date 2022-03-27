package hu.bme.aut.shed.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class GameOptionsRequest {
    int numberOfDecks;
    int numberOfCardsInHand;
    int numberOfPlayers;
    boolean joker;
}
