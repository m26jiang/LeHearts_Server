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
    Integer[] HeartScores = { 0, -10, -10, -10, -10, -10, -10, -20, -30, -40, -50, -60, -70};
    ArrayList<Integer> TeamOne, TeamTwo;

    int SUITS = suit.length;
    int RANKS = rank.length;
    int N = SUITS * RANKS;

    String CURRENT_SUIT;

    String[] deck = new String[N];

    public Game() {
        InitializeDeck();
        ShuffleCards();
        DetermineTeams();

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
    // Count Scores
//    public int tallyScores() {
//
//        return ;
//    }

    // Winners

    public void EndOfHand() {
        CURRENT_SUIT = "N";
    }


    // Hands are all empty
    public boolean NoHandsLeft() {
        for (int i = 0; i < board.length; i++) {
            if (board[i] == null) {
                return false;
            }
        }
        return true;
    }

    public synchronized boolean legalMove(String card, Player player) {
        if (player == currentPlayer && card.substring(4).equals(CURRENT_SUIT)) {
//            currentPlayer.otherPlayerMoved(location);
            return true;
        } else if (player == currentPlayer && CURRENT_SUIT.equals("N")) {
            return true;
        }
        return false;
    }

    class Player extends Thread {
        int player_num;
        ArrayList<String> hand;
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
                current_hand.add(deck[player_num]);
            }

            this.socket = socket;
            this.player_num = player_num;
            this.hand = current_hand;

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

        /**
         * Accepts notification of who the opponent is.
         */
        public void ConfigureNextPlayer(Player next) {
            if (player_num == 4) {
                this.next =
            }

        }

        /**
         * Handles the otherPlayerMoved message.
         */
        public void otherPlayerMoved(String card) {
            output.println("PLAYER_MOVED " + card);
//            if
//            output.println(
//                    TallyScores() ? "DEFEAT" : NoHandsLeft() ? "TIE" : "");
        }

        /**
         * The run method of this thread.
         */
        public void run() {
            try {
                // The thread is only started after everyone connects.
                output.println("MESSAGE All players connected");

                // Tell the first player that it is her turn.
                if (player_num == FirstPlayer()) {
                    output.println("MESSAGE Your move");
                }

                // Repeatedly get commands from the client and process them.
                while (true) {
                    String command = input.readLine();
                    if (command.startsWith("MOVE")) {
                        String card = command.substring(5);
                        if (legalMove(card, this)) {
                            output.println("VALID_MOVE");
                            output.println(hasWinner() ? "VICTORY"
                                    : NoHandsLeft() ? "TIE"
                                    : "");
                        } else {
                            output.println("MESSAGE ?");
                        }
                    } else if (command.startsWith("QUIT")) {
                        return;
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
