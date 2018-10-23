/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities.testclient;

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
class TestClientConnectionHandler implements Runnable
{
    private static final int PORT = 8818;
    private static final String IP_ADDRESS = "localhost";
    
    private final Socket socket;
    private PrintWriter outStream;
    private BufferedReader inStream;

    private final Consumer<String> dataHandler;
    private final Consumer<String> errorHandler;
    
    public TestClientConnectionHandler(Consumer<String> dataHandler,
            Consumer<String> errorHandler) throws IOException
    {
        this.socket = new Socket(IP_ADDRESS, PORT);
        this.dataHandler = dataHandler;
        this.errorHandler = errorHandler;
    }

    void stop()
    {
        try
        {
            outStream.close();
            socket.close();
        }
        catch (IOException ex)
        {
            errorHandler.accept("Error closing client session");
            errorHandler.accept(ex.getMessage());
        }
    }
    
    @Override
    public void run()
    {
        try
        {
            connect();
            handleSocket();
            
            dataHandler.accept("SOCKET DONE");
        }
        catch (IOException error)
        {
            errorHandler.accept("Error handling socket");
            errorHandler.accept(error.getMessage());
        }
        finally
        {
            try
            {
                socket.close();
            }
            catch (IOException error2)
            {
                errorHandler.accept("Error aborting socket");
                errorHandler.accept(error2.getMessage());
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
