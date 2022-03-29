package hu.bme.aut.shed.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ConnectionRequest {
    private Long gameId;
    private String username;
}
