package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {


    private ServerSocket ss;

    private int nbPlayer = 0;


    public int getServerPort()
    {
        return ss.getLocalPort();
    }

    public Server(int port)
    {

        try {
            ss = new ServerSocket(port);

        }catch (IOException e)
        {
            System.out.println("Server creation problem");
        }
    }


    public void ClientConnection()
    {
        ServerConnection player1 = null;
        ServerConnection player2 = null;
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
        if (player1 !=null  && player2 != null) {
            player1.setOtherPlayer(player2);
            player2.setOtherPlayer(player1);
        }
    }


}
