package hu.bme.aut.shed.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ActionResponse {
    String validity;
    Message message;
    String username;
    List<CardResponse> cards;
}