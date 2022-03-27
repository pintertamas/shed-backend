package hu.bme.aut.shed.service;

import hu.bme.aut.shed.model.CardConfig;
import hu.bme.aut.shed.model.Shape;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

@Service
public class CardConfigService {

    public ArrayList<CardConfig> createCards(int numberOfDecks, boolean jokers) {
        int numberOfCards = numberOfDecks * 13 * Shape.values().length;
        if (jokers) numberOfCards += 3;

        ArrayList<CardConfig> cards = new ArrayList<>(numberOfCards);

        for (int i = 0; i < numberOfDecks; i++) {
            for (Shape shape : Shape.values()) {
                for (int cardNumber = 2; cardNumber < 14; cardNumber++) {
                    CardConfig newCard = new CardConfig();
                    newCard.setNumber(cardNumber);
                    newCard.setShape(shape);
                    cards.add(newCard);
                }
                CardConfig newCard = new CardConfig();
                newCard.setNumber(1);
                newCard.setShape(shape);
                cards.add(newCard);
            }
        }
        return shuffleDeck(cards);
    }
    public ArrayList<CardConfig>  shuffleDeck(ArrayList<CardConfig> cards) {
        // copy cards to an ArrayList
        ArrayList<CardConfig> cardsCopy = new ArrayList<>(cards);

        shuffleArrayList(cardsCopy);

        return cardsCopy;
    }

    private void shuffleArrayList(ArrayList<?> array) {
        Iterator<?> itr = array.iterator();

        while (itr.hasNext()) {
            System.out.print(itr.next() + " ");
        }

        Random random = new Random();

        for (int i = array.size() - 1; i >= 1; i--) {
            Collections.swap(array, i, random.nextInt(i + 1));
        }

        System.out.println("After shuffling Arraylist:");

        itr = array.iterator();

        while (itr.hasNext()) {
            System.out.print(itr.next() + " ");
        }
    }
}
