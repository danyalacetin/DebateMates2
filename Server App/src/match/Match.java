/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package match;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import utilities.Command;
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
    private static final int MAX_PANELISTS = 1;
    
    private final List<Worker> players;
    private final List<Worker> panelists;
    private final List<Worker> spectators;
    
    private final ChatProcessor processor;
    private final ReentrantLock membersLock;
    private final int matchID;
    private boolean isRunning;
    private final Thread gameThread;
    
    private Worker player1;
    private Worker player2;
    
    private String currentAnnouncement;
    private final List<Integer> votes;
    private final List<Worker> winners;
    private Worker voteWinner;
    private String question;
    
    Match(int id) {
        players = new ArrayList<>(2);
        panelists = new ArrayList<>(3);
        spectators = new ArrayList<>(3);
        
        processor = new ChatProcessor();
        membersLock = new ReentrantLock();
        matchID = id;
        
        isRunning = false;
        gameThread = new Thread(this::runGame);
        currentAnnouncement = "Waiting for members...";
        votes = new ArrayList<>(MAX_PANELISTS);
        winners = new ArrayList<>(3);
        voteWinner = null;
        question = null;
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
    
    private void setCurrentAnnouncement(String announce) {
        currentAnnouncement = announce;
        processCommand(Command.createAnonymous("announce " + currentAnnouncement));
    }
    
    private boolean canStart() {
        return 2 == players.size() && MAX_PANELISTS == panelists.size() && !isRunning;
    }
    
    private void getPanelistsVotes() {
        votes.clear();
        panelists.forEach(worker -> worker.send(Command.createAnonymous("vote")));
    }
    
    public synchronized void voteReceived(int vote) {
        votes.add(vote);
        if (MAX_PANELISTS == votes.size()) {
            voteWinner = votes.stream()
                    .filter(v -> v == 1)
                    .count() >= (MAX_PANELISTS / 2) + (MAX_PANELISTS % 2)
                    ? player1
                    : player2;
            votes.clear();
            winners.add(voteWinner);
            inputRecieved();
        }
    }
    
    private void waitForInput() {
        synchronized(gameThread) {
            try {
                gameThread.wait();
            } catch (InterruptedException error) {
                System.err.println(error.getMessage());
            }
        }
    }
    
    private void inputRecieved() {
        synchronized(gameThread) {
            gameThread.notify();
        }
    }
    
    private Worker getWinner() {
        int player1Count = (int) winners.stream().filter(p -> p == player1).count();
        int player2Count = (int) winners.stream().filter(p -> p == player2).count();
        
        if (player1Count == player2Count) return null;
        else if(player1Count > player2Count) return player1;
        else return player2;
    }
    
    private void announceWinner(Worker winner) {
        String cmd = winner.getNickname()+ " Wins!";
        processCommand(Command.createAnonymous("end " + winner.getLogin()));
        setCurrentAnnouncement(cmd);
    }
    
    private void runGame() {
        question = "question";
        processCommand(Command.createAnonymous("start"));
        processCommand(Command.createAnonymous("display " + question));
        boolean startPlayer = new Random().nextBoolean();
        player1 = startPlayer ? players.get(0) : players.get(1);
        player2 = startPlayer ? players.get(1) : players.get(0);
        
        player1.send(processor.processCommand(Command.createAnonymous("matchmessage Present your agrument for")));
        player1.send(Command.createAnonymous("enable 120"));
        waitForInput();
        player2.send(processor.processCommand(Command.createAnonymous("matchmessage Rebute")));
        player2.send(Command.createAnonymous("enable 90"));
        waitForInput();
        processCommand(Command.createAnonymous("matchmessage Time to Vote!"));
        panelists.forEach(p -> p.send(Command.createAnonymous("enable 30")));
        getPanelistsVotes();
        waitForInput();
        
        player2.send(processor.processCommand(Command.createAnonymous("matchmessage Present your argument against")));
        player2.send(Command.createAnonymous("enable 120"));
        waitForInput();
        player1.send(processor.processCommand(Command.createAnonymous("matchmessage Rebute")));
        player1.send(Command.createAnonymous("enable 90"));
        waitForInput();
        processCommand(Command.createAnonymous("matchmessage Time to Vote!"));
        panelists.forEach(p -> p.send(Command.createAnonymous("enable 30")));
        getPanelistsVotes();
        waitForInput();
        
        Worker winner = getWinner();
        if (null == winner) {
            processCommand(Command.createAnonymous("matchmessage It is a Tie!"));
            processCommand(Command.createAnonymous("matchmessage Vote Again!"));
            panelists.forEach(p -> p.send(Command.createAnonymous("enable 30")));
            getPanelistsVotes();
            waitForInput();
            winner = getWinner();
        }
        
        announceWinner(winner);
        endMatch();
    }
    
    private void startMatch() {
        isRunning = true;
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
        if (cmd.is("chat")) inputRecieved();
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
        System.out.println("Match: " + matchID + ") " +  msg);
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
                    member.send(Command.createAnonymous("announce "
                            + currentAnnouncement));
                    if (isRunning) member.send("display " + question);
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
            }
        } finally {
            membersLock.unlock();
        }
        
        return isRemoved; 
    }
}
