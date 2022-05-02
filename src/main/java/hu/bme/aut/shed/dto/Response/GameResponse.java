package hu.bme.aut.shed.dto.Response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class GameResponse {
    Long gameId;
    String name;
}
