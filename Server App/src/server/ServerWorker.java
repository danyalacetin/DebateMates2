/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 * Interface used to define the interaction between the Server and the majority
 * of its parts.
 * 
 * @author Aidan Stehbens
 */
public interface ServerWorker { // needs a new name
    /**
     * Displays a message to the main server screen.
     * @param txt Text to be displayed.
     */
    abstract void serverLog(String txt);
    
    /**
     * Processes a command to be carried out by the Server.
     * @param command Command to be processed.
     */
    abstract void processCommand(Command command);
}
