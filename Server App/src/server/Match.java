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
 * 
 * @author Aidan Stehbens
 */
class Match {
    private static final int MAX_PLAYERS = 2;
    private static final int MAX_SPECTATORS = 3;
    private static final int MAX_PANELISTS = 3;
    
    private final List<Worker> players;
    private final List<Worker> panelists;
    private final List<Worker> spectators;
    
    private final ChatProcessor processor;
    private final ReentrantLock membersLock;
    private final int matchID;
    
    Match(int id) {
        players = new ArrayList<>(2);
        panelists = new ArrayList<>(3);
        spectators = new ArrayList<>(3);
        
        processor = new ChatProcessor();
        membersLock = new ReentrantLock();
        matchID = id;
    }
    
    int getNumMembers(String type) {
        int number;
        switch (type) {
            case "player":
                number = players.size();
                break;
            case "panelist":
                number = panelists.size();
                break;
            case "spectator":
                number = spectators.size();
                break;
            case "":
                number = players.size() + panelists.size()
                        + spectators.size(); // in case total is needed
                break;
            default:
                number = -1;
                break;
        }
        return number;
    }
    
    boolean checkAvailable(String type) {
        boolean isAvailable = false;
        membersLock.lock();
        try {
            switch (type) {
                case "player":
                    isAvailable = players.size() < MAX_PLAYERS;
                    break;
                case "panelist":
                    isAvailable = panelists.size() < MAX_PANELISTS;
                    break;
                case "spectator":
                    isAvailable = spectators.size() < MAX_SPECTATORS;
                    break;
            }
        } finally {
            membersLock.unlock();
        }
        return isAvailable;
    }
    
    void processCommand(Command cmd) {
        Command processed = processor.processCommand(cmd);
        post(processed.toString());
    }
    
    private void post(String msg) {
        Consumer<Worker> sendFunction = worker -> {
            worker.send(msg);
        };
        
        players.forEach(sendFunction);
        panelists.forEach(sendFunction);
        spectators.forEach(sendFunction);
        postToServer(msg);
    }
    
    private void postToServer(String msg) {
        Server.getInstance().serverLog("Match: " + matchID + ") " +  msg);
    }
    
    boolean addMember(Worker member, String type) {
        boolean isAvailable;
        List<Worker> toAdd;
        
        membersLock.lock();
        isAvailable = checkAvailable(type);
        
        try {
            if (isAvailable) {
                switch (type) {
                    case "player":
                        toAdd = players;
                        break;
                    case "panelist":
                        toAdd = panelists;
                        break;
                    case "spectator":
                        toAdd = spectators;
                        break;
                    default:
                        toAdd = null; // incase of invalid command
                        break;
                }
                if (null != toAdd) {
                    toAdd.add(member);
                    member.enterMatch(matchID);
                    member.send(ServerConstants.JOINED);
                    processCommand(Command.anonymousCommand("announce "
                            + member.getLogin() + " has entered as a " + type));
                }
            }
        } finally {
            membersLock.unlock();
        }
        
        return isAvailable;
    }
    
    private void endMatch() {
        
    }
    
    boolean removeMember(Worker member) {
        boolean isRemoved = false;
        
        membersLock.lock();
        try {
            if (players.contains(member)) {
                players.remove(member);
                isRemoved = true;
                member.leaveMatch();
                endMatch(); // end the match
            } else if (panelists.contains(member)) {
                panelists.remove(member);
                isRemoved = true;
                member.leaveMatch();
            } else if (spectators.contains(member)) {
                spectators.remove(member);
                isRemoved = true;
                member.leaveMatch();
            }
            
            if (isRemoved) {
                member.leaveMatch();
                processCommand(Command.anonymousCommand("announce "
                        + member.getLogin() + " has left"));
            }
        } finally {
            membersLock.unlock();
        }
        
        return isRemoved; 
    }
}
