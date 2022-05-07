package hu.bme.aut.shed.service.RuleStrategy;

import hu.bme.aut.shed.model.*;
import hu.bme.aut.shed.service.CardConfigService;
import hu.bme.aut.shed.service.PlayerCardService;
import hu.bme.aut.shed.service.TableCardService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class RuleHelperService {
    @Autowired
    private final TableCardService tableCardService;
    @Autowired
    private final PlayerCardService playerCardService;
    @Autowired
    private final CardConfigService cardConfigService;

    public TableCard invisibleCardOnTable(Player playerFrom, TableCard tableCard, PlayerCard playerCard) {

        List<TableCard> throwTableCards = tableCardService.getAllByTableCardStateAndGame(TableCardState.THROW, playerFrom.getGame());
        throwTableCards.sort(Comparator.comparing(TableCard::getId));
        TableCard previousCard = tableCard;

        boolean good = false;
        while (!good) {
            if (previousCard.getCardConfig().getRule() == Rule.INVISIBLE) {

                int localIndex = throwTableCards.indexOf(tableCard);
                if (localIndex == 0) {
                    playerFrom.getCards().remove(playerCard);
                    tableCardService.createTableCard(playerCard.getCardConfig(), TableCardState.THROW);
                    playerCardService.removeById(playerCard.getId());
                    good = true;
                } else {
                    localIndex -= 1;
                    previousCard = throwTableCards.get(localIndex);
                }
            } else {
                good = true;
            }
        }
        return previousCard;
    }

    /*public void throwNoneCard(Player playerFrom, TableCard tableCard, PlayerCard playerCard) throws CantThrowCardException {
        if (playerCard.getCardConfig().getNumber() >= tableCard.getCardConfig().getNumber()) {

            if (tableCard.getCardConfig().getRule() == Rule.INVISIBLE) {
                TableCard previousCard = invisibleCardOnTable(playerFrom, tableCard, playerCard);

                if (previousCard.getCardConfig().getRule() == Rule.REDUCER) {
                    if ( playerCard.getCardConfig().getNumber() <= previousCard.getCardConfig().getNumber()) {
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
                TableCard previousCard = invisibleCardOnTable(playerFrom, tableCard, playerCard);

                if (previousCard.getCardConfig().getRule() == Rule.REDUCER) {
                    if ( playerCard.getCardConfig().getNumber() <= previousCard.getCardConfig().getNumber()) {
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
            }
        } else {
            throw new CantThrowCardException();
        }
    }

    public void throwBurnerCard(Player playerFrom, TableCard tableCard, PlayerCard playerCard) throws CantThrowCardException {
        if (playerCard.getCardConfig().getNumber() <= tableCard.getCardConfig().getNumber()) {
            if (tableCard.getCardConfig().getRule() == Rule.INVISIBLE) {
                TableCard previousCard = invisibleCardOnTable(playerFrom, tableCard, playerCard);

                if (previousCard.getCardConfig().getRule() == Rule.REDUCER) {
                    if ( playerCard.getCardConfig().getNumber() <= previousCard.getCardConfig().getNumber()) {
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
    }*/
}
