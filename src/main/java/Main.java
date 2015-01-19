package main.java;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main extends HttpServlet {
	  private static final long serialVersionUID = 1L;
//public class Main {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
            startServer(req, resp);

    }

    private void startServer(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
//	  private void startServer() throws IOException {
        // declaration section:
        // declare a server socket and a client socket for the server
        // declare an input and an output stream
        ServerSocket listener = null;
        Socket clientSocket = null;

        System.out.println("Hearts Server is Running");
        // Try to open a server socket on port 9001
        // Note that we can't choose a port less than 1023 if we are not
        // privileged users (root)
        try {
            listener = new ServerSocket(9001);
        } catch (IOException e) {
            System.out.println(e);
        }
        // Create a socket object from the ServerSocket to listen and accept
        // connections.
        // Open input and output streams
        try {
            while (true) {
                // Create game and players hooked up with sockets
                Game game = new Game();
                Game.Player player1 = game.new Player(listener.accept(), 1);
                Game.Player player2 = game.new Player(listener.accept(), 2);
                Game.Player player3 = game.new Player(listener.accept(), 3);
                Game.Player player4 = game.new Player(listener.accept(), 4);

                // Configure turns
                player1.ConfigureNextPlayer(player2);
                player2.ConfigureNextPlayer(player3);
                player3.ConfigureNextPlayer(player4);
                player4.ConfigureNextPlayer(player1);

                // Configure which player goes first
                switch (game.FirstPlayer()) {
                    case 1:
                        game.currentPlayer = player1;
                        break;
                    case 2:
                        game.currentPlayer = player2;
                        break;
                    case 3:
                        game.currentPlayer = player3;
                        break;
                    case 4:
                        game.currentPlayer = player4;
                        break;
                }

                // Deal Hands to Players
                
                
                // start all player threads
                player1.start();
                player2.start();
                player3.start();
                player4.start();
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            listener.close();
        }

    }

    public static void main(String[] args) throws Exception {
//        Server server = new Server(Integer.valueOf(System.getenv("PORT")));
//        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
//        context.setContextPath("/");
//        server.setHandler(context);
//        context.addServlet(new ServletHolder(new Main()), "/*");
//        server.start();
//        server.join();
    }
}
