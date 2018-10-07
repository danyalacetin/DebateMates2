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

/**
 *
 * @author Demo
 */
class Worker implements Runnable {
    
    private final Socket client;
    private final PrintWriter outStream;
    private final BufferedReader inStream;
    
    private String userID;
    private int matchID;
    
    private final ServerWorker server;
    
    Worker(Socket clientSocket) throws IOException {
        client = clientSocket;
        server = Server.getInstance();
        
        outStream = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(client.getOutputStream())));
        inStream = new BufferedReader(new InputStreamReader(client.getInputStream()));
        userID = null;
        matchID = -1;
    }
    
    int getMatchID() {
        return matchID;
    }
    
    void enterMatch(int id) {
        matchID = id;
    }
    
    void leaveMatch() {
        matchID = -1;
    }
    
    boolean isLoggedIn() {
        return null !=  userID;
    }
    
    boolean inMatch() {
        return -1 != matchID;
    }
    
    String getLogin() {
        return userID;
    }
    
    private void logout() {
        if (isLoggedIn())
            handleInput("logout");
    }
    
    void setLogin(String login) {
        userID = login;
    }
    
    @Override
    public void run() {
        try {
            handleClient();
        } catch (IOException ex) {
            System.err.println("Connection Closed");
        } finally {
            handleInput("disconnect");
            server.serverLog("Disconnected: " + client.getInetAddress());
        }
    }
    
    void shutdown() {
        try {
            logout();
            client.close(); 
        } catch (IOException e) {

        }
    }
    
    private void handleInput(String cmdString) {
        Command command = new Command(cmdString, this);
        server.processCommand(command);
    }
    
    void handleClient() throws IOException {
        String line;
        send(ServerConstants.CONNECTED);
        
        while((line = inStream.readLine()) != null) {
//            server.serverLog("CLIENT SENT: " +  line);
            handleInput(line);
        }
    }
    
    void send(Command msg) {
        if (msg.getSource().getLogin().equals(userID)) {
            String msgString = msg.toString();
            if (msgString.contains(userID))
                send(msgString.replace(userID, "You"));
        }
        else {
            send(msg.toString());
        }
    }
    
    void send(String msg) {
        outStream.println(msg);
        outStream.flush();
    }
    
    void send(ServerConstants msg) {
        send(msg.toString());
    }
}