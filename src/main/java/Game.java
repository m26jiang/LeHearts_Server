package main.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by chengpeng123 on 15-01-16.
 */
public class Game {

    String[] suit = { "C", "D", "H", "S" };
    Integer[] rank = { 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 };
    ArrayList<Integer> TeamOne, TeamTwo;
    int TeamOneScore, TeamTwoScore;

    ArrayList<String> CURRENT_HAND;

    int SUITS = suit.length;
    int RANKS = rank.length;
    int N = SUITS * RANKS;

    String CURRENT_SUIT;

    String[] deck = new String[N];

    public Game() {
        InitializeDeck();
        ShuffleCards();
        DetermineTeams();
        CURRENT_SUIT = "C";
        CURRENT_HAND = new ArrayList<String>();
        System.out.println("new game");
    }

    public void InitializeDeck() {
        for (int i = 0; i < RANKS; i++) {
            for (int j = 0; j < SUITS; j++) {
                deck[SUITS * i + j] = rank[i] + " : " + suit[j];
            }
        }
    }
    public void ShuffleCards() {
        for (int i = 0; i < N; i++) {
            int r = i + (int) (Math.random() * (N - i));
            String t = deck[r];
            deck[r] = deck[i];
            deck[i] = t;
        }
    }

    public void DetermineTeams() {

        TeamOne = new ArrayList<Integer>();
        TeamTwo = new ArrayList<Integer>();

        // Team 1 : Queen of Spades and Jack of Diamonds

        for (int i = 0; i < N; i++) {
            if (deck[i].equals("12 : S") || deck[i].equals("11 : D")) {
                if (!TeamOne.contains(i / 13 + 1)) {
                    TeamOne.add(i / 13 + 1);
                }
            }
        }

        // Team 2
        for (int i = 1; i < 5; i++) {
            if (!TeamOne.contains(i)) {
                TeamTwo.add(i);
            }
        }

    }
    /**
     * The current player.
     */
    Player currentPlayer;

    public int FirstPlayer() {
        int player = 0;

        for (int i = 0; i < N; i++) {
            if (deck[i].equals("2 : C")) {
                player = i / 13 + 1;
            }
        }

        return player;
    }

    // find the winner of the current hand
    public int HandWinner() {
        int winner = 0;
        int highest = 0;

        // CURRENT_HAND FORMAT:{ "{PLAYER_NUM} : {CARD_RANK} : {CARD_SUIT}" }

        for (int i = 0; i < 4; i++) {
            int player = Integer.parseInt(CURRENT_HAND.get(i).substring(0,1));
            int firstColon = CURRENT_HAND.get(i).indexOf(":") + 2;
            int secondColon = CURRENT_HAND.get(i).lastIndexOf(":") + 2;
            int rank =  Integer.parseInt(CURRENT_HAND.get(i).substring(firstColon,secondColon - 3));
            String suit = CURRENT_HAND.get(i).substring(secondColon);

            if (rank > highest && suit.equals(CURRENT_SUIT)) {
                highest = rank;
                winner = player;
            }
        }

        return winner;
    }

    // add cards from current_hand to player if matters
    public void AddCards(Player player) {
        for (int i = 0; i < CURRENT_HAND.size(); i++) {
            int firstColon = CURRENT_HAND.get(i).indexOf(":") + 2;
            int secondColon = CURRENT_HAND.get(i).lastIndexOf(":") + 2;
            int rank =  Integer.parseInt(CURRENT_HAND.get(i).substring(firstColon, secondColon-3));
            String suit = CURRENT_HAND.get(i).substring(secondColon);
            String card = CURRENT_HAND.get(i).substring(firstColon);

            if (    suit.equals("H") ||
                    (suit.equals("D") && rank == 11) ||
                    (suit.equals("S") && rank == 12) ||
                    (suit.equals("C") && rank == 10) ) {
                player.cards.add(card);
            }

        }
    }

