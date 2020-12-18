package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class SocketServer {
    private ServerSocket serverSocket;

    private static List<ServerThread> clients = new ArrayList<>();

    public void registerObserver(ServerThread client) {
       clients.add(client);
    }

    public void unregisterObserver(ServerThread client) {
        clients.remove(client);
    }

    public void notifyObservers(String nick, byte[] bytes) {
        for(Observer client: clients) {
            client.notify(nick, bytes);
        }
    }

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
             while (true) {
                 ServerThread serverThread = new ServerThread(serverSocket.accept(), this);
                 registerObserver(serverThread);
                 serverThread.start();

             }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SocketServer socketServer = new SocketServer();
        socketServer.start(5293);
    }
}
