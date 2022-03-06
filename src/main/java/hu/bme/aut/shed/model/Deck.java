package hu.bme.aut.shed.model;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class Deck {
    private Stack<Card> cards;
    private int deck_count;

    public Deck() {
        this.cards = new Stack<>();
    }

    public void createCards() {
        for (Shape shape : Shape.values()) {
            for (int i = 2; i < 14; i++) {
                Card newCard = new Card(shape, i);
                this.cards.add(newCard);
            }
        }
    }

    public void shuffleDeck() {
        // copy cards to an ArrayList
        ArrayList<Card> cardsCopy = new ArrayList<>(this.getCards());

        shuffleArrayList(cardsCopy);

        for (Card card : getCards()) {
            System.out.println(card.toString());
        }

        System.out.println("Shuffled");

        // reset deck
        this.setCards(new Stack<>());

        // add shuffled cards back
        this.getCards().addAll(cardsCopy);

        for (Card card : getCards()) {
            System.out.println(card.toString());
        }
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
