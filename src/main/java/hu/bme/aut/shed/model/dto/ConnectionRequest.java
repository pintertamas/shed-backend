package hu.bme.aut.shed.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ConnectionRequest {
    private Long gameId;
    private String username;
}
