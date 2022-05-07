package hu.bme.aut.shed.service.PlayerCardStateStrategy;

import hu.bme.aut.shed.exception.CantThrowCardException;
import hu.bme.aut.shed.model.Player;
import hu.bme.aut.shed.model.PlayerCard;
import hu.bme.aut.shed.model.TableCard;

public interface PlayerCardStateStrategy {
    void throwCard(Player playerFrom, TableCard tableCard, PlayerCard playerCard) throws CantThrowCardException;
}
