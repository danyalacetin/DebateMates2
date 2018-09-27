/*
 * This package contains tests for the server package.
 */
package server;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Aidan Stehbens
 */
public class ServerTest
{
    private static Server serverInstance;
    
    @BeforeClass
    public static void setUpClass()
    {
        serverInstance = new Server(3000);
        serverInstance.start();
    }
    
    @AfterClass
    public static void tearDownClass()
    {
        serverInstance.end();
        serverInstance = null;
    }
    
    /**
     * Adds a new socket to the server socket. Adds a pause in order to allow
     * for items to be processed for convenience of testing.
     */
    private void addWorker() {
        try {
            Socket testConnection = new Socket("127.0.0.1", 3000);
        } catch (IOException ex) {
            Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            
        }
    }
    
    /**
     * Instantiation test. Tests whether or not the Server class can be
     * successfully instantiated.
     */
    @Test
    public void testInstantiation() {
        System.out.println("Server Instantiation Test...");
        Server server = new Server(3000);
        assertNotNull(server);
    }
    
    /**
     * Tests whether a new server worker is added when a new connection is made
     */
    @Test
    public void testAddWorkerSingle() {
        System.out.println("Server adding one worker test...");
        
        int lengthBefore = serverInstance.getWorkerList().size();
        addWorker();
        int lengthAfter = serverInstance.getWorkerList().size();
        
        int expectedDifference = 1;
        int actualDifference = lengthAfter - lengthBefore;
        
        assertEquals(expectedDifference, actualDifference);
    }
    
    /**
     * Tests whether a new server worker is added when a new connection is made
     * for multiple new connections
     */
    @Test
    public void testAddWorkerMultiple() {
        System.out.println("Server adding multiple workers test...");
        
        int lengthBefore = serverInstance.getWorkerList().size();
        addWorker();
        addWorker();
        addWorker();
        int lengthAfter = serverInstance.getWorkerList().size();
        
        int expectedDifference = 3;
        int actualDifference = lengthAfter - lengthBefore;
        
        assertEquals(expectedDifference, actualDifference);
    }
    
    /**
     * Tests whether a new server worker is initialised with a value
     */
    @Test
    public void testNewWorkerNotNull() {
        System.out.println("Server check for null / failed connections test...");
        
        addWorker();
        addWorker();
        
        List<ServerWorker> workers = serverInstance.getWorkerList();
        workers.forEach(worker -> assertNotNull(worker)); // no workers are null
    }
}
