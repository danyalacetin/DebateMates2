/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Demo
 */
class ConnectionFinder {
    
    private final int port = 8818;
    private ServerSocket serverSocket;
    private ConnectionInitiator connectionInitiator;
    
    private boolean isOpen;
    private boolean isSearching;
    
    void initialise() {
        connectionInitiator = Server.getInstance();
    }
    
    void open() {
        isOpen = true;
    }
    
    void close() {
        isOpen = false;
    }
    
    void start() {
        open();
        isSearching = true;
        
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            
        }
        
        while (isSearching) {
            Socket newSocket = null;
            try {
                newSocket = serverSocket.accept();
                if (isOpen)
                    connectionInitiator.newConnection(newSocket);
                else
                    newSocket.close();
            } catch (IOException ex) {
                try {
                    if (null != newSocket)
                        newSocket.close();
                } catch (IOException e) {
                    
                }
            }
        }
    }
    
    void stop() {
        close();
        isSearching = false;
        try {
            serverSocket.close();
        } catch (IOException ex) {
            
        }
        
        serverSocket = null;
    }
}
