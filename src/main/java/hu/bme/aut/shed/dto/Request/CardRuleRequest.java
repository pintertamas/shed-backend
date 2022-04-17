package hu.bme.aut.shed.dto.Request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Data
@Getter
public class CardRuleRequest {
    int number;
    String rule;
}
