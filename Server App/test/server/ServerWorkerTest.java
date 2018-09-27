/*
 * This package contains tests for the server package.
 */
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Aidan Stehbens
 */
public class ServerWorkerTest
{
    private static ServerWorker worker;
    private static Server server;
    private static Socket client;
    
    public ServerWorkerTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
        
        
    }
    
    @AfterClass
    public static void tearDownClass()
    {
        
    }
    
    @Before
    public void setUp()
    {
        server = new Server(3000);
        server.start();
        
        try {
            client = new Socket("127.0.0.1", 3000);
        } catch (IOException ex) {
            Logger.getLogger(ServerWorkerTest.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
    
    @After
    public void tearDown()
    {
        try {
            client.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerWorkerTest.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        client = null;
        server.end();
        server = null;
    }

    /**
     * Instantiation Test
     */
    @Test
    public void instantiationTest() {
        System.out.println("ServerWorker Instantiation Test...");
        ServerWorker testWorker = new ServerWorker(server, client);
        assertNotNull(testWorker);
    }
    
    /**
     * Test the server establishing a communication to the client.
     */
    @Test
    public void connectionRceived()
    {
        System.out.println("ServerWorker Connection Received Test...");
        
        BufferedReader in;
        
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            
            String actualMessage = in.readLine();
            String expectedMessage = "Connected";
            
            assertEquals(expectedMessage, actualMessage);
            
        } catch (IOException ex) {
            Logger.getLogger(ServerWorkerTest.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void loginTest() {
        System.out.println("ServerWorker Login Test...");
        
        BufferedReader in;
        OutputStream out;
        
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = client.getOutputStream();
            
            in.readLine();
            
            out.write("login Chad\n".getBytes());
            Thread.sleep(100);
            String expectedMessage = in.readLine();
            String actualMessage = "Logged In Successfully";
            
            assertEquals(expectedMessage, actualMessage);
            
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(ServerWorkerTest.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Test
    public void alreadyLoggedInTest() {
        System.out.println("ServerWorker Already Logged In Test...");
        
        BufferedReader in;
        OutputStream out;
        
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = client.getOutputStream();
            
            in.readLine();
            
            out.write("login Chad\n".getBytes());
            out.write("login Chad\n".getBytes());
            Thread.sleep(100);
            in.readLine();
            String expectedMessage = in.readLine();
            String actualMessage = "You Are Already Logged In";
            
            assertEquals(expectedMessage, actualMessage);
            
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(ServerWorkerTest.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Test
    public void usernameTakenTest() {
        System.out.println("ServerWorker Username Taken Test");
        
        OutputStream out1;
        
        BufferedReader in2;
        OutputStream out2;
        
        try {
            Socket newClient = new Socket("127.0.0.1", 3000);
            
            in2 = new BufferedReader(new InputStreamReader(newClient.getInputStream()));
            out1 = client.getOutputStream();
            out2 = newClient.getOutputStream();
            
            in2.readLine();
            
            out1.write("login Chad\n".getBytes());
            out2.write("login Chad\n".getBytes());
            Thread.sleep(100);
            String expectedMessage = in2.readLine();
            String actualMessage = "Username Already Taken";
            
            newClient.close();
            
            assertEquals(expectedMessage, actualMessage);
            
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(ServerWorkerTest.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void errorCommandTest() {
        System.out.println("ServerWorker Error Command Test...");
        
        BufferedReader in;
        OutputStream out;
        
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = client.getOutputStream();
            
            in.readLine();
            
            out.write("login Chad Chad\n".getBytes());
            Thread.sleep(100);
            String expectedMessage = in.readLine();
            String actualMessage = "Incorrect Usage Of 'Login' Command";
            
            assertEquals(expectedMessage, actualMessage);
            
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(ServerWorkerTest.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Test
    public void chatTest() {
        System.out.println("ServerWorker Error Command Test...");
        
        BufferedReader in;
        OutputStream out;
        
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = client.getOutputStream();
            
            in.readLine();
            
            out.write("login Chad\n".getBytes());
            Thread.sleep(100);
            in.readLine();
            out.write("Hello, World!\n".getBytes());
            String expectedMessage = in.readLine();
            String actualMessage = "You: Hello, World!";
            
            assertEquals(expectedMessage, actualMessage);
            
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(ServerWorkerTest.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Test
    public void logoutTest() {
        System.out.println("ServerWorker Error Command Test...");
        
        BufferedReader in;
        OutputStream out;
        
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = client.getOutputStream();
            
            in.readLine();
            
            out.write("login Chad\n".getBytes());
            Thread.sleep(100);
            in.readLine();
            out.write("logout\n".getBytes());
            String expectedMessage = in.readLine();
            String actualMessage = "Logged Out Successfully";
            
            assertEquals(expectedMessage, actualMessage);
            
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(ServerWorkerTest.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        
    }
}
