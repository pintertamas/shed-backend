package hu.bme.aut.shed.service.RuleStrategy.Rule;

import hu.bme.aut.shed.exception.CantThrowCardException;
import hu.bme.aut.shed.model.*;
import hu.bme.aut.shed.service.CardConfigService;
import hu.bme.aut.shed.service.PlayerCardService;
import hu.bme.aut.shed.service.RuleHelperService;
import hu.bme.aut.shed.service.RuleStrategy.RuleStrategy;
import hu.bme.aut.shed.service.TableCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("BURNER")
public class Burner implements RuleStrategy {

    @Autowired
    private TableCardService tableCardService;
    @Autowired
    private PlayerCardService playerCardService;
    @Autowired
    private RuleHelperService ruleHelperService;
    @Autowired
    private CardConfigService cardConfigService;

    @Override
    public void throwCard(Player playerFrom, TableCard tableCard, PlayerCard playerCard) throws CantThrowCardException {
        if (playerCard.getCardConfig().getNumber() <= tableCard.getCardConfig().getNumber()) {
            if (tableCard.getCardConfig().getRule() == Rule.INVISIBLE) {
                TableCard previousCard = ruleHelperService.invisibleCardOnTable(playerFrom, tableCard, playerCard);

                if (previousCard.getCardConfig().getRule() == Rule.REDUCER) {
                    if (playerCard.getCardConfig().getNumber() <= previousCard.getCardConfig().getNumber()) {
                        playerFrom.getCards().remove(playerCard);
                        tableCardService.createTableCard(playerCard.getCardConfig(), TableCardState.THROW);
                        playerCardService.removeById(playerCard.getId());
                    } else {
                        throw new CantThrowCardException();
                    }
                }
            } else {
                playerFrom.getCards().remove(playerCard);
                tableCardService.createTableCard(playerCard.getCardConfig(), TableCardState.THROW);
                playerCardService.removeById(playerCard.getId());
                List<CardConfig> cardConfigs = cardConfigService.getCardConfigsByGameId(playerFrom.getGame().getId());
                for (CardConfig cardConfig : cardConfigs) {
                    tableCardService.removeTableCardsByCardConfigAndTableCardState(cardConfig, TableCardState.THROW);
                }
            }
        } else {
            throw new CantThrowCardException();
        }
    }
}
