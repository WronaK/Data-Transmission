package server;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread implements Observer {
    private Socket clientSocket;
    private SocketServer socketServer;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public ServerThread(Socket socket, SocketServer socketServer) {
        this.clientSocket = socket;
        this.socketServer = socketServer;
    }

    public void run() {
        try {
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            dataInputStream = new DataInputStream(clientSocket.getInputStream());
            while (true) {
                String nick = dataInputStream.readUTF();
                int size = dataInputStream.readInt();
                byte[] inputLine = new byte[size];
                dataInputStream.readFully(inputLine, 0, size);
                socketServer.notifyObservers(nick, inputLine);
            }
        } catch (IOException e) {
            socketServer.unregisterObserver(this);
        }
    }

    @Override
    public void notify(String nick, byte[] bytes) {
        try {
            dataOutputStream.writeUTF(nick);
            dataOutputStream.writeInt(bytes.length);
            dataOutputStream.write(bytes, 0, bytes.length);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
