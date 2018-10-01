/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

/**
 *
 * @author Demo
 */
class ConnectionFinder {
    
    private final int port = 8818;
    private ServerSocket serverSocket;
    private Consumer<Socket> newConnection;
    
    private boolean isOpen;
    private boolean isSearching;

    public ConnectionFinder()
    {
        newConnection = null;
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
                if (isOpen && null != newConnection)
                    newConnection.accept(newSocket);
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
    
    void registerConnectionFunction(Consumer<Socket> connectionFunction) {
        if (null != connectionFunction) newConnection = connectionFunction;
    }
}
