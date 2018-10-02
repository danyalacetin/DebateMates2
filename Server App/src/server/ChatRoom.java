/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Demo
 */
class ChatRoom {
    private final int id;
    
    private final int maxMembers;
    private final List<Worker> members;
    private final ReentrantLock membersLock;
    private final ReentrantLock historyLock;
    
    private final List<Command> history;
    private final IdManager idManager;
    private final ServerWorker server;
    
    ChatRoom(int id, int max) {
        this.id = id;
        
        members = new ArrayList<>();
        membersLock = new ReentrantLock();
        historyLock = new ReentrantLock();
        history = new ArrayList<>();
        idManager = new IdManager();
        
        server = Server.getInstance();
        maxMembers = max;
    }
    
    boolean isFull() {
        return maxMembers == members.size();
    }
    
    void storeMessage(Command msg) {
        historyLock.lock();
        try {
            history.add(msg); 
        } finally {
            historyLock.unlock();
        }
    }
    
    void sendAnnouncement(Command msg) {
        String newCommandString = "announce " + idManager.getId()
                + " " + msg.toString();
        Command newCommand = new Command(newCommandString, msg.getSource());
        post(newCommand);
    }
    
    void sendToServer(Command msg) {
        Worker worker = msg.getSource();
        String tempString = "";
        if (null != worker) tempString += "Room: " + id + " | ";
        if (msg.isCommand("chat")) {
            tempString += msg.getSource().getLogin();
            tempString += ": " + String.join(" ", Arrays.copyOfRange(msg.getArgs(), 2, msg.getArgs().length));
        }else if (msg.isCommand("announce")) {
            tempString += String.join(" ", Arrays.copyOfRange(msg.getArgs(), 1, msg.getArgs().length));
        }
        
        if (!tempString.equals("")) server.serverLog(tempString);
    }
    
    void post(Command msg) {
        storeMessage(msg);
        
        // temp
        sendToServer(msg);
        // end temp
        
        members.forEach((worker) -> {
            worker.send(msg);
        });
    }
    
    int getNumMembers() {
        return members.size();
    }
    
    void sendMessage(Command msg) {
        String newCommandString = "chat " + idManager.getId()
                + " " + msg.getSource().getLogin() + " " + msg.toString();
        
        Command newCommand = new Command(newCommandString, msg.getSource());
        
        post(newCommand);
    }
    
    boolean addMember(Worker member) {
        boolean isSuccessful = false;
        
        membersLock.lock();
        try {
            isSuccessful = !isFull();
            if (isSuccessful) {
                members.add(member);
                member.send(ServerConstants.JOINED);
                sendAnnouncement(new Command(member.getLogin() + " just entered chat", member));
            }
        } finally {
            membersLock.unlock();
        }
        
        return isSuccessful;
    }
    
    boolean removeMember(Worker member) {
        boolean isSuccessful = false;
        
        membersLock.lock();
        try {
            isSuccessful = members.contains(member);
            if (isSuccessful) {
                members.remove(member);
                member.leaveChatRoom();
                sendAnnouncement(new Command(member.getLogin() + " just left chat", member));
            }
        } finally {
            membersLock.unlock();
        }
        
        return isSuccessful; 
    }
    
    void close() {
        
    }
}
