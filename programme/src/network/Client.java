package network;

import game.Manager.NetworkManager;
import game.NetworkPackageCoordinates;
import javafx.application.Platform;
import view.GameViewNetwork;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private String ipAdress = "localhost";

    private DataInputStream input;
    private DataOutputStream output;

    private int port;

    private ObjectInputStream objInput;
    private ObjectOutputStream objOutput;

    private boolean canSendMessage = true;

    private int playerId;
    private NetworkManager game;
    private GameViewNetwork gv;

    public DataInputStream getInput() {
        return input;
    }

    public DataOutputStream getOutput() {
        return output;
    }

    public NetworkManager getGame() {
        return game;
    }

    public void setGv(GameViewNetwork gv) {
        this.gv = gv;
    }

    public int getPlayerId() {
        return playerId;
    }

    public Client(String address, int port ) throws IOException { //throw an IOException -> can't connect to server

        ipAdress = address;
        this.port = port;


        socket = new Socket(ipAdress, port); // socket to connect with client

        // Create all input and output to talk
        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());


        objOutput = new ObjectOutputStream(socket.getOutputStream());
        objInput = new ObjectInputStream(socket.getInputStream());
        playerId = input.readInt(); // read from the ServerConnection, the player id (1 or 2)


        game = new NetworkManager(playerId);

    }





    ///
    // Send coordinates with standard input
    // coordinates must be on this pattern: "x y true/false" and x and y must be beetween 0 and 9 included
    public void sendCoordinates() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                String str = scanner.nextLine(); // Scan the standard input
                String[] splited = str.split("\\s+"); // Split the message in 3 with spaces

                if (splited.length == 3) {
                    NetworkPackageCoordinates c = new NetworkPackageCoordinates(Integer.parseInt(splited[0]), Integer.parseInt(splited[1]), Boolean.parseBoolean(splited[2]));
                    game.placeMyBoat(c.getX(),c.getY(),c.isHorizontal());

                    objOutput.writeObject(c);
                    objOutput.flush();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //Send coordinates  other player
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
                            game.isEnding();
                        }

                    }
                }
                //End of connexion
                canSendMessage = false;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        game.erreurConnexion(); // Display error message
                        gv.setRestartButtonVisible();//  Set menu button visible
                    }
                });
            }
        });
        t.start();

    }




}
