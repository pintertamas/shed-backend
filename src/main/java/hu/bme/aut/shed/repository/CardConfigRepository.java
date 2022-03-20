package hu.bme.aut.shed.repository;

import hu.bme.aut.shed.model.CardConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardConfigRepository extends JpaRepository<CardConfig, Long> {
}
