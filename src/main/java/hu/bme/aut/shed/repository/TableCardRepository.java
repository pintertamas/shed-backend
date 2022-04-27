package hu.bme.aut.shed.repository;

import hu.bme.aut.shed.model.CardConfig;
import hu.bme.aut.shed.model.TableCard;
import hu.bme.aut.shed.model.TableCardState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableCardRepository extends JpaRepository<TableCard, Long> {
    void deleteByCardConfig(CardConfig cardConfig);

    TableCard findByState(TableCardState tableCardState);

    void deleteByCardConfigAndAndState(CardConfig cardConfig, TableCardState tableCardState);
}
