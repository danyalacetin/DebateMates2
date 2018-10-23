/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package match;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import server.Server;
import utilities.Command;
import server.Worker;
import utilities.IdManager;

/**
 *
 * @author Demo
 */
public class MatchManager {
    private final Map<Integer, Match> matches;
    private final IdManager idManager;
    
    public MatchManager() {
        matches = new HashMap<>();
        idManager = new IdManager();
    }
    
    int createMatch() {
        final int id = idManager.getId();
        Match match = new Match(id);
        matches.put(id, match);
        
        return id;
    }
    
    public void process(Command cmd) {
        final int id = cmd.getSource().getMatchID();
        Match match = matches.get(id);
        if (null != matches) match.processCommand(cmd);
    }
    
    public void joinMatch(Worker worker, String type) {
        int id = findMatch(type);
        joinMatch(worker, type, id);
    }
    
    void joinMatch(Worker worker, String type, int id) {
        Match match = matches.get(id);
        if (-1 != id && null != match) match.addMember(worker, type);
    }
    
    private int findMatch(String type) {
//        int id = -1;
        
        for (Entry<Integer, Match> entry : matches.entrySet()) {
            Match match = entry.getValue();
            if (match.checkAvailable(type)) {
//                id = entry.getKey();
                return entry.getKey();
            }
        }
        
//        if (-1 == id) {
//            id = createMatch();
//        }
//        
//        return id;
        return createMatch();
    }
    
    public void leaveMatch(Worker worker) {
        matches.get(worker.getMatchID()).removeMember(worker);
    }
    
    public void vote(int id, String vote) {
        int voteInt = -1;
        try {
            voteInt = Integer.parseInt(vote);
        } catch (NumberFormatException err) {
            System.err.println(err.getMessage());
        }
        matches.get(id).voteReceived(voteInt);
    }
    
    public String getMatchInfo() {
        String info = "# Matches: " + matches.size() + "\n";
        for (Entry<Integer, Match> entry : matches.entrySet()) {
            Match match = entry.getValue();
            
            info += "Match ID: " + entry.getKey() + ", ";
            info += match.getNumMembers("") + " total member(s)\n";
            
            info += "    Players: " + match.getNumMembers("player") + "\n";
            info += "    Panelists: " + match.getNumMembers("panelist") + "\n";
            info += "    Spectators: " + match.getNumMembers("spectator") + "\n";
        }
        
        return info;
    }
}
