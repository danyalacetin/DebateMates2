/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities.testclient;

import java.io.IOException;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 *
 * @author Demo
 */
public class ServerConnectionTest
{
    private final TestClientConnectionHandler handler;
    private boolean isRunning;
    
    public ServerConnectionTest(Consumer<String> display, Consumer<String> error)
            throws IOException {
        handler = new TestClientConnectionHandler(display, error);
        isRunning = false;
    }

    public void sendData(String data)
    {
        if (null != handler) handler.sendMessage(data);
    }
    
    public void stop() {
        if (null != handler && isRunning) {
            isRunning = false;
            handler.stop();
        }
    }
    
    public void start() {
        if (!isRunning) {
            isRunning = true;
            Thread client = new Thread(handler);
            client.start();
        };
    }
}
