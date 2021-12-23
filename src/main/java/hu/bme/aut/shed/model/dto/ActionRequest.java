package hu.bme.aut.shed.model.dto;

import hu.bme.aut.shed.model.Card;
import hu.bme.aut.shed.model.Player;
import lombok.Data;

@Data
public class ActionRequest {
    String gameId;
    Player player;
    Card playedCard;
}
