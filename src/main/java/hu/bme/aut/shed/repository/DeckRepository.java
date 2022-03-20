package hu.bme.aut.shed.repository;

import hu.bme.aut.shed.model.Deck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeckRepository extends JpaRepository<Deck, String> {
}
