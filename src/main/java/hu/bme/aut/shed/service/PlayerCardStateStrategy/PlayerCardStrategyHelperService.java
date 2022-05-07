package hu.bme.aut.shed.service.PlayerCardStateStrategy;

import hu.bme.aut.shed.exception.CantThrowCardException;
import hu.bme.aut.shed.model.*;
import hu.bme.aut.shed.service.PlayerCardService;
import hu.bme.aut.shed.service.RuleStrategy.RuleStrategy;
import hu.bme.aut.shed.service.TableCardService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class PlayerCardStrategyHelperService {

    @Autowired
    private final Map<String, RuleStrategy> ruleStrategy;

    @Autowired
    private final TableCardService tableCardService;
    @Autowired
    private final PlayerCardService playerCardService;

    public void throwCard(Player playerFrom, TableCard tableCard, PlayerCard playerCard) throws CantThrowCardException {
        if (playerCard.getState() == PlayerCardState.HAND) {
            if (tableCard == null) {                                                  //If we dont a have a throw table card then all card works like a jolly
                ruleStrategy.get("JOLLY_JOKER").throwCard(playerFrom, tableCard, playerCard);
            }
            ruleStrategy.get(playerCard.getCardConfig().getRule().name()).throwCard(playerFrom, tableCard, playerCard);
        }
        if (playerCard.getState() == PlayerCardState.VISIBLE) {
            int tableCardPickSize = tableCardService.getAllByTableCardStateAndGame(TableCardState.PICK, playerFrom.getGame()).size();
            int playerCardHandSize = playerCardService.getAllPlayerCardsByPlayerAndState(playerFrom, PlayerCardState.HAND).size();
            if (tableCardPickSize == 0 && playerCardHandSize == 0) {
                if (tableCard == null) {                                                  //If we dont a have a throw table card then all card works like a jolly
                    ruleStrategy.get("JOLLY_JOKER").throwCard(playerFrom, tableCard, playerCard);
                }
                ruleStrategy.get(playerCard.getCardConfig().getRule().name()).throwCard(playerFrom, tableCard, playerCard);
            }
        }
        if (playerCard.getState() == PlayerCardState.INVISIBLE) {
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

}
