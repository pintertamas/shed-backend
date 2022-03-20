package hu.bme.aut.shed.repository;

import hu.bme.aut.shed.model.Game;
import hu.bme.aut.shed.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player,Long> {
    List<Player> findByGame(Game game);
}
