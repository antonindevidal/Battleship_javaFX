package network;

import game.Manager.NetworkManager;
import network.NetworkPackageCoordinates;
import javafx.application.Platform;
import view.GameViewNetwork;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Client side of a netxork game, every player has a Client
 */
public class Client {

    /**
     * Socket connected to a ServerConnection
     */
    private Socket socket;

    /**
     * Input of data from serverConnection
     */
    private DataInputStream input;
    /**
     * Output of data from serverConnection
     */
    private DataOutputStream output;

    /**
     * Input of an object from serverConnection
     */
    private ObjectInputStream objInput;

    /**
     * Output of an object from serverConnection
     */
    private ObjectOutputStream objOutput;

    /**
     * true if the client can send a message trough an output
     */
    private boolean canSendMessage = true;

    /**
     * Manager of the current game
     */
    private NetworkManager game;

    /**
     * controller of the gameView
     */
    private GameViewNetwork gv;



    public NetworkManager getGame() {
        return game;
    }

    public void setGv(GameViewNetwork gv) {
        this.gv = gv;
    }


    /**
     * Constructor
     * @param address address of the server(ip)
     * @param port  port of the server
     * @throws IOException throw if there is a problem to instantiate the client
     */
    public Client(String address, int port ) throws IOException { //throw an IOException -> can't connect to server
        int playerId;


        socket = new Socket(address, port); // socket to connect with client

        // Create all input and output to talk
        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());


        objOutput = new ObjectOutputStream(socket.getOutputStream());
        objInput = new ObjectInputStream(socket.getInputStream());
        playerId = input.readInt(); // read from the ServerConnection, the player id (1 or 2)


        game = new NetworkManager(playerId);

    }


    /**
     * Send a NetworkPackageCoordinates to the serverConnection
     * @param x x coordinate to send
     * @param y y coordinate to send
     * @param horizontal orientation of the boat ( true if horizontal)
     */
    public void sendCoordinates(int x, int y, boolean horizontal)
    {
        if (!canSendMessage) // cant send message
            return;
        try {

            NetworkPackageCoordinates c = new NetworkPackageCoordinates(x,y,horizontal);
            objOutput.writeObject(c); // write on the socket the package
            objOutput.flush(); // flush output to avoid problems



        } catch (IOException e) {
            System.out.println("Error when sending message to server");
        }
    }

    /**
     * Receive coordinates from the serverConnection -  run in a separate thread
     */
    public void receiveCoordinates() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {


                    NetworkPackageCoordinates npc = null;
                    try {
                        npc = (NetworkPackageCoordinates) objInput.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println("Cant read message from the server");
                        break;
                    }
                    if (npc != null)
                    {

                        if(npc.getX() == -1 && npc.getY() ==-1 && !npc.isHorizontal())
                        {
                            // Mesage from other player meaning end of connexion
                            break; // End connexion
                        }
                        else
                        {
                            // Call game manager to perform action
                            game.otherPlayerPlaceBoat(npc.getX(),npc.getY(),npc.isHorizontal());
                            game.otherPlayerShoot(npc.getX(),npc.getY());
                            if(game.isEnding())
                            {
                                gv.setRestartButtonVisible();

                            }

                        }

                    }
                }
                //End of connexion
                canSendMessage = false;
                Platform.runLater(() -> {
                        game.erreurConnexion(); // Display error message
                        gv.setRestartButtonVisible();//  Set menu button visible
                });
            }
        });
        t.start();

    }




}
