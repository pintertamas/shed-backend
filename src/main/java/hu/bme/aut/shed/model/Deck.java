package hu.bme.aut.shed.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Deck")
@Table(name = "decks")
public class Deck {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private int numberOfDecks;

    public Deck(int numberOfDecks) {
        this.numberOfDecks = numberOfDecks;
    }
/*
    public void createCards() {
        for (int i = 0; i < numberOfDecks; i++){
            for (Shape shape : Shape.values()) {
                for (int y = 2; y < 14; y++) {
                    Card newCard = new Card(shape, y);
                    this.cards.add(newCard);
                }
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
    }*/
}
