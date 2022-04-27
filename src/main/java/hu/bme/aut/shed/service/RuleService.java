package hu.bme.aut.shed.service;

import hu.bme.aut.shed.exception.CantThrowCardException;
import hu.bme.aut.shed.model.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RuleService {
    @Autowired
    private final TableCardService tableCardService;
    @Autowired
    private final PlayerCardService playerCardService;

    private final CardConfigService cardConfigService;

    public void throwNoneCard(Player playerFrom, TableCard tableCard, PlayerCard playerCard) throws CantThrowCardException {
        if (playerCard.getCardConfig().getNumber() >= tableCard.getCardConfig().getNumber()) {
            if (tableCard.getCardConfig().getRule() == Rule.INVISIBLE) {
                //TODO check Invisible card
            } else {
                playerFrom.getCards().remove(playerCard);
                tableCardService.createTableCard(playerCard.getCardConfig(), TableCardState.THROW);
                playerCardService.removeById(playerCard.getId());
            }
        } else {
            throw new CantThrowCardException();
        }
    }

    public void throwJollyJokerCard(Player playerFrom, TableCard tableCard, PlayerCard playerCard) {
        playerFrom.getCards().remove(playerCard);
        tableCardService.createTableCard(playerCard.getCardConfig(), TableCardState.THROW);
        playerCardService.removeById(playerCard.getId());
    }

    public void throwInvisibleCard(Player playerFrom, TableCard tableCard, PlayerCard playerCard) {
        playerFrom.getCards().remove(playerCard);
        tableCardService.createTableCard(playerCard.getCardConfig(), TableCardState.THROW);
        playerCardService.removeById(playerCard.getId());
    }

    public void throwReducerCard(Player playerFrom, TableCard tableCard, PlayerCard playerCard) throws CantThrowCardException {
        if (playerCard.getCardConfig().getNumber() <= tableCard.getCardConfig().getNumber()) {
            if (tableCard.getCardConfig().getRule() == Rule.INVISIBLE) {
                //TODO check Invisible card
            } else {
                playerFrom.getCards().remove(playerCard);
                tableCardService.createTableCard(playerCard.getCardConfig(), TableCardState.THROW);
                playerCardService.removeById(playerCard.getId());
            }
        } else {
            throw new CantThrowCardException();
        }
    }

    public void throwBurnerCard(Player playerFrom, TableCard tableCard, PlayerCard playerCard) throws CantThrowCardException {
        if (playerCard.getCardConfig().getNumber() <= tableCard.getCardConfig().getNumber()) {
            if (tableCard.getCardConfig().getRule() == Rule.INVISIBLE) {
                //TODO check Invisible card
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
