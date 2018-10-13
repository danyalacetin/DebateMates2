/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connections;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for managing open connections.
 * @author Aidan Stehbens
 */
class ConnectionManager
{
    private final SyncListWrapper<ServerConnection> connections;
    
    ConnectionManager()
    {
        connections = new SyncListWrapper<>(new ArrayList<>());
    }
    
    ServerConnection addConnection(Socket socket)
    {
        ServerConnection newConnection = new ServerConnection(socket);
        connections.add(newConnection);
        return newConnection;
    }
    
    void removeConnection(ServerConnection conn)
    {
        connections.remove(conn);
    }
}
