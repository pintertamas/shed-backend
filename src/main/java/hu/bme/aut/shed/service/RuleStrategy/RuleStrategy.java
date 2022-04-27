package hu.bme.aut.shed.service.RuleStrategy;

import hu.bme.aut.shed.exception.CantThrowCardException;
import hu.bme.aut.shed.model.Player;
import hu.bme.aut.shed.model.PlayerCard;
import hu.bme.aut.shed.model.TableCard;

public interface RuleStrategy {
    void throwCard(Player playerFrom, TableCard tableCard, PlayerCard playerCard) throws CantThrowCardException;
}
