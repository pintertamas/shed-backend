package hu.bme.aut.shed.service.PlayerCardStateStrategy.PlayerCardState;

import hu.bme.aut.shed.exception.CantThrowCardException;
import hu.bme.aut.shed.model.*;
import hu.bme.aut.shed.service.PlayerCardService;
import hu.bme.aut.shed.service.PlayerCardStateStrategy.PlayerCardStateStrategy;
import hu.bme.aut.shed.service.RuleStrategy.RuleStrategy;
import hu.bme.aut.shed.service.TableCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("INVISIBLE")
public class Invisible implements PlayerCardStateStrategy {
    @Autowired
    private Map<String, RuleStrategy> ruleStrategy;

    @Autowired
    private TableCardService tableCardService;
    @Autowired
    private PlayerCardService playerCardService;

    @Override
    public void throwCard(Player playerFrom, TableCard tableCard, PlayerCard playerCard) throws CantThrowCardException {
        int tableCardPickSize = tableCardService.getAllByTableCardStateAndGame(TableCardState.PICK, playerFrom.getGame()).size();
        int playerCardHandSize = playerCardService.getAllPlayerCardsByPlayerAndState(playerFrom, PlayerCardState.HAND).size();
        if (tableCardPickSize == 0 && playerCardHandSize == 0) {
            if (tableCard == null) {                                                  //If we dont a have a throw table card then all card works like a jolly
                ruleStrategy.get("JOLLY_JOKER").throwCard(playerFrom, tableCard, playerCard);
            }
            ruleStrategy.get(playerCard.getCardConfig().getRule().name()).throwCard(playerFrom, tableCard, playerCard);
        }
    }
}
