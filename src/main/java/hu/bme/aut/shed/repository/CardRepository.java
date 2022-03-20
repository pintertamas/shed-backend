package hu.bme.aut.shed.repository;

import hu.bme.aut.shed.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
}
