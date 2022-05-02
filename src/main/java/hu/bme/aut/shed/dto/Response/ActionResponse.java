package hu.bme.aut.shed.dto.Response;

import hu.bme.aut.shed.dto.Request.CardRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ActionResponse {
    String username;
    boolean Valid;
    List<CardRequest> cards;
}