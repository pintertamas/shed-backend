package hu.bme.aut.shed.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class GameOptionsRequest {
    int numberOfDecks;
    int numberOfCardsInHand;
    boolean visible;
    boolean joker;
    List<CardRuleRequest> cardRules;
}
