/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import connections.ClientConnection;

/**
 * Interface for creating new connections.
 * 
 * @author Aidan Stehbens
 */
interface ConnectionInitiator
{
    /**
     * Task to complete when a new connection is made with the server.
     * @param socket Socket for the new connection.
     */
    abstract void newConnection(ClientConnection socket);
}
