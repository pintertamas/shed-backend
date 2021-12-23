package hu.bme.aut.shed.repository;

import hu.bme.aut.shed.model.Game;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends MongoRepository<Game, String> {
    Game findGameByGameId(String gameId);
}
