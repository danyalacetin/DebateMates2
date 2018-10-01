/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Demo
 */
class ChatRoom {
    private static final int MAX_MEMBERS = 5;
    private final List<Worker> members;
    private final ReentrantLock membersLock;
    private final ReentrantLock historyLock;
    
    private final List<Command> history;
    private final IdManager idManager;
    
    ChatRoom() {
        members = new ArrayList<>();
        membersLock = new ReentrantLock();
        historyLock = new ReentrantLock();
        history = new ArrayList<>();
        idManager = new IdManager();
    }
    
    boolean isFull() {
        return MAX_MEMBERS == members.size();
    }
    
    boolean addMember(Worker member) {
        boolean isSuccessful = false;
        
        membersLock.lock();
        try {
            isSuccessful = !isFull();
            if (isSuccessful) members.add(member);
        } finally {
            membersLock.unlock();
        }
        
        return isSuccessful;
    }
    
    void storeMessage(Command msg) {
        historyLock.lock();
        try {
            history.add(msg);
        } finally {
            historyLock.unlock();
        }
    }
    
    void sendMessage(Command msg) {
        String senderString = msg.getLabel();
        String command = "chat";
        String newCommand = command + " " + idManager.getId()
                + " " + msg.toString();
        
        members.forEach((worker) -> {
            worker.sendCommand(new Command(newCommand, msg.getSource()));
        });
    }
    
    boolean removeMember(Worker member) {
        boolean isSuccessful = false;
        
        membersLock.lock();
        try {
            isSuccessful = members.contains(member);
            if (isSuccessful) members.remove(member);
        } finally {
            membersLock.unlock();
        }
        
        return isSuccessful; 
    }
    
    void close() {
        
    }
}
