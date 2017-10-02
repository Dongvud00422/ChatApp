/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Nam Nguyen
 */
public class ChatServer {

    private Socket socket;
    private ServerSocket server;
    private DataInputStream streamIn;
    private DataOutputStream streamOut;

    public ChatServer(int port) {
        try {
            System.out.println("Binding to port " + port + ", please wait ...");
            // Tao serverSocket moi.
            server = new ServerSocket(port);
            System.out.println("Server started: " + server);
            System.out.println("Waiting for a client ...");
            // Cho phep su dung server.
            socket = server.accept();
            System.out.println("Client accepted: " + socket);

            streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            streamOut = new DataOutputStream(socket.getOutputStream());
            boolean done = false;
           
            while (!done) {
                try {

                    String line = streamIn.readUTF();
                    System.out.println(line);
                    streamOut.writeUTF(new java.util.Scanner(System.in).nextLine());
                    streamOut.flush();
                    done = line.equals(".exit");
                } catch (IOException e) {
                    done = true;
                }
            }
            if (socket != null) {
                socket.close();
            }
            if (streamIn != null) {
                streamIn.close();
            }

        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    public static void main(String[] args) {
        ChatServer chatSv = new ChatServer(3333);
    }

}
