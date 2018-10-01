/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.Consumer;

/**
 *
 * @author Demo
 */
class Worker implements Runnable {
    
    private final Socket client;
    private final PrintWriter outStream;
    private final BufferedReader inStream;
    
    private final Consumer<Command> requestHandler;
    private String userID;
    private int chatRoomID;
    
    
    
    Worker(Socket clientSocket, Consumer<Command> requestHandler) throws IOException {
        client = clientSocket;
        this.requestHandler = requestHandler;
        
        outStream = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(client.getOutputStream())));
        inStream = new BufferedReader(new InputStreamReader(client.getInputStream()));
        userID = null;
    }
    
    int getChatRoom() {
        return chatRoomID;
    }
    
    void setChatRoom(int id) {
        chatRoomID = id;
    }
    
    void leaveChatRoom() {
        chatRoomID = -1;
    }
    
    void setLogin(String login) {
        userID = login;
    }
    
    @Override
    public void run() {
        try {
            handleClient(); // this is finishing instead of throwing exception
        } catch (IOException ex) {
            System.out.println("hello");
            handleInput("logout " + userID);
            handleInput("serverlog Disconnected: " + client);
            try {
                client.close(); 
            } catch (IOException e) {
                
            }
        }
    }
    
    private void handleInput(String cmdString) {
        Command command = new Command(cmdString, this);
        requestHandler.accept(command);
    }
    
    void handleClient() throws IOException {
        
        String line;
        
        send(StringCommands.CONNECTED);
        while((line = inStream.readLine()) != null) {
            handleInput(line);
        }
    }
    
    void send(Command msg) {
        send(msg.toString());
    }
    
    void send(String msg) {
        outStream.println(msg);
        outStream.flush();
    }
    
    void send(StringCommands msg) {
        send(msg.toString());
    }
}