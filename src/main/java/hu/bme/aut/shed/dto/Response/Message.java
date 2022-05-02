package hu.bme.aut.shed.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Message {
    String type;
    String message;
}
