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
public class ConnectionManager
{
    private final Consumer<ClientConnection> connectionPasser;
    private final ConnectionScanner connectionScanner;
    
    public ConnectionManager(Consumer<ClientConnection> connectionPasser)
    {
        this.connectionPasser = connectionPasser;
        this.connectionScanner = new ConnectionScanner(this::newConnection);
    }
    
    public void initialise() {
        connectionScanner.initialise();
    }
    
    public void open() {
        connectionScanner.open();
    }
    
    public void close() {
        connectionScanner.close();
    }
    
    public void start() {
        Thread scannerThread = new Thread(connectionScanner::start);
        scannerThread.start();
    }
    
    public void stop() {
        connectionScanner.stop();
    }
    
    void newConnection(Socket socket)
    {
        ClientConnection connection = new ClientConnection(socket);
        connectionPasser.accept(connection);
    }
    
    public String getServerDetails() {
        String output = "";
        
        output += "Server Details:\n";
        output += "    Scanning connections: " + connectionScanner.isRunning()+ "\n";
        output += "    Accepting connections: " + connectionScanner.isOpen()+ "\n";
        output += "    Open on port: " + connectionScanner.getPort() + "\n";
        
        return output;
    }
}
