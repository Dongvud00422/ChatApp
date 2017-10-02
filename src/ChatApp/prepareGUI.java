/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ChatApp;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.*;

/**
 *
 * @author dongvu
 */
public class prepareGUI extends JFrame {

    private JPanel chatHistory;
    private JTextField txtinput;
    private JButton btnSend;

    private Socket socket;
    private DataInputStream streamIn;
    private DataOutputStream streamOut;
    private static int marginTop = 10;
    private static int countOut = 0;
    private ArrayList<JLabel> outbox = new ArrayList<>();
    private ArrayList<JLabel> inbox = new ArrayList<>();

    public prepareGUI(String name, String serverName, int port) {
        this.setTitle(name);
        this.setSize(500, 600);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setVisible(true);

        chatHistory = new JPanel();
        
        
        txtinput = new JTextField();
        btnSend = new JButton("Send");

        chatHistory.setBounds(20, 20, 460, 460);
        chatHistory.setLayout(null);
        chatHistory.setBackground(Color.yellow);
       
        txtinput.setBounds(20, 510, 380, 40);
        btnSend.setBounds(410, 510, 70, 40);

      
        
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Gui text den server khi an button va reset textField.

                    if (!txtinput.getText().isEmpty()) {
                        outbox.add(new JLabel(txtinput.getText()));
                        outbox.get(countOut).setBounds(10, marginTop, 440, 20);
                        outbox.get(countOut).setForeground(Color.blue);
                        chatHistory.add(outbox.get(countOut));
                        streamOut.writeUTF(txtinput.getText());
                        streamOut.flush();
                        marginTop += 25;
                        countOut++;
                        txtinput.setText("");
                    }

                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });

        this.add(txtinput);
        this.add(btnSend);
        this.add(chatHistory);
        System.out.println("Establishing connection. Please wait ...");
        try {
            // Tao socket de ket noi den server.
            socket = new Socket(serverName, port);
            System.out.println("Connected " + socket);
            // Mo luong tu server -> app.
            streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            // Mo luong tu app -> server.
            streamOut = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        String line = "";
        int countInt = 0;
        while (!line.equals(".exit")) {
            try {
                line = streamIn.readUTF();
                System.out.println(line);

                inbox.add(new JLabel(line));
                inbox.get(countInt).setBounds(10, marginTop, 440, 20);
                inbox.get(countInt).setForeground(Color.red);
                chatHistory.add(inbox.get(countInt));
                marginTop += 25;
                countInt++;

            } catch (IOException | NullPointerException ex) {
                System.out.println(ex.getMessage());
                break;
            }
        }
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
