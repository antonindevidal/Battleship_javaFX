package network;

import network.NetworkPackageCoordinates;

import java.io.*;
import java.net.Socket;

/**
 * manage connections with a client and another ServerConnection
 */
public class ServerConnection implements Runnable{

    /**
     * Input of data from a client
     */
    private DataInputStream input;

    /**
     * Output of data from a client
     */
    private DataOutputStream output;

    /**
     * Output of an object from a client
     */
    private ObjectOutputStream objOutput;

    /**
     * Input of an object from a client
     */
    private ObjectInputStream objInput;

    /**
     * id of the player it communicate with
     */
    private int playerId;

    /**
     * ServerConnection of the other player
     */
    private ServerConnection otherPlayer;


    /**
     * Constructor
     * @param socket socket of the server
     * @param playerId  id of the player it communicate with
     */
    public ServerConnection(Socket socket, int playerId) {
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

    /**
     * Read from his objInput and send to the other ServerConnection
     */
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
                    System.out.println("oui");
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


    /**
     * Send coordinate to the other player
     * @param c object to send
     */
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

    /**
     * End connection and send signal to client
     */
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
