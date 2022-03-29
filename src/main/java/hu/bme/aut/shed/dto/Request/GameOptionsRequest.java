package hu.bme.aut.shed.dto.Request;

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
