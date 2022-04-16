package hu.bme.aut.shed.repository;

import hu.bme.aut.shed.model.Game;
import hu.bme.aut.shed.model.Player;
import hu.bme.aut.shed.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player,Long> {
    List<Player> findByGame(Game game);
    Player findByUserAndGameId(User user, Long gameId);
    Player findByUsername(String username);
}
