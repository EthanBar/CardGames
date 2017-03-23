import java.util.Scanner;

class Presidente {

    /* Game of El Presidente
    Note that this class is entirely static
    it's not meant to be used as an object,
    and instead functionally. The other classes
    show more OOP related concepts.
     */

    private Hand[] players;
    private Deck deck;
    private boolean winnerExists = false;
    private int currentUp = 0;
    private int nextLeader = 0;
    private int currentLeader = 0;
    private boolean leaderUp = true;
    private int topCard = 1;
    private int cardCount = 1;
    private Scanner scanner = new Scanner(System.in);

    Presidente() {
        deck = new Deck();
        System.out.println("Welcome to El Presidente! How many players are playing?");
        int playerCount = 0;
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

    private void turn() {
        if (currentUp == currentLeader && !leaderUp) { // If it's back to the leader again
            topCard = 1;
            cardCount = 0;
            currentUp = nextLeader;
            currentLeader = nextLeader;
            leaderUp = true;
        }

        System.out.println("\nPlayer " + Integer.toString(currentUp + 1) + " is up. Your hand is: ");
        System.out.println(players[currentUp].getHand());
        if (leaderUp) { // If it's the leader's first turn
            leaderUp = false;
            System.out.println("You may play any card(s)");
            getInput(players[currentUp], true);
        } else { // If it's a normal turn
            System.out.println("You must play " + Integer.toString(cardCount) + " card(s) higher than " + deck.nameOfValue(topCard));
            getInput(players[currentUp], false);
        }
        winnerExists = checkWin();
        if (currentUp >= players.length - 1) { // -1 is for arrays
            currentUp = 0;
        } else {
            currentUp++;
        }
    }

    private boolean checkWin() {
        for (int handNo = 0; handNo < players.length; handNo++) {
            if (players[handNo].getHand().isEmpty()) {
                System.out.println("Player " + Integer.toString(handNo) + " wins!");
                return true;
            }
        }
        return false;
    }

    private void getInput(Hand player, boolean isFirst) { // TODO: implement in Hand class
//        String[] inputsv2 = players[currentUp].getInput(topCard, true);
        boolean errorFound;
        String[] inputs;
        do {
            errorFound = false;
            int previousValue = 0;
            inputs = scanner.nextLine().toUpperCase().split(" ");
            if (isFirst) { // If leader, then they get to set the card count
                cardCount = inputs.length;
            }
            for (String card: inputs) {
                if (card.equals("SKIP")) {
                    return;
                } else {
                    if (inputs.length == cardCount) {
                        if (player.getHand().contains(card)) {
                            if (previousValue == 0 || deck.numericValue(card) == previousValue) {
                                if (deck.numericValue(card) > topCard || isFirst) {
                                    previousValue = deck.numericValue(card);
                                } else {
                                    errorFound = true;
                                    System.out.println("Card(s) too low");
                                    break;
                                }
                            } else {
                                errorFound = true;
                                System.out.println("Card(s) do not match");
                                break;
                            }
                        } else {
                            errorFound = true;
                            System.out.println("Card(s) not found");
                            break;
                        }
                    } else {
                        errorFound = true;
                        System.out.println("Incorrect card count");
                        break;
                    }
                }
            }
        } while (errorFound);
        // We know that they didn't skip, and that the input is valid
        for (String card: inputs) {
            topCard = deck.numericValue(card);
            nextLeader = currentUp;
            player.discard(card);
        }
    }
}