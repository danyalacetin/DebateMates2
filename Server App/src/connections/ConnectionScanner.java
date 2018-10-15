/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connections;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;
import server.Server;

/**
 * Responsible for getting new connections.
 * @author Aidan Stehbens
 */
class ConnectionScanner
{
    private final int port = 8818;
    private ServerSocket serverSocket;
    
    private final Consumer<Socket> newConnection;
    private Thread inputLoopThread;
    
    private boolean isOpen;
    private boolean isRunning;
    
    private Consumer<String> errorLog;
    
    ConnectionScanner(Consumer<Socket> func)
    {
        newConnection = func;
        inputLoopThread = null;
        isOpen = false;
    }
    
    int getPort() {
        return port;
    }
    
    boolean isRunning() {
        return isRunning;
    }
    
    boolean isOpen() {
        return isOpen;
    }
    
    void initialise() {
        errorLog = Server.getInstance()::errorLog;
    }
    
    private void errorLog(String msg) {
        if (null != errorLog) errorLog.accept(msg);
    }
    
    public void open()
    {
        isOpen = true;
    }
    
    public void close()
    {
        isOpen = false;
    }
    
    public void restart()
    {
        stop();
        start();
    }
    
    public void start()
    {
        try
        {
            open();
            isRunning = true;
            serverSocket = new ServerSocket(port);
            inputLoopThread = new Thread(this::handleServerSocket);
        }
        catch (IOException error)
        {
            errorLog(error.getMessage());
        }
        inputLoopThread.start();
    }
    
    public void stop()
    {
        try
        {
            close();
            isRunning = false;
            serverSocket.close();
        }
        catch (IOException error)
        {
            errorLog(error.getMessage());
        }
    }
    
    public void handleServerSocket()
    {
        while(isRunning && serverSocket != null)
        {
            try
            {
                Socket newSocket = serverSocket.accept();
                if (isOpen) newConnection.accept(newSocket);
                Server.getInstance().serverLog("Accept connection from "
                        + newSocket.getInetAddress());
            }
            catch (IOException error)
            {
            errorLog(error.getMessage());
            }
        }
    }
}
