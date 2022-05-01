package hu.bme.aut.shed.repository;

import hu.bme.aut.shed.model.CardConfig;
import hu.bme.aut.shed.model.TableCard;
import hu.bme.aut.shed.model.TableCardState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableCardRepository extends JpaRepository<TableCard, Long> {
    void deleteByCardConfig(CardConfig cardConfig);

    TableCard findByState(TableCardState tableCardState);

    void deleteByCardConfigAndState(CardConfig cardConfig, TableCardState tableCardState);

    List<TableCard> findAllByState(TableCardState tableCardState);
}
