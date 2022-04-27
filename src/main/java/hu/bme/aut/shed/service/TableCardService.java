package hu.bme.aut.shed.service;

import hu.bme.aut.shed.model.CardConfig;
import hu.bme.aut.shed.model.TableCard;
import hu.bme.aut.shed.model.TableCardState;
import hu.bme.aut.shed.repository.TableCardRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TableCardService {

    @Autowired
    private final TableCardRepository tableCardRepository;

    public void createTableCard(CardConfig cardConfig, TableCardState tableCardState) {
        TableCard tableCard = new TableCard();
        tableCard.setCardConfig(cardConfig);
        tableCard.setState(tableCardState);
        tableCardRepository.save(tableCard);
    }

    public void removeTableCardsByCardConfig(CardConfig cardConfig) {
        tableCardRepository.deleteByCardConfig(cardConfig);
    }

}
