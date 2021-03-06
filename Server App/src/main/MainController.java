/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import server.Server;
import userinterface.ClientInstanceTest;
import userinterface.MainUI;
import utilities.IdManager;

/**
 *
 * @author Demo
 */
public class MainController
{
    private static MainController instance = null;
    
    private Server server;
    private MainUI window;
    
    private static final IdManager CLIENT_NUMBER = new IdManager();
    
    private MainController() {
        instance = this;
    }
    
    
    
    public static void clear() {
        if (null != instance) {
            instance.window.clearLog();
        }
    }
    
    public static void clearErr() {
        if (null != instance) {
            instance.window.clearErrLog();
        }
    }
    
    private void log(String msg) {
        if (null != window) {
            window.log(msg);
        }
    }
    
    private void errLog(String msg) {
        if (null != window) {
            window.errorLog(msg);
        }
    }
    
    public static void createTestClient() {
        ClientInstanceTest test = new ClientInstanceTest(CLIENT_NUMBER.getId(),
                client ->   {
                                CLIENT_NUMBER.recycleID(client.getID());
                            
                            });
        test.setVisible(true);
    }
    
    public static void createTestClient(String number) {
        try {
            int num = Integer.parseInt(number);
            for (int i = 0; i < num; ++i) createTestClient();
        } catch (NumberFormatException error) {
            System.err.println("invalid command");
        }
    }
    
    /**
     * Redirects the System.out and System.err to write using log and errLog
     * respectively, effectively printing to the GUI console.
     */
    private void redirectOutputStreams() {
        OutputStream out = new OutputStream()
        {
            @Override
            public void write(int b) throws IOException
            {
                log(String.valueOf((char) b));
                
            }

            @Override
            public void write(byte[] b) throws IOException
            {
                write(b, 0, b.length);
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException
            {
                log(new String(b, off, len));
            }
        };
        
        OutputStream outErr = new OutputStream()
        {
            @Override
            public void write(int b) throws IOException
            {
                errLog(String.valueOf((char) b));
            }

            @Override
            public void write(byte[] b) throws IOException
            {
                write(b, 0, b.length);
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException
            {
                errLog(new String(b, off, len));
            }
        };
        
        System.setErr(new PrintStream(outErr, true));
        System.setOut(new PrintStream(out, true));
    }
    
    private void start() {
        
        Server.initialiseInstance(this::log, this::errLog);
        server = Server.getInstance();
        window = new MainUI();
        window.setVisible(true);
        redirectOutputStreams();
        
        server.startServer();
    }
    
    public static void main(String[] args) {
        MainController mainApp = new MainController();

        mainApp.start();
    }
}
