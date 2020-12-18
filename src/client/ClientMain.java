package client;

import javax.swing.*;

public class ClientMain {
    public static void main(String[] args) {
       ClientModel clientModel = new ClientModel();
       ClientView clientView = new ClientView();

       clientView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       clientView.setVisible(true);
       clientView.setResizable(false);
       ClientController clientController = new ClientController(clientView, clientModel);
       clientController.startConnection("localhost", 5293);
    }
}
