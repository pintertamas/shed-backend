package hu.bme.aut.shed.service;

import hu.bme.aut.shed.model.*;
import hu.bme.aut.shed.repository.CardConfigRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class CardConfigService {

    @Autowired
    private final CardConfigRepository cardConfigRepository;

    @Autowired
    private PlayerCardService playerCardService;
    @Autowired
    private TableCardService tableCardService;

    public ArrayList<CardConfig> createCards(Game game , Map<Integer,Rule> cardRules) throws IllegalArgumentException {
        int numberOfCards = game.getNumberOfDecks() * 13 * Shape.values().length;
        if (game.isJokers()) numberOfCards += 4;

        ArrayList<CardConfig> cards = new ArrayList<>(numberOfCards);

        for (int i = 0; i < game.getNumberOfDecks(); i++) {
            for (Shape shape : Shape.values()) {
                for (int cardNumber = 2; cardNumber < 15; cardNumber++) {
                    CardConfig newCard = new CardConfig();
                    newCard.setNumber(cardNumber);
                    newCard.setShape(shape);
                    newCard.setGame(game);
                    newCard.setRule(cardRules.get(cardNumber));
                    cards.add(newCard);
                    cardConfigRepository.save(newCard);
                }
                CardConfig newCard = new CardConfig();
                newCard.setNumber(1);
                newCard.setShape(shape);
                newCard.setGame(game);
                newCard.setRule(cardRules.get(1));
                cards.add(newCard);
                cardConfigRepository.save(newCard);
            }
        }
        return shuffleDeck(cards);
    }

    @Transactional
    public void deleteCardConfigs(Long gameId){
        List<CardConfig> deletedCards = cardConfigRepository.findAllByGameId(gameId);
        for (CardConfig cardConfig : deletedCards){
            playerCardService.removeByGameId(cardConfig);       //I delete also the cards which are already been drawn by players
            tableCardService.removeTableCardsByCardConfig(cardConfig); ////I delete also the cards which are already been drawn by table
            cardConfigRepository.deleteById(cardConfig.getId());
        }
    }

    public List<CardConfig> getCardConfigsByGameId(Long gameId){
        return cardConfigRepository.findAllByGameId(gameId);
    }

    public ArrayList<CardConfig> shuffleDeck(ArrayList<CardConfig> cards) {
        // copy cards to an ArrayList
        ArrayList<CardConfig> cardsCopy = new ArrayList<>(cards);

        for(int i = 0 ; i < 30 ; i++){
            shuffleArrayList(cardsCopy);
        }
        return cardsCopy;
    }

    private void shuffleArrayList(ArrayList<?> array) {
        /*Iterator<?> itr = array.iterator();

        while (itr.hasNext()) {
            System.out.print(itr.next() + " ");
        }*/

        Random random = new Random();

        for (int i = array.size() - 1; i >= 1; i--) {
            Collections.swap(array, i, random.nextInt(i + 1));
        }

        /*System.out.println("After shuffling Arraylist:");

        itr = array.iterator();

        while (itr.hasNext()) {
            System.out.print(itr.next() + " ");
        }*/
    }
}
