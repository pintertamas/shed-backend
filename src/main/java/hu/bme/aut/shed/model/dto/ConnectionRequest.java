package hu.bme.aut.shed.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ConnectionRequest {
    private String gameId;
    private String username;
}
