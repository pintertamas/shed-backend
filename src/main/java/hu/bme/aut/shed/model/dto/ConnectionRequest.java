package hu.bme.aut.shed.model.dto;

import hu.bme.aut.shed.model.Player;
import lombok.Data;

@Data
public class ConnectionRequest {
    private String gameId;
    private Player player;
}
