package hu.bme.aut.shed.dto.Response;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameResponse {
    Long gameId;
    String name;
}
