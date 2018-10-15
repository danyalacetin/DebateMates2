/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connections;

import java.net.Socket;
import java.util.ArrayList;

/**
 * Responsible for managing open connections.
 * @author Aidan Stehbens
 */
class ConnectionManager
{
    private final SyncListWrapper<ClientConnection> connections;
    
    ConnectionManager()
    {
        connections = new SyncListWrapper<>(new ArrayList<>());
    }
    
    ClientConnection addConnection(Socket socket)
    {
        ClientConnection newConnection = new ClientConnection(socket);
        connections.add(newConnection);
        return newConnection;
    }
    
    void removeConnection(ClientConnection conn)
    {
        connections.remove(conn);
    }
}
