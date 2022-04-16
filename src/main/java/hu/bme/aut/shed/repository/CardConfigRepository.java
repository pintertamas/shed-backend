package hu.bme.aut.shed.repository;

import hu.bme.aut.shed.model.CardConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardConfigRepository extends JpaRepository<CardConfig, Long> {
    List<CardConfig> findAllByGameId(Long game_id);

}
