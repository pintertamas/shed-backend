package hu.bme.aut.shed.service;

import hu.bme.aut.shed.exception.CantThrowCardException;
import hu.bme.aut.shed.model.*;
import hu.bme.aut.shed.service.RuleStrategy.RuleStrategy;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class PlayerCardStrategyHelperService {

    @Autowired
    private final Map<String, RuleStrategy> ruleStrategy;

    private final TableCardService tableCardService;

    public void throwCard(Player playerFrom, TableCard tableCard, PlayerCard playerCard) throws CantThrowCardException {
        if(playerCard.getState() == PlayerCardState.HAND){
            if (tableCard == null) {                                                  //If we dont a have a throw table card then all card works like a jolly
                ruleStrategy.get("JOLLY_JOKER").throwCard(playerFrom, tableCard, playerCard);
            }
            ruleStrategy.get(playerCard.getCardConfig().getRule().name()).throwCard(playerFrom, tableCard, playerCard);
        }
        if(playerCard.getState() == PlayerCardState.VISIBLE){
            if(tableCardService.getAllByTableCardStateAndGame(TableCardState.PICK , playerFrom.getGame()).size() == 0){

            }
        }
        if (playerCard.getState() == PlayerCardState.INVISIBLE){

        }
    }

}
