package hu.bme.aut.shed.repository;

import hu.bme.aut.shed.model.Game;
import hu.bme.aut.shed.model.GameStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<List<Game>> findAllByStatusAndVisibility(GameStatus status , boolean visibility);
    Optional<List<Game>> findAllByStatus(GameStatus status);
    Optional<Game> findByName(String name);
}
