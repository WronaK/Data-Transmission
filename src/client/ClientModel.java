package client;

import java.io.*;
import java.net.Socket;

public class ClientModel {
    private Socket clientSocket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private String nick;

    public void sendMessage(byte[] bytes) {
        try {
            dataOutputStream.writeUTF(nick);
            dataOutputStream.writeInt(bytes.length);
            dataOutputStream.write(bytes, 0, bytes.length);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void setDataOutputStream(DataOutputStream dataOutputStream) {
        this.dataOutputStream = dataOutputStream;
    }

    public void setDataInputStream(DataInputStream dataInputStream) {
        this.dataInputStream = dataInputStream;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
