package hu.bme.aut.shed.repository;

import hu.bme.aut.shed.model.PlayerCard;
import hu.bme.aut.shed.model.PlayerCardKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerCardRepository extends JpaRepository<PlayerCard, PlayerCardKey> {
}