    public void RemoveCardsFromHands() {
        RemovePlayerCard(currentPlayer);
        RemovePlayerCard(currentPlayer.next);
        RemovePlayerCard(currentPlayer.next.next);
        RemovePlayerCard(currentPlayer.next.next.next);
    }
    public void RemovePlayerCard(Player player) {
        for (int i = 0; i < CURRENT_HAND.size(); i++) {
            if (player.hand.contains(CURRENT_HAND.get(i).substring(4))) {
                player.hand.remove(player.hand.indexOf(CURRENT_HAND.get(i).substring(4)));
            }
        }
    }
    
    public boolean HasTwoOfClubs(Player player) {
    	if (player.hand.contains("2 : C")) {
    		return true;
    	}
    	return false;
    }
    // Winners for current hand

    public void EndOfPlay(Player player, String card) {
        // Make sure winner goes NEXT
        int winner = HandWinner();

        if (currentPlayer.player_num == winner) {
            // Current player stays
        } else if (currentPlayer.next.player_num == winner) {
            currentPlayer = currentPlayer.next;
        } else if (currentPlayer.next.next.player_num == winner) {
            currentPlayer = currentPlayer.next.next;
        } else if (currentPlayer.next.next.next.player_num == winner) {
            currentPlayer = currentPlayer.next.next.next;
        }

        // Add current cards to player if it matters
        AddCards(currentPlayer);

        RemoveCardsFromHands();
        // RESET HAND, SUIT
        CURRENT_HAND = new ArrayList<String>();
        CURRENT_SUIT = "N";
        
    }

    // Player param is the current player
    public void NotifyPlayers(Player player, String card) {
    	player.next.otherPlayerMoved(card, player.player_num);
    	player.next.next.otherPlayerMoved(card, player.player_num);
    	player.next.next.next.otherPlayerMoved(card, player.player_num);
    }

    // Hands are all empty
    public boolean NoHandsLeft(Player player) {

        if (player.hand.size() + player.next.hand.size() + player.next.next.hand.size() + player.next.next.next.hand.size() == 0) {
            return true;
        }

        return false;
    }


    // Count Scores
    public int GetWinner(Player player) {
        TeamOneScore = 0;
        TeamTwoScore = 0;

        if (TeamOne.contains(player.player_num)) {
            TeamOneScore += PlayerScore(player);
        }

        if (TeamOne.contains(player.next.player_num)) {
            TeamOneScore += PlayerScore(player.next);
        }

        if (TeamOne.contains(player.next.next.player_num)) {
            TeamOneScore += PlayerScore(player.next.next);
        }

        if (TeamOne.contains(player.next.next.next.player_num)) {
            TeamOneScore += PlayerScore(player.next.next.next);
        }

        if (TeamTwo.contains(player.player_num)) {
            TeamTwoScore += PlayerScore(player);
        }

        if (TeamTwo.contains(player.next.player_num)) {
            TeamTwoScore += PlayerScore(player.next);
        }

        if (TeamTwo.contains(player.next.next.player_num)) {
            TeamTwoScore += PlayerScore(player.next.next);
        }

        if (TeamTwo.contains(player.next.next.next.player_num)) {
            TeamTwoScore += PlayerScore(player.next.next.next);
        }

        if (TeamOneScore > TeamOneScore) {
            return 1;
        } else {
            return 2;
        }
    }

    // Count Individual PlayerScore

    public int PlayerScore(Player player) {
        int total = 0;
        int heartscore = 0;
        boolean TenOfClubs = false;
        ArrayList<String> player_cards = player.cards;
        
        Integer[] HeartScores = { 0, -10, -10, -10, -10, -10, -10, -20, -30, -40, -50, -60, -70};
        for (int i = 0; i < player_cards.size(); i++) {

            int firstColon = player_cards.get(i).indexOf(":") - 1;
            int rank = Integer.parseInt(player_cards.get(i).substring(0,firstColon));
            String suit = player_cards.get(i).substring(firstColon + 3);
            
            if (suit.equals("H")) {
                // all hearts?
                heartscore += HeartScores[rank-2];
            } else if (suit.equals("D") && rank == 11) {
                total += 100;
            } else if (suit.equals("S") && rank == 12) {
                total += -100;
            } else if (suit.equals("C") && rank == 10) {
                TenOfClubs = true;
            }
        }
        // CHeck for shooting the moon
        if (heartscore == -330) {
            total += 200;
        } else {
            total += heartscore;
        }

        // Check for Ten of clubs
        if (TenOfClubs && total != 0) {
            total *= 2;
        } else if (TenOfClubs) {
            total = 50;
        }
        return total;
    }

