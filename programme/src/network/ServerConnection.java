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
            //Create all input and output to communicate
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
    public void run()  {
        try {
            output.writeInt(playerId); // Send id to the client
            output.flush();
            while(true)
            {
                if(Thread.interrupted())
                {
                    System.out.println("ServerConnexion interrupted");
                    break;
                }
                NetworkPackageCoordinates c = null;


                try {
                    c = (NetworkPackageCoordinates) objInput.readObject(); // Try to read on his object input what the client sent
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("ServerConnection can't read in his object input");
                    break;
                }
                if (c != null)
                {
                    sendCoordinatesToOtherPlayer(c); // redirect the input to the other client
                    c=null;
                }
            }

        }catch (IOException e)
        {
            System.out.println("Exception in serverConnection of player "+ playerId);
        }

    }



    private void sendCoordinatesToOtherPlayer(NetworkPackageCoordinates c)
    {
        try {
            otherPlayer.objOutput.writeObject(c); // send message to the other client
            otherPlayer.objOutput.flush();
        } catch (Exception e) {
            endClient();
            System.out.println("ServerConnexion can't send a message to the other player");
        }

    }
    private void endClient()
    {
        try {
            objOutput.writeObject(new NetworkPackageCoordinates(-1,-1,false)); // send an end message
        }catch (Exception e)
        {
            System.out.println("ServerConnection can't send the end message to his client");
        }
    }

}
