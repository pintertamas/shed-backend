package hu.bme.aut.shed.service;

import hu.bme.aut.shed.model.CardConfig;
import hu.bme.aut.shed.model.Player;
import hu.bme.aut.shed.model.PlayerCard;
import hu.bme.aut.shed.model.PlayerCardState;
import hu.bme.aut.shed.repository.PlayerCardRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class PlayerCardService {

    @Autowired
    private final PlayerCardRepository playerCardRepository;

    @Transactional()
    public PlayerCard createPlayerCard(CardConfig cardConfig, Player player, PlayerCardState cardState) {
        PlayerCard playerCard = new PlayerCard();
        playerCard.setCardConfig(cardConfig);
        playerCard.setPlayer(player);
        playerCard.setState(cardState);
        return playerCardRepository.save(playerCard);
    }

    public void transferPlayerCardConfig(PlayerCard playerCard, Player player) {
        playerCard.setPlayer(player);
        playerCardRepository.save(playerCard);
    }

    public void setStateOfPlayerCardConfig() {
    }

    @Transactional()
    public void removeById(Long id) {
        playerCardRepository.deleteById(id);
    }

    @Transactional()
    public void removeByGameId(CardConfig cardConfig) {
        playerCardRepository.deleteByCardConfig(cardConfig);
    }


}
