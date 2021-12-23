package hu.bme.aut.shed.repository;

import hu.bme.aut.shed.model.Game;
import hu.bme.aut.shed.model.GameStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface GameRepository extends MongoRepository<Game, String> {
    Game findGameByGameId(String gameId);
    ArrayList<Game> findAllByStatusEquals(GameStatus status);
}
