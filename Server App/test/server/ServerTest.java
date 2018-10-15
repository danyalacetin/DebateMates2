/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import database.Database;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.function.Consumer;
import match.MatchManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import utilities.Command;

/**
 *
 * @author Demo
 */
public class ServerTest
{
    static Server server;
    private final static int PORT = 8818;
    
    public ServerTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
        Server.initialiseInstance(ServerTest::print, ServerTest::printErr);
        server = Server.getInstance();
        server.startServer();
    }
    
    @AfterClass
    public static void tearDownClass()
    {
        server.stopServer();
        server = null;
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    private static void print(String str) {
        System.out.println(str);
    }
    
    private static void printErr(String str) {
        System.err.println(str);
    }
    
    @Test
    public void testClientConnections()
    {
        Socket client = new Socket();
        SocketAddress address = new InetSocketAddress("localhost", PORT);
        
        try {
            client.connect(address);
            assertTrue("Client Connected Successfully", true);
        } catch (IOException error) {
            fail("Could Not Connect");
        }
    }
}
