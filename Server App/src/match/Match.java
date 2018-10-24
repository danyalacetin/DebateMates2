/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package match;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import server.Server;
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
        question = getQuestion();
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
    
    private void waitTime(final long time) {
        try {
            Thread.sleep(time * 1000);
        } catch (InterruptedException ex) {
            
        }
    }
    
    private void waitForInput(final int timeout) {
        synchronized(gameThread) {
            try {
                gameThread.wait(timeout * 1000);
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
        processCommand(Command.createAnonymous("start"));
        for(int i = 5; i > -1; i--){
            processCommand(Command.createAnonymous("announce The Game is about to begin in "+i));
            waitTime(1);
        }
        processCommand(Command.createAnonymous("display " + question));
        processCommand(Command.createAnonymous("announce Please read the statement."));
        waitTime(5);
        boolean startPlayer = new Random().nextBoolean();
        player1 = startPlayer ? players.get(0) : players.get(1);
        player2 = startPlayer ? players.get(1) : players.get(0);
        
        processCommand(Command.createAnonymous(player1.getNickname()+" Is forming thier argument."));
        player1.send(processor.processCommand(Command.createAnonymous("announce "
                + player1.getNickname() + " please present your agrument for")));
        player1.send(Command.createAnonymous("enable"));
        waitForInput(120);
        player1.send(Command.createAnonymous("disable"));
        processCommand(Command.createAnonymous(player2.getNickname()+" Is forming their rebutle."));
        player2.send(processor.processCommand(Command.createAnonymous("announce "
                + player2.getNickname() + " present your rebutle")));
        player2.send(Command.createAnonymous("enable"));
        waitForInput(90);
        player2.send(Command.createAnonymous("disable"));
        processCommand(Command.createAnonymous("announce Panelists are voting!"));
        panelists.forEach(p -> p.send(Command.createAnonymous("enable")));
        getPanelistsVotes();
        waitForInput(30);
        panelists.forEach(p -> p.send(Command.createAnonymous("disable")));
        
        processCommand(Command.createAnonymous(player2.getNickname()+" Is forming their argument."));
        player2.send(processor.processCommand(Command.createAnonymous("announce "
                + player2.getNickname() +  " Present your argument against")));
        player2.send(Command.createAnonymous("enable"));
        waitForInput(120);
        player2.send(Command.createAnonymous("disable"));
        processCommand(Command.createAnonymous(player1.getNickname()+" Is forming their rebutle."));
        player1.send(processor.processCommand(Command.createAnonymous("announce "
                + player1.getNickname() + " present your rebutle")));
        player1.send(Command.createAnonymous("enable"));
        waitForInput(90);
        player1.send(Command.createAnonymous("disable"));
        processCommand(Command.createAnonymous("announce Panelists are voting!"));
        panelists.forEach(p -> p.send(Command.createAnonymous("enable")));
        getPanelistsVotes();
        waitForInput(30);
        panelists.forEach(p -> p.send(Command.createAnonymous("disable")));
        
        Worker winner = getWinner();
        if (null == winner) {
            processCommand(Command.createAnonymous("announce It is a Tie!"));
            processCommand(Command.createAnonymous("announce Who's debated best overall?"));
            panelists.forEach(p -> p.send(Command.createAnonymous("enable")));
            getPanelistsVotes();
            waitForInput(30);
            panelists.forEach(p -> p.send(Command.createAnonymous("disable")));
            winner = getWinner();
        }
        
        announceWinner(winner);
        isRunning = false;
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
        endMatch(null);
    }
    
    private void kickAll() {
        final List<Worker> toRemove = new ArrayList<>();
        players.forEach(toRemove::add);
        panelists.forEach(toRemove::add);
        spectators.forEach(toRemove::add);
        toRemove.forEach(this::removeMember);
    }
    
    private void endMatch(Worker reason) {
        isRunning = false;
        final List<Command> joinCommands = new ArrayList<>();
        
        if (null != reason) {
            players.forEach(worker -> {
                joinCommands.add(Command.create("join player", worker));
            });
            panelists.forEach(worker -> {
                joinCommands.add(Command.create("join panelist", worker));
            });
            spectators.forEach(worker -> {
                joinCommands.add(Command.create("join spectator", worker));
            });
        }
        shutdown();
        if (null != reason) joinCommands.forEach(Server.getInstance()::processCommand);
    }
    
    private void shutdown() {
        kickAll();
        Server.getInstance().processCommand(Command.createAnonymous("deletematch " + matchID));
    }
    
    boolean removeMember(Worker member) {
        boolean isRemoved = false;
        Worker left = null;
        
        membersLock.lock();
        try {
            if (players.contains(member)) {
                players.remove(member);
                isRemoved = true;
                if(isRunning) left = member; // end the match
            } else if (panelists.contains(member)) {
                panelists.remove(member);
                isRemoved = true;
                if(isRunning) left = member; // end the match
            } else if (spectators.contains(member)) {
                spectators.remove(member);
                isRemoved = true;
            }
            
            if (isRemoved) member.leaveMatch();
        } finally {
            membersLock.unlock();
            if (null != left) endMatch(left);
        }
        
        return isRemoved; 
    }
    
    public String getQuestion(){
        int rand = ThreadLocalRandom.current().nextInt(1, 20 + 1);
        switch (rand){
            case 1:
                return "Mac is better than Pc";
            case 2:
                return "Donald Trump is a good president";
            case 3:
                return "The great pyramids were built using alien technology";
            case 4:
                return "Racism is rampant in western society";
            case 5:
                return "The theory of evolution is fake";
            case 6:
                return "Video games cause violence";
            case 7:
                return "There are more than two genders";
            case 8:
                return "There needs to be proper gun control in places like America";
            case 9:
                return "Global warming isn’t real";
            case 10:
                return "The earth is flat";
            case 11:
                return "9/11 was an inside job";
            case 12:
                return "High school is a waste of time";
            case 13:
                return "Morality comes from god";
            case 14:
                return "Aliens regularly come to earth";
            case 15:
                return "Woman get paid less than men purely due to gender inequality";
            case 16:
                return "Artificial Intelligence is dangerous";
            case 17:
                return "Private schools are better than public schools";
            case 18:
                return "Executing prisoners should be ilegal";
            case 19:
                return "Celebrities don't deserve a private life";
            case 20:
                return "Cigarettes should be banned";
            default:
                return "Error";
        }
    }
}
