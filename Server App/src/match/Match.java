/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package match;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import utilities.Command;
import server.Server;
import server.ServerConstants;
import server.Worker;

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
    private boolean isStarted;
    private Thread gameThread;
    private String currentAnnouncement;
    
    Match(int id) {
        players = new ArrayList<>(2);
        panelists = new ArrayList<>(3);
        spectators = new ArrayList<>(3);
        
        processor = new ChatProcessor();
        membersLock = new ReentrantLock();
        matchID = id;
        
        isStarted = false;
        gameThread = null;
        currentAnnouncement = "Waiting for members";
    }
    
    /**
     * returns the number of members in the match based on the string value given
     * @param type type of member to search for, if empty string given then
     * the total members is returned
     * @return  number of certain type of members depending on the string value
     * given
     */
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
    
    private boolean canStart() {
        return 1 == players.size() && 3 <= panelists.size() && !isStarted;
    }
    
    private void runGame() {
        // send start message
        // wait for everyone to respond
        // let player one have a turn
        // wait for response
        // let player two to have a turn
        // wait for response
        // repeat last 4 steps for x times
        // collect judges votes
        // declare winner
    }
    
    private void startMatch() {
        isStarted = true;
        gameThread = new Thread(this::runGame);
        gameThread.start();
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
                    member.send(ServerConstants.JOIN_SUCCESS);
                    processCommand(Command.createAnonymous("announce "
                            + currentAnnouncement));
                }
            }
        } finally {
            if (canStart()) startMatch();
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
                processCommand(Command.createAnonymous("announce "
                        + member.getLogin() + " has left"));
            }
        } finally {
            membersLock.unlock();
        }
        
        return isRemoved; 
    }
}
