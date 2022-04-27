package hu.bme.aut.shed.service.RuleStrategy.Rule;

import hu.bme.aut.shed.model.Player;
import hu.bme.aut.shed.model.PlayerCard;
import hu.bme.aut.shed.model.TableCard;
import hu.bme.aut.shed.service.RuleStrategy.RuleStrategy;
import org.springframework.stereotype.Component;

@Component("JOLLY_JOKER")
public class Jolly_Joker implements RuleStrategy {
    @Override
    public void throwCard(Player playerFrom, TableCard tableCard, PlayerCard playerCard) {

    }
}
