package network;

import game.NetworkGame;
import game.NetworkPackageCoordinates;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private String ipAdress = "localhost";
    private int port = 4444;
    private DataInputStream input;
    private DataOutputStream output;

    private ObjectInputStream objInput;
    private ObjectOutputStream objOutput;

    private int playerId;
    private NetworkGame game;

    public DataInputStream getInput() {
        return input;
    }

    public DataOutputStream getOutput() {
        return output;
    }

    public NetworkGame getGame() {
        return game;
    }

    public int getPlayerId() {
        return playerId;
    }

    public Client(String address, int port) {

        ipAdress = address;
        this.port = port;
        System.out.println("-----------Client------------");
        try {
            socket = new Socket(ipAdress, port);
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());


            objOutput = new ObjectOutputStream(socket.getOutputStream());
            objInput = new ObjectInputStream(socket.getInputStream());
            playerId = input.readInt();

            System.out.println("You are player nb " + playerId);
        } catch (IOException e) {
            System.out.println("Erreur connection client");
        }
        game = new NetworkGame(playerId);

    }


    public void receiveNums() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    int n = -1;
                    try {
                        n = input.readInt();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (n != -1)
                        System.out.println("Receive from other player : " + n);
                }
            }
        });
        t.start();
    }

    public void sendNums() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Scanner scanner = new Scanner(System.in);

                while (true) {
                    try {
                        int n = scanner.nextInt();
                        getOutput().writeInt(n);
                        getOutput().flush();
                        System.out.println("Player " + getPlayerId() + " sent " + n);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void sendCoordinates() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                String str = scanner.nextLine();
                String[] splited = str.split("\\s+");

                if (splited.length == 3) {
                    NetworkPackageCoordinates c = new NetworkPackageCoordinates(Integer.parseInt(splited[0]), Integer.parseInt(splited[1]), Boolean.parseBoolean(splited[2]));
                    game.placeMyBoat(c.getX(),c.getY(),c.isHorizontal());

                    objOutput.writeObject(c);
                    objOutput.flush();
                    System.out.println("Player " + getPlayerId() + " sent {x: " + splited[0] + ", y: " + splited[1] + "}");
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void sendCoordinates(int x, int y, boolean horizontal)
    {
        try {

            NetworkPackageCoordinates c = new NetworkPackageCoordinates(x,y,horizontal);
            game.placeMyBoat(c.getX(),c.getY(),c.isHorizontal());
            objOutput.writeObject(c);
            objOutput.flush();
            System.out.println("Player " + getPlayerId() + " sent {x: " + x + ", y: " + y + "} Horizontal: "+horizontal);



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveCoordinates() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    NetworkPackageCoordinates c = null;
                    try {
                        c = (NetworkPackageCoordinates) objInput.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (c != null)
                    {
                        System.out.println("Receive from other player : " + c);
                        game.otherPlayerPlaceBoat(c.getX(),c.getY(),c.isHorizontal());
                        game.otherPlayerShoot(c.getX(),c.getY());
                        game.isEnding();
                        c=null;
                    }
                }
            }
        });
        t.start();

    }


}
