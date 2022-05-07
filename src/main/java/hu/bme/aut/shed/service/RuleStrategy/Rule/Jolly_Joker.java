package hu.bme.aut.shed.service.RuleStrategy.Rule;

import hu.bme.aut.shed.model.Player;
import hu.bme.aut.shed.model.PlayerCard;
import hu.bme.aut.shed.model.TableCard;
import hu.bme.aut.shed.model.TableCardState;
import hu.bme.aut.shed.service.PlayerCardService;
import hu.bme.aut.shed.service.RuleStrategy.RuleStrategy;
import hu.bme.aut.shed.service.TableCardService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("JOLLY_JOKER")
public class Jolly_Joker implements RuleStrategy {

    @Autowired
    private TableCardService tableCardService;
    @Autowired
    private PlayerCardService playerCardService;

    @Override
    public void throwCard(Player playerFrom, TableCard tableCard, PlayerCard playerCard) {
        LoggerFactory.getLogger(this.getClass()).info(String.valueOf("idaig 3"));
        playerFrom.getCards().remove(playerCard);
        tableCardService.createTableCard(playerCard.getCardConfig(), TableCardState.THROW);
        playerCardService.removeById(playerCard.getId());
        LoggerFactory.getLogger(this.getClass()).info(String.valueOf("idaig 4"));
    }
}
