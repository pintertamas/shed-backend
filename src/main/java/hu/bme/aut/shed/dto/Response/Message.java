package hu.bme.aut.shed.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Data
public class Message {
    String type;
    String message;
}
