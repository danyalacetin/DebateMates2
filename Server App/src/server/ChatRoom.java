/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 *
 * @author Demo
 */
class ChatRoom {
    private final int maxMembers;
    private final List<Worker> members;
    private final ReentrantLock membersLock;
    private final ReentrantLock historyLock;
    
    private final List<Command> history;
    private final IdManager idManager;
    private final Consumer<Command> commandProcessor;
    
    ChatRoom(int max, Consumer<Command> commandProcessor) {
        members = new ArrayList<>();
        membersLock = new ReentrantLock();
        historyLock = new ReentrantLock();
        history = new ArrayList<>();
        idManager = new IdManager();
        
        this.commandProcessor = commandProcessor;
        maxMembers = max;
    }
    
    boolean isFull() {
        return maxMembers == members.size();
    }
    
    boolean addMember(Worker member) {
        boolean isSuccessful = false;
        
        membersLock.lock();
        try {
            isSuccessful = !isFull();
            if (isSuccessful) {
                members.add(member);
                sendMessage(new Command(ServerConstants.SERVER_DISPLAY_NAME
                        + " " + member.getLogin() + " just entered chat", null));
            }
        } finally {
            membersLock.unlock();
        }
        
        return isSuccessful;
    }
    
    void storeMessage(Command msg) {
        historyLock.lock();
        try {
            history.add(msg);
            commandProcessor.accept(msg);
        } finally {
            historyLock.unlock();
        }
    }
    
    void sendMessage(Command msg) {
        String command = "chat";
        String newCommandString = command + " " + idManager.getId()
                + " " + msg.toString();
        
        Command newCommand = new Command(newCommandString, msg.getSource());
        
        storeMessage(newCommand);
        members.forEach((worker) -> {
            worker.send(newCommand);
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
