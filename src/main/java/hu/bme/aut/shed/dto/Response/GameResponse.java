package hu.bme.aut.shed.dto.Response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class GameResponse {
    Long gameId;
    String name;
}
