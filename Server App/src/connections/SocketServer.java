/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connections;

import java.net.Socket;
import java.util.function.Consumer;

/**
 * Handles all connections to the server
 * Handles creation, deletion and maintenance of all sockets which are connected.
 * 
 * @author Aidan Stehbens
 */
public class SocketServer
{
    private final Consumer<ServerConnection> handlerFunction;
    private final ConnectionManager connectionManager;
    private final ConnectionScanner connectionScanner;
    
    public SocketServer(Consumer<ServerConnection> handlerFunction)
    {
        this.handlerFunction = handlerFunction;
        this.connectionManager = new ConnectionManager();
        this.connectionScanner = new ConnectionScanner();
    }
    
    void newConnection(Socket socket)
    {
        
    }
    
}
