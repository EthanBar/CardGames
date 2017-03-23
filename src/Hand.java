import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

class Hand {

    private Deck deck;
    private ArrayList<String> hand = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);

    Hand(Deck deck){
        this.deck = deck;
    }

    Hand(Deck deck, int startNumber){
        this.deck = deck;
        for (int i = 0; i < startNumber; i++){
            try {
                hand.add(deck.drawCard());
            } catch (DeckError de){
                break;
            }
        }
    }

    ArrayList<String> getHand(){
        return hand;
    }

    void draw(int draws){
        for (int i = 0; i < draws; i++){
            try {
                deck.drawCard();
            }
            catch (DeckError de){
                break;
            }
        }
    }

    void discard(String card){
        if (hand.contains(card)){
            hand.remove(card);
            deck.discardPile.add(card);
        } else {
            System.err.println("Card not found");
        }
    }

    void sort(){
        // Use arraylist's sort method
        hand.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return deck.numericValue(o1) - deck.numericValue(o2);
            }
        });
    }
//
//    String[] getInput(int minCard, boolean mustMatch){
//        String[] inputs;
//        boolean noError = true;
//        do {
//            noError = true;
//            inputs = scanner.nextLine().toUpperCase().split(" ");
//
//        } while (!noError);
//        return inputs;
//    }

}
