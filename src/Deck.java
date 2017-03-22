import java.util.ArrayList;
import java.util.Random;

class Deck {

    private ArrayList<String> drawPile = new ArrayList<>();
    ArrayList<String> discardPile = new ArrayList<>();
    private String[] suits = {"S", "C", "D", "H"};
    private String[] numbers = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    private static Random ram = new Random();

    Deck(){
        populateDeck();
    }

    private void populateDeck(){
        for (String suit: suits){
            for (String number: numbers){
                drawPile.add(suit + number);
            }
        }
    }

    int numericValue(String card) {
        String number = card.substring(1);
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException nfe) {
            switch (number) {
                case "J":
                    return 11;
                case "Q":
                    return 12;
                case "K":
                    return 13;
                case "A":
                    return 14;
                default:
                    return 0; // That's bad btw
            }
        }
    }

    String nameOfValue(int value) {
        if (value < 11){
            return "a " + Integer.toString(value);
        }
        switch (value) {
            case 11:
                return "a Jack";
            case 12:
                return "a Queen";
            case 13:
                return "a King";
            case 14:
                return "an Ace";
            default:
                return "o noses"; // That's bad btw
        }
    }

    String drawCard() throws DeckError {
        if (drawPile.isEmpty()) {
            throw new DeckError("Cannot draw from empty deck");
        }
        int toRemove = ram.nextInt(drawPile.size());
        String toReturn = drawPile.get(toRemove);
        drawPile.remove(toRemove);
        return toReturn;
    }

    ArrayList<String> getDrawPile(){
        return drawPile;
    }
}