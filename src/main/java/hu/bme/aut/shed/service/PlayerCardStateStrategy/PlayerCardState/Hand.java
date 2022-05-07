package hu.bme.aut.shed.service.PlayerCardStateStrategy.PlayerCardState;

import hu.bme.aut.shed.exception.CantThrowCardException;
import hu.bme.aut.shed.model.Player;
import hu.bme.aut.shed.model.PlayerCard;
import hu.bme.aut.shed.model.TableCard;
import hu.bme.aut.shed.service.PlayerCardStateStrategy.PlayerCardStateStrategy;
import hu.bme.aut.shed.service.RuleStrategy.RuleStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("HAND")
public class Hand implements PlayerCardStateStrategy {

    @Autowired
    private Map<String, RuleStrategy> ruleStrategy;

    @Override
    public void throwCard(Player playerFrom, TableCard tableCard, PlayerCard playerCard) throws CantThrowCardException {
        if (tableCard == null) {                                                  //If we dont a have a throw table card then all card works like a jolly
            ruleStrategy.get("JOLLY_JOKER").throwCard(playerFrom, tableCard, playerCard);
        }
        ruleStrategy.get(playerCard.getCardConfig().getRule().name()).throwCard(playerFrom, tableCard, playerCard);
    }
}
