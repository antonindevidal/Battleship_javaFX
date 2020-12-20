package console;

import network.Server;

public class ServerConsole {
    public static void main(String[] args) {
        Server s = new Server();
        s.ClientConnection();
    }
}
