package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private String ipAdress ="localhost";
    private int port = 4444;
    private ServerSocket ss;

    private int nbPlayer = 0;
    private ServerConnection player1;
    private ServerConnection player2;

    public int getServerPort()
    {
        return ss.getLocalPort();
    }

    public Server(int port)
    {

        try {
            this.port = port;
            ss = new ServerSocket(port);

        }catch (IOException e)
        {
            System.out.println("Server creation problem");
        }
    }


    public void ClientConnection()
    {
        while (nbPlayer < 2) // Waiting for 2 players connected
        {
            try {
                Socket s = ss.accept(); // Wait for a player
                nbPlayer++;
                System.out.println("Player "+ nbPlayer+" is connected");

                ServerConnection sc = new ServerConnection(s,nbPlayer); //Create a connexion for the player


                if(nbPlayer == 1) player1 = sc;
                else if (nbPlayer ==2) player2 = sc;


                Thread  t = new Thread(sc);
                t.start(); // Run the connexion in a new thread to run in parallel

            } catch (IOException e) {
                System.out.println("Server can't connect a client");
            }

        }

        player1.setOtherPlayer(player2);
        player2.setOtherPlayer(player1);
    }


}
