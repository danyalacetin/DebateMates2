/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Demo
 */
class Match {
    private final List<Worker> players;
    private final List<Worker> panelists;
    private final List<Worker> spectators;
    
    private int currentPlayers;
    private int currentPanelists;
    private int currentSpectators;
    
    Match() {
        players = new ArrayList<>(2);
        panelists = new ArrayList<>();
        spectators = new ArrayList<>();
        
        currentPlayers = 0;
        currentPanelists = 0;
        currentSpectators = 0;
    }
    
    void sendMessage(Command cmd) {
        
    }
}