    public synchronized boolean legalMove(String card, Player player) {
        // Player plays a card on suit
    	if (player == currentPlayer && HasTwoOfClubs(player) && !card.equals("2 : C")) {
//    		System.out.println(card);
    		return false;
    	} else if (player == currentPlayer && card.substring(4).contains(CURRENT_SUIT)) {
            // Everyone has played

            if (CURRENT_HAND.size() == 3) {
                CURRENT_HAND.add(currentPlayer.player_num + " : " + card);
                NotifyPlayers(player, card);
                EndOfPlay(player, card);
            } else {
                // Add card to the current hand
                CURRENT_HAND.add(currentPlayer.player_num + " : " + card);

                // move to next player
                currentPlayer = currentPlayer.next;
                
                NotifyPlayers(player, card);	
                currentPlayer.notifyTurn();
            }
            return true;
        } else if (player == currentPlayer && CURRENT_SUIT.equals("N")) {
            // Add new suit
        	int firstColon = card.indexOf(":") + 2;
            CURRENT_SUIT = card.substring(firstColon);

            // Add card to the current hand
            CURRENT_HAND.add(currentPlayer.player_num + " : " + card);

            // move to next player
            currentPlayer = currentPlayer.next;
            
            NotifyPlayers(player, card);
            currentPlayer.notifyTurn();
            return true;
        } else if (player == currentPlayer && !player.HasCurrentSuit()) {

            if (CURRENT_HAND.size() == 3) {
                CURRENT_HAND.add(currentPlayer.player_num + " : " + card);
                NotifyPlayers(player, card);
                EndOfPlay(player, card);
            } else {
                // Add card to the current hand
                CURRENT_HAND.add(currentPlayer.player_num + " : " + card);

                // move to next player
                currentPlayer = currentPlayer.next;
                
                NotifyPlayers(player, card);
                currentPlayer.notifyTurn();
            }

            return true;
        }

//		System.out.println(card);
//		System.out.println(CURRENT_SUIT);
//		System.out.println(player.player_num);
//		System.out.println(currentPlayer.player_num);
        return false;
    }

    class Player extends Thread {
        int player_num;
        ArrayList<String> hand;
        ArrayList<String> cards;
        Player next;
        Socket socket;
        BufferedReader input;
        PrintWriter output;

        /**
         * Constructs a handler thread for a given socket and mark
         * initializes the stream fields, displays the first two
         * welcoming messages.
         */
        public Player(Socket socket, int player_num) {
            ArrayList<String> current_hand = new ArrayList<String>();

            for (int i = 13 * player_num - 13; i < 13 * player_num; i++) {
                current_hand.add(deck[i]);
            }

            this.socket = socket;
            this.player_num = player_num;
            this.hand = current_hand;
            this.cards = new ArrayList<String>();

            try {
                input = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);
                output.println("WELCOME " + player_num);
                output.println("MESSAGE Waiting for other players to connect");
            } catch (IOException e) {
                System.out.println("Player died: " + e);
            }
        }

        public boolean HasCurrentSuit() {
            for (int i = 0; i < hand.size(); i++) {
                if (hand.get(i).contains(CURRENT_SUIT)) {
                    return true;
                }
            }
            return false;
        }
        /**
         * Accepts notification of who the next player is.
         */
        public void ConfigureNextPlayer(Player next) {
            this.next = next;
        }

        /**
         * Handles the otherPlayerMoved message.
         */
        public void otherPlayerMoved(String card, int num) {
            output.println("PLAYER_MOVED " + num + " : " + card);

//            if
//            output.println(
//                    TallyScores() ? "DEFEAT" : NoHandsLeft() ? "TIE" : "");
        }
        public void notifyTurn() {
        	output.println("YOUR_TURN");
        }

