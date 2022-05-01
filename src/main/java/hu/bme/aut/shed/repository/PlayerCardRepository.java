package hu.bme.aut.shed.repository;

import hu.bme.aut.shed.model.CardConfig;
import hu.bme.aut.shed.model.Player;
import hu.bme.aut.shed.model.PlayerCard;
import hu.bme.aut.shed.model.PlayerCardState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerCardRepository extends JpaRepository<PlayerCard, Long> {
    void deleteByCardConfig(CardConfig cardConfig);

    List<PlayerCard> findAllByPlayer(Player player);

    List<PlayerCard> findAllByPlayerAndState(Player player, PlayerCardState tableCardState);
}
