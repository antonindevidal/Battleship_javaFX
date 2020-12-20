package network;

import game.NetworkPackageCoordinates;

import java.io.*;
import java.net.Socket;

public class ServerConnection implements Runnable{

    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;


    private ObjectOutputStream objOutput;
    private ObjectInputStream objInput;

    private int playerId;

    private ServerConnection otherPlayer;


    public ServerConnection(Socket socket, int playerId) {
        this.socket = socket;
        this.playerId = playerId;

        try {
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
            objOutput = new ObjectOutputStream(socket.getOutputStream());
            objInput = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            System.out.println("Problem to get ServerConnection Streams");
        }
    }

    public void setOtherPlayer(ServerConnection sc)
    {
        otherPlayer = sc;
    }

    @Override
    public void run() {
        try {
            output.writeInt(playerId);
            output.flush();
            while(true)
            {
                NetworkPackageCoordinates c = null;

                try {
                    c = (NetworkPackageCoordinates) objInput.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (c != null)
                {
                    sendCoordinatesToOtherPlayer(c);
                    System.out.println("Receive from player "+playerId+" : " + c);
                    c=null;
                }
            }

        }catch (IOException e)
        {
            System.out.println("Exception in serverConnection of player "+ playerId);
        }
    }


    private void sendToOtherPlayer(int i)
    {
        try {
            otherPlayer.output.writeInt(i);
            otherPlayer.output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void sendCoordinatesToOtherPlayer(NetworkPackageCoordinates c)
    {
        try {
            otherPlayer.objOutput.writeObject(c);
            otherPlayer.objOutput.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
