package hu.bme.aut.shed.service;

import hu.bme.aut.shed.model.CardConfig;
import hu.bme.aut.shed.model.Game;
import hu.bme.aut.shed.model.TableCard;
import hu.bme.aut.shed.model.TableCardState;
import hu.bme.aut.shed.repository.TableCardRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
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

    public TableCard getLastTableCard(TableCardState tableCardState, Game game) {
        List<TableCard> tableCards = this.getAllByTableCardStateAndGame(tableCardState, game);
        tableCards.sort(Comparator.comparing(TableCard::getId));
        if (tableCards.size() == 0) {
            return null;
        }
        return tableCards.get(tableCards.size() - 1);
    }

    public boolean checkSameFourLastThrowTableCard(Game game) {
        List<TableCard> tableCards = this.getAllByTableCardStateAndGame(TableCardState.THROW, game);
        tableCards.sort(Comparator.comparing(TableCard::getId));
        boolean fourLastSame = false;
        if (tableCards.size() >= 4) {
            for (int i = tableCards.size() - 1; i > tableCards.size() - 4; i--) {
                if (tableCards.get(i).getCardConfig().getNumber() == tableCards.get(i - 1).getCardConfig().getNumber()) {
                    fourLastSame = true;
                } else {
                    fourLastSame = false;
                    break;
                }
            }

        }
        return false;
    }

    @Transactional
    public void removeById(Long id) {
        tableCardRepository.deleteById(id);
    }

    @Transactional
    public void removeTableCardByCardConfig(CardConfig cardConfig) {
        tableCardRepository.deleteByCardConfig(cardConfig);
    }

    @Transactional
    public void removeTableCardByCardConfigAndTableCardState(CardConfig cardConfig, TableCardState tableCardState) {
        tableCardRepository.deleteByCardConfigAndState(cardConfig, tableCardState);
    }

    @Transactional
    public void removeAllTableCardByTableCardStateAndGame(TableCardState tableCardState, Game game) {
        List<TableCard> tableCards = this.getAllByTableCardStateAndGame(tableCardState, game);
        tableCardRepository.deleteAll(tableCards);
    }

}
