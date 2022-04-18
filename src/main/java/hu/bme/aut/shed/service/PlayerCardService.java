package hu.bme.aut.shed.service;

import hu.bme.aut.shed.model.*;
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
    public PlayerCard createPlayerCards(CardConfig cardConfig , Player player , PlayerCardState cardState){
        PlayerCard playerCards = new PlayerCard();
        playerCards.setCardConfig(cardConfig);
        playerCards.setPlayer(player);
        playerCards.setState(cardState);
        return playerCardRepository.save(playerCards);
    }

    @Transactional()
    public void removeById(Long id){
        playerCardRepository.deleteById(id);
    }

    @Transactional()
    public void removeByGameId(CardConfig cardConfig){
        playerCardRepository.deleteByCardConfig(cardConfig);
    }



}
