package hu.bme.aut.shed.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class JoinGameMessage {
    String type;
    String username;
}
