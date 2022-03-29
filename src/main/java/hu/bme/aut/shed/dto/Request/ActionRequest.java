package hu.bme.aut.shed.dto.Request;

import hu.bme.aut.shed.model.CardConfig;
import hu.bme.aut.shed.model.Player;
import lombok.Data;

@Data
public class ActionRequest {
    Long gameId;
    Player player;
    CardConfig playedCard;
}