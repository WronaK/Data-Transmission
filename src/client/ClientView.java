package client;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static javax.swing.ScrollPaneConstants.*;

public class ClientView extends JFrame {
    private JTextField inputMessage = new JTextField();
    private JButton sendImage = new JButton("Wyślij obraz");
    private JButton sendSound = new JButton("Wyślij dżwięk");
    private JButton sendText = new JButton("Wyślij tekst");
    private JTextArea messageArea = new JTextArea();

    public ClientView() {
        JScrollPane messagePanel = new JScrollPane(messageArea, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
        getContentPane().add(messagePanel, BorderLayout.PAGE_START);
        messageArea.setEnabled(false);
        messageArea.setDisabledTextColor(Color.BLACK);
        messagePanel.setPreferredSize(new Dimension(600, 500));
        sendImage.setPreferredSize(new Dimension(200, 30));
        sendSound.setPreferredSize(new Dimension(200, 30));
        sendText.setPreferredSize(new Dimension(200, 30));
        getContentPane().add(sendText, BorderLayout.CENTER);
        getContentPane().add(sendSound, BorderLayout.WEST);
        getContentPane().add(sendImage, BorderLayout.EAST);
        getContentPane().add(inputMessage, BorderLayout.PAGE_END);
        pack();
    }

    public void addText(String nick, String messege) {
        messageArea.append(nick + ": " + messege + "\n");
        pack();
    }

    public void addImage(String nick, BufferedImage image) {
        messageArea.append(nick + ": " + "Wysłano obraz.\n");
        pack();

        JLabel label = new JLabel();
        label.setIcon(new ImageIcon((BufferedImage) image));
        JFrame frame = new JFrame("Obraz otrzymany od " + nick);
        frame.getContentPane().add(label, BorderLayout.CENTER);
        frame.setVisible(true);
        frame.pack();
    }

    public void addSound(String nick, AudioInputStream sound) {
        messageArea.append(nick + ": " + "Wysłano plik dżwiękowy.\n");
        pack();

        Button play = new Button("Kilknij, aby odsłuchać");
        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Clip clip = null;
                try {
                    clip = AudioSystem.getClip();
                    clip.open(sound);

                    clip.setMicrosecondPosition(0);
                    clip.start();

                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        JFrame frame = new JFrame("Plik dźwiękowy otrzymany od " + nick);
        frame.setPreferredSize(new Dimension(400, 200));
        frame.setResizable(false);
        frame.getContentPane().add(play, BorderLayout.CENTER);
        frame.setVisible(true);
        frame.pack();
    }

    public void addSendButtonListener(ActionListener actionListener) {
        sendSound.addActionListener(actionListener);
        sendImage.addActionListener(actionListener);
        sendText.addActionListener(actionListener);
    }

    public JButton getAddImage() {
        return sendImage;
    }

    public JButton getAddFileVoid() {
        return sendSound;
    }

    public JButton getSendButton() {
        return sendText;
    }

    public String getMessageText() {
        String mm = inputMessage.getText();
        inputMessage.setText("");
        pack();
        return mm;
    }
}
