package hu.bme.aut.shed.repository;

import hu.bme.aut.shed.model.Game;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends MongoRepository<Game, String> {
    @Override
    Optional<Game> findById(String s);
}
