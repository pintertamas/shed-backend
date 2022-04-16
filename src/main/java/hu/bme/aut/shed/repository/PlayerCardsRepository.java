package hu.bme.aut.shed.repository;

import hu.bme.aut.shed.model.PlayerCards;
import hu.bme.aut.shed.model.PlayerCardsKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerCardsRepository extends JpaRepository<PlayerCards, PlayerCardsKey> {
}
