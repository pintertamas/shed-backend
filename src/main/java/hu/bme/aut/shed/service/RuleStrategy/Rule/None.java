package hu.bme.aut.shed.service.RuleStrategy.Rule;

import hu.bme.aut.shed.exception.CantThrowCardException;
import hu.bme.aut.shed.model.Player;
import hu.bme.aut.shed.model.PlayerCard;
import hu.bme.aut.shed.model.TableCard;
import hu.bme.aut.shed.model.TableCardState;
import hu.bme.aut.shed.service.PlayerCardService;
import hu.bme.aut.shed.service.RuleStrategy.RuleStrategy;
import hu.bme.aut.shed.service.TableCardService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("NONE")
@AllArgsConstructor
public class None implements RuleStrategy {

    @Autowired
    private final TableCardService tableCardService;

    @Autowired
    private final PlayerCardService playerCardService;

    @Override
    public void throwCard(Player playerFrom, TableCard tableCard, PlayerCard playerCard) throws CantThrowCardException {
        if (playerCard.getCardConfig().getNumber() >= tableCard.getCardConfig().getNumber()) {
            playerFrom.getCards().remove(playerCard);
            tableCardService.createTableCard(playerCard.getCardConfig(), TableCardState.THROW);
            playerCardService.removeById(playerCard.getId());
        } else {
            throw new CantThrowCardException();
        }
    }
}
