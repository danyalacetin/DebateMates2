/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverconnectiontest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Demo
 */
class ConnectionHandler implements Runnable
{
    private static final int PORT = 8818;
    private static final String IP_ADDRESS = "192.168.0.3";
    
    private final Socket socket;
    private PrintWriter outStream;
    private BufferedReader inStream;
    
    private final Consumer<String> dataHandler;

    public ConnectionHandler(Consumer<String> dataHandler) throws IOException
    {
        this.socket = new Socket(IP_ADDRESS, PORT);
        this.dataHandler = dataHandler;
    }

    void stop()
    {
        try
        {
//            sendMessage("disconnect");
            outStream.close();
            socket.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run()
    {
        try
        {
            connect();
            handleSocket();
            
            System.out.println("SOCKET DONE");
        }
        catch (IOException error)
        {
            System.err.println("Error handling socket");
            System.err.println(error.getMessage());
        }
        finally
        {
            try
            {
                socket.close();
            }
            catch (IOException error2)
            {
                System.err.println("Error closing socket");
                System.err.println(error2.getMessage());
            }
        }
        
    }
    
    private void connect() throws IOException
    {
        outStream = new PrintWriter(new BufferedWriter
                (new OutputStreamWriter(socket.getOutputStream())));
        inStream = new BufferedReader
                (new InputStreamReader(socket.getInputStream()));
    }
    
    void sendMessage(String msg)
    {
        outStream.println(msg);
        outStream.flush();
    }
    
    private void handleSocket() throws IOException
    {
        String line;
        while(null != (line = inStream.readLine()))
        {
            dataHandler.accept(line);
        }
    }
}