        // outputs what player has in their hands
        public void dealPlayerCards(Player player) {
        	output.println("DEALING START ");
//        	for (int i = 0; i < 4; i++) {
//        		if (player.player_num != player_num) {
//        			player = player.next;
//        		}
//        	}
	        	for (int i = 0; i < player.hand.size(); i++) {
	        		output.println("CARD " + player.hand.get(i));
	        	}
        	output.println("DEALING COMPLETE");
        }
        
        // outputs what cards player has
        public void cardsInHand(Player player) {
        	if (player.hand.size() <= 0) {
        		
        	} else {
        		output.println("PLAYER_HAND BEGIN");
	        	for (int i = 0; i < player.hand.size(); i++) {
	        		output.println(player.hand.get(i));
	        	}
        		output.println("PLAYER_HAND END");
        	}
        }
        // outputs what cards player has WON
        public void ownedCards(Player player) {
        	if (player.cards.size() <= 0) {
        		
        	} else {
	        	output.println("OWNED_CARDS BEGIN");
	        	for (int i = 0; i < player.cards.size(); i++) {
	        		output.println(player.cards.get(i));
	        	}
	        	output.println("OWNED_CARDS END");
        	}
        }
        
        public void currentHand(Player player) {
        	output.println("CURRENT_HAND BEGIN");
        	for (int i = 0; i < CURRENT_HAND.size(); i++) {
            	output.println(CURRENT_HAND.get(i));
        	}
        	output.println("CURRENT_HAND END");
        }
        
        /**
         * The run method of this thread.
         */
        public void run() {
            try {
                // The thread is only started after everyone connects.
                output.println("MESSAGE All players connected");

                dealPlayerCards(this);
                // Tell the first player that it is her turn.
                if (player_num == FirstPlayer()) {
                    output.println("YOUR_TURN");
                }
//                String command = "";
                // Repeatedly get commands from the client and process them.
                while (true) {

                    String command = input.readLine();
                    if (command.startsWith("MOVE") && command.contains(":")) {
                        String card = command.substring(5);
                        if (legalMove(card, this)) {
                            output.println("VALID_MOVE");

                            if (NoHandsLeft(currentPlayer)) {
                                // tally scores
                                int team_num = GetWinner(currentPlayer);

                                // Output winners
                                if (TeamOne.contains(player_num) && team_num == 1) {
                                    output.println("WIN : " + TeamOneScore + " > " + TeamTwoScore );
                                } else if (TeamTwo.contains(player_num) && team_num == 2){
                                    output.println("WIN : " + TeamTwoScore + " > " + TeamOneScore );
                                } else if (TeamOne.contains(player_num) && team_num == 2) {
                                    output.println("LOSE : " + TeamOneScore + " < " + TeamTwoScore );
                                } else if (TeamTwo.contains(player_num) && team_num == 1) {
                                    output.println("LOSE : " + TeamTwoScore + " < " + TeamOneScore );
                                }
                            }
                            if (CURRENT_SUIT.equals("N")) {
	                            // Notify Winner
	                            currentPlayer.notifyTurn();
                            }
                        } else {
                            output.println("INVALID_MOVE");
                            output.println("YOUR_TURN");
//                            command = input.readLine();
                            continue;
                        }
                    } else if (command.startsWith("QUIT")) {
                        return;
                    } else if (command.startsWith("HAND?")) {
                    	cardsInHand(this);
                        output.println("YOUR_TURN");
                    } else if (command.startsWith("CARDS?")) {
                    	ownedCards(this);
                        output.println("YOUR_TURN");
                    } else if (command.startsWith("CURRENT_SUIT?")) {
                    	output.println(CURRENT_SUIT);
                    	output.println("YOUR_TURN");
                    } else if (command.startsWith("CURRENT_HAND?")) {
                    	currentHand(this);
                    	output.println("YOUR_TURN");
                    } else if (!command.contains(":")) {
                        output.println("INVALID_MOVE");
                        output.println("YOUR_TURN");
                    }
                }
            } catch (IOException e) {
                System.out.println("Player died: " + e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
