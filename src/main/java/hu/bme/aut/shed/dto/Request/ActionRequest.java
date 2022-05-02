package hu.bme.aut.shed.dto.Request;

import hu.bme.aut.shed.model.Player;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ActionRequest {
    String username;
    List<CardRequest> cards;
}
