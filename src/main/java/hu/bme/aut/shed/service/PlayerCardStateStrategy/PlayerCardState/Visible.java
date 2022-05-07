package hu.bme.aut.shed.service.PlayerCardStateStrategy.PlayerCardState;

import hu.bme.aut.shed.exception.CantThrowCardException;
import hu.bme.aut.shed.model.Player;
import hu.bme.aut.shed.model.PlayerCard;
import hu.bme.aut.shed.model.PlayerCardState;
import hu.bme.aut.shed.model.TableCard;
import hu.bme.aut.shed.service.PlayerCardService;
import hu.bme.aut.shed.service.PlayerCardStateStrategy.PlayerCardStateStrategy;
import hu.bme.aut.shed.service.RuleStrategy.RuleStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("VISIBLE")
public class Visible implements PlayerCardStateStrategy {

    @Autowired
    private Map<String, RuleStrategy> ruleStrategy;
    @Autowired
    private PlayerCardService playerCardService;

    @Override
    public void throwCard(Player playerFrom, TableCard tableCard, PlayerCard playerCard) throws CantThrowCardException {
        int playerCardVisibleSize = playerCardService.getAllPlayerCardsByPlayerAndState(playerFrom, PlayerCardState.VISIBLE).size();
        int playerCardHandSize = playerCardService.getAllPlayerCardsByPlayerAndState(playerFrom, PlayerCardState.HAND).size();
        if (playerCardVisibleSize == 0 && playerCardHandSize == 0) {
            if (tableCard == null) {                                                  //If we dont a have a throw table card then all card works like a jolly
                ruleStrategy.get("JOLLY_JOKER").throwCard(playerFrom, tableCard, playerCard);
            }
            ruleStrategy.get(playerCard.getCardConfig().getRule().name()).throwCard(playerFrom, tableCard, playerCard);
        }
    }
}
