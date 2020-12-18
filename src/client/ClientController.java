package client;

import javax.sound.sampled.AudioInputStream;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class ClientController {
    private ClientView clientView;
    private ClientModel clientModel;

    public ClientController(ClientView clientView, ClientModel clientModel) {
        this.clientView = clientView;
        this.clientModel = clientModel;

        this.clientView.addSendButtonListener(new SendButtonListener());
    }

    public void startConnection(String ip, int port) {
         try {
             setNick();
             clientModel.setClientSocket(new Socket(ip, port));
             clientModel.setDataOutputStream(new DataOutputStream(clientModel.getClientSocket().getOutputStream()));
             clientModel.setDataInputStream(new DataInputStream(clientModel.getClientSocket().getInputStream()));

            while (true) {
                String nick = clientModel.getDataInputStream().readUTF();
                int size = clientModel.getDataInputStream().readInt();
                byte[] inputLine = new byte[size];
                clientModel.getDataInputStream().readFully(inputLine, 0, size);
                setMessage(nick, inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setMessage(String nick, byte[] bytes) {
        ChainElement chainElement = new TextElement();
        Object object = chainElement.handleRequestDeserializable(bytes);
        if(object instanceof String) {
            clientView.addText(nick, (String) object);
        } else if(object instanceof BufferedImage) {
            clientView.addImage(nick, (BufferedImage) object);
        } else if(object instanceof AudioInputStream) {
            clientView.addSound(nick, (AudioInputStream) object);
        }
    }

    private void setNick() {
        String nick = JOptionPane.showInputDialog("Podaj swój Nick:");
        if((nick != null) && (nick.length() > 0)) {
            clientView.setTitle(nick);
            clientModel.setNick(nick);
        } else {
            clientView.setTitle("Użytkownik");
            clientModel.setNick("Użytkownik");
        }
    }

    class SendButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Object object = actionEvent.getSource();
            ChainElement chainElement = new TextElement();
            if(object == clientView.getSendButton()) {
                String text = clientView.getMessageText();
                byte[] bytes = chainElement.handleRequestSerializable(0, text);
                clientModel.sendMessage(bytes);
            } else if(object == clientView.getAddImage()) {
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Images","bmp");
                displayFileChooser("Pliki \'bmp\'", filter, chainElement, 1);
            } else if(object == clientView.getAddFileVoid()) {
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Sound files", "wav");
                displayFileChooser("Pliki \'wav\'", filter, chainElement, 2);
            }
        }

        private void displayFileChooser(String title, FileNameExtensionFilter fileNameExtensionFilter, ChainElement chainElement, int type) {
            JFileChooser jFileChooser= new JFileChooser();
            jFileChooser.setDialogTitle(title);
            jFileChooser.setAcceptAllFileFilterUsed(false);
            jFileChooser.addChoosableFileFilter(fileNameExtensionFilter);
            if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = jFileChooser.getSelectedFile();
                JOptionPane.showMessageDialog(null, "Wysyłasz plik: " + file.getAbsolutePath());
                byte[] bytes = chainElement.handleRequestSerializable(type, file);
                clientModel.sendMessage(bytes);
            }
        }
    }

}
