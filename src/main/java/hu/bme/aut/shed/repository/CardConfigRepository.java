package hu.bme.aut.shed.repository;

import hu.bme.aut.shed.model.CardConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardConfigRepository extends JpaRepository<CardConfig, Long> {
    List<CardConfig> findAllByGameId(Long game_id);
}
