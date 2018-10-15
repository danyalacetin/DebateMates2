/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connections;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Demo
 */
public class ClientConnection implements Runnable
{
    private final Socket socket;
    private WorkerConnectionInterface worker;
    
    private BufferedWriter outStream;
    private BufferedReader inStream;
    
    private final List<String> backlog;
    
    ClientConnection(Socket socket)
    {
        this.socket = socket;
        openCommunication();
        backlog = new ArrayList<>();
    }
    
    public void setWorker(WorkerConnectionInterface worker)
    {
        this.worker = worker;
    }
    
    private boolean openCommunication()
    {
        if (null != socket)
        {
            OutputStream out = null;
            InputStream in = null;
            
            try
            {
                out = socket.getOutputStream();
                in = socket.getInputStream();
            }
            catch (IOException openError)
            {
                System.err.println(openError.getMessage());
            }
            
            if (null != out)
                outStream = new BufferedWriter(new OutputStreamWriter(out));
            if (null != in)
                inStream = new BufferedReader(new InputStreamReader(in));
        }
        
        return null != outStream && null != inStream;
    }
    
    private void sendStringToWorker(String str)
    {
        if (null != worker)
        {
            if (!backlog.isEmpty())
                backlog.forEach(string -> worker.handleString(string));
            worker.handleString(str);
        }
        else
        {
            backlog.add(str);
        }
    }
    
    private void tryCatch(TryRunnable function)
    {
        try
        {
            function.run();
        }
        catch(IOException ex)
        {
            System.err.println(ex.getMessage());
        }
    }
    
    public void send(String output) throws IOException
    {
        outStream.write(output);
        outStream.flush();
    }
    
    public void sendHandled(String output)
    {
        tryCatch(() -> send(output));
    }
    
    private void inputLoop() throws IOException
    {
        String line;
        while (null != (line = inStream.readLine()))
        {
            if (!line.equals("")) sendStringToWorker(line);
        }
    }

    @Override
    public void run()
    {
        if (openCommunication())
        {
            tryCatch(this::inputLoop);
        }
    }
}
