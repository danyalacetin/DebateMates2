/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import connections.ClientConnection;
import utilities.Command;

/**
 *
 * @author Demo
 */
public class Worker implements Runnable {
    
    private ClientConnection client;
    
    private String userID;
    private int matchID;
    
    private final ServerWorker server;
    
    Worker(ClientConnection connection) {
        client = connection;
        client.setWorker(this::handleString);
        server = Server.getInstance();
        userID = null;
        matchID = -1;
    }
    
    public void setConnection(ClientConnection conn) {
       client = conn;
    }
    
    public int getMatchID() {
        return matchID;
    }
    
    public void enterMatch(int id) {
        matchID = id;
    }
    
    public void leaveMatch() {
        matchID = -1;
    }
    
    boolean isLoggedIn() {
        return null !=  userID;
    }
    
    boolean inMatch() {
        return -1 != matchID;
    }
    
    public String getLogin() {
        return userID;
    }
    
    public String getNickname() {
        String nickname = Server.getInstance().getDB().getQuery(userID, "NICKNAME");
        return nickname.equalsIgnoreCase("nickname") ? userID : nickname;
    }
    
    void setLogin(String login) {
        userID = login;
    }
    
    @Override
    public void run() {
        client.run();
    }
    
    void shutdown() {
        client.close();
    }
    
    public void send(Command msg) {
        send(msg.toString());
    }
    
    public void send(String msg) {
        client.send(msg);
    }
    
    public void send(ServerConstants msg) {
        send(msg.toString());
    }

    public void handleString(String input) {
        Command command = Command.create(input, this);
        server.processCommand(command);
    }
}
