package hu.bme.aut.shed.model.dto;

import lombok.Data;

@Data
public class ConnectionRequest {
    private String gameId;
    private String username;
}
