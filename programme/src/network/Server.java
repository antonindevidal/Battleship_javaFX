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



    public Server()
    {
        System.out.println("--------Serveur---------");

        try {
            ss = new ServerSocket(port);

        }catch (IOException e)
        {
            System.out.println("Problème création serveur");
        }
    }


    public void ClientConnection()
    {
        while (nbPlayer < 2)
        {
            try {
                Socket s = ss.accept();
                nbPlayer++;
                System.out.println("Player "+ nbPlayer+" is connected");

                ServerConnection sc = new ServerConnection(s,nbPlayer);


                if(nbPlayer == 1) player1 = sc;
                else if (nbPlayer ==2) player2 = sc;


                Thread  t = new Thread(sc);
                t.start();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        player1.setOtherPlayer(player2);
        player2.setOtherPlayer(player1);
    }


}
