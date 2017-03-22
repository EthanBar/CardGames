import java.util.Scanner;

public class Presidente {

    /* Game of El Presidente
    Note that this class is entirely static
    it's not meant to be used as an object,
    and instead functionally. The other classes
    show more OOP related concepts.
     */

    private static Hand[] players;
    private static Deck deck;
    private static boolean winnerExists = false;
    private static int currentUp = 0;
    private static int nextLeader = 0;
    private static int currentLeader = 0;
    private static boolean leaderUp = true;
    private static int topCard = 1;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        deck = new Deck();
        System.out.println("Welcome to El Presidente! How many players are playing?");
        int playerCount = 0;
        System.out.print("\033[H\033[2J");
        boolean firstAsk = true;
        while (playerCount < 2) {
            if (!firstAsk) {
                System.out.println("More than 1 players are required");
            }
            firstAsk = false;
            playerCount = Integer.parseInt(scanner.nextLine());
        }
        players = new Hand[playerCount];
        int extraCards = 52 % playerCount; // Leftover cards to deal
        for (int i = 0; i < players.length; i++) {
            if (i < extraCards) {
                players[i] = new Hand(deck, 52 / playerCount + 1); // Rounds down + leftover card
            } else {
                players[i] = new Hand(deck, 52 / playerCount); // Rounds down
            }
            players[i].sort();
        }
        while (!winnerExists) {
            turn();
        }
    }

    private static void turn() {
        if (currentUp == currentLeader && !leaderUp) { // If it's back to the leader again
            topCard = 1;
            currentUp = nextLeader;
            currentLeader = nextLeader;
            leaderUp = true;
        }

        System.out.println("\nPlayer " + Integer.toString(currentUp + 1) + " is up. Your hand is: ");
        System.out.println(players[currentUp].getHand());
        if (leaderUp) { // If it's the leader's first turn
            leaderUp = false;
            System.out.println("You may play any card(s)");
            topCard = deck.numericValue(getInput(players[currentUp], true));
        } else { // If it's a normal turn
            System.out.println("You may play any card higher than " + deck.nameOfValue(topCard));
            topCard = deck.numericValue(getInput(players[currentUp], false));
        }
        winnerExists = checkWin();
        if (currentUp >= players.length - 1) { // -1 is for arrays
            currentUp = 0;
        } else {
            currentUp++;
        }
    }

    private static boolean checkWin() {
        for (int handNo = 0; handNo < players.length; handNo++) {
            if (players[handNo].getHand().isEmpty()) {
                System.out.println("Player " + Integer.toString(handNo) + " wins!");
                return true;
            }
        }
        return false;
    }

    static private String getInput(Hand player, boolean isFirst) {
        boolean cardNotFound = true;
        String toReturn = "";
        while (cardNotFound) {
            toReturn = scanner.nextLine().toUpperCase();
            if (toReturn.equals("SKIP")) {
                cardNotFound = false;
            } else {
                if (player.getHand().contains(toReturn)) {
                    if (deck.numericValue(toReturn) > topCard || isFirst) {
                        nextLeader = currentUp;
                        player.discard(toReturn);
                        cardNotFound = false;
                        topCard = deck.numericValue(toReturn);
                    } else {
                        System.out.println("Card too low");
                    }
                } else {
                    System.out.println("Card not found, try again.");
                }
            }
        }
        return toReturn;
    }


}
