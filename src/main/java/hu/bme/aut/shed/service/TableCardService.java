package hu.bme.aut.shed.service;

import hu.bme.aut.shed.model.CardConfig;
import hu.bme.aut.shed.model.Game;
import hu.bme.aut.shed.model.TableCard;
import hu.bme.aut.shed.model.TableCardState;
import hu.bme.aut.shed.repository.TableCardRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public List<TableCard> getAllByTableCardStateAndGame(TableCardState tableCardState, Game game) {
        List<TableCard> withOutFilter = tableCardRepository.findAllByState(tableCardState);
        List<TableCard> withFilter = new ArrayList<>();
        for (TableCard tableCard : withOutFilter) {
            if (Objects.equals(tableCard.getCardConfig().getGame().getId(), game.getId())) {
                withFilter.add(tableCard);
            }
        }
        return withFilter;
    }

    public void removeTableCardsByCardConfig(CardConfig cardConfig) {
        tableCardRepository.deleteByCardConfig(cardConfig);
    }

    public void removeTableCardsByCardConfigAndTableCardState(CardConfig cardConfig, TableCardState tableCardState) {
        tableCardRepository.deleteByCardConfigAndState(cardConfig, tableCardState);
    }

}
