package hu.bme.aut.shed.service;

import hu.bme.aut.shed.exception.CantThrowCardException;
import hu.bme.aut.shed.model.*;
import hu.bme.aut.shed.service.RuleStrategy.RuleStrategy;
import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class PlayerCardStateHelperService {

    @Autowired
    private final Map<String, RuleStrategy> ruleStrategy;

    @Autowired
    private final TableCardService tableCardService;

    @Autowired
    private final PlayerCardService playerCardService;

    private void throwCardByRuleStrategy(Player playerFrom, TableCard tableCard, PlayerCard playerCard) throws CantThrowCardException {
        if (tableCard == null) {                                                  //If we dont a have a throw table card then all card works like a jolly
            ruleStrategy.get("JOLLY_JOKER").throwCard(playerFrom, tableCard, playerCard);
            LoggerFactory.getLogger(this.getClass()).info(String.valueOf("Idáig 1"));
        } else {
            ruleStrategy.get(playerCard.getCardConfig().getRule().name()).throwCard(playerFrom, tableCard, playerCard);
        }
        LoggerFactory.getLogger(this.getClass()).info(String.valueOf("Idáig 2"));
    }

    public void pickCard(Player playerTo, TableCard tableCard) {
        CardConfig cardConfig = tableCard.getCardConfig();
        playerCardService.createPlayerCard(cardConfig, playerTo, PlayerCardState.HAND); //This adds card also to player cards list
        tableCardService.removeTableCardByCardConfig(tableCard.getCardConfig());
    }

    public void pickUpAllThrowCards(Player playerFrom,PlayerCard playerCard){
        Game game = playerFrom.getGame();
        List<TableCard> throwTableCards = tableCardService.getAllByTableCardStateAndGame(TableCardState.THROW,game);
        for(TableCard throwTableCard : throwTableCards){
            this.pickCard(playerFrom , throwTableCard);
        }
        playerCardService.setStateOfPlayerCard(playerCard, PlayerCardState.HAND);
    }

    public void throwCard(Player playerFrom, TableCard tableCard, PlayerCard playerCard) throws CantThrowCardException {
        if (playerCard.getState() == PlayerCardState.HAND) {
            this.throwCardByRuleStrategy(playerFrom, tableCard, playerCard);
        }
        if (playerCard.getState() == PlayerCardState.VISIBLE) {
            try {
                int tableCardPickSize = tableCardService.getAllByTableCardStateAndGame(TableCardState.PICK, playerFrom.getGame()).size();
                int playerCardHandSize = playerCardService.getAllPlayerCardsByPlayerAndState(playerFrom, PlayerCardState.HAND).size();
                if (tableCardPickSize == 0 && playerCardHandSize == 0) {
                    this.throwCardByRuleStrategy(playerFrom, tableCard, playerCard);
                }
            }catch (CantThrowCardException exception){
                pickUpAllThrowCards(playerFrom, playerCard);
            }

        }
        if (playerCard.getState() == PlayerCardState.INVISIBLE) {
            try{
                int playerCardVisibleSize = playerCardService.getAllPlayerCardsByPlayerAndState(playerFrom, PlayerCardState.VISIBLE).size();
                int playerCardHandSize = playerCardService.getAllPlayerCardsByPlayerAndState(playerFrom, PlayerCardState.HAND).size();
                if (playerCardVisibleSize == 0 && playerCardHandSize == 0) {
                    this.throwCardByRuleStrategy(playerFrom, tableCard, playerCard);
                }
            }catch (CantThrowCardException exception){
                pickUpAllThrowCards(playerFrom,playerCard);
            }
        }
    }
}
