/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverconnectiontest;

import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Demo
 */
public class ServerConnectionTest
{
    static ConnectionHandler handler = null;
    
    static void displayData(String data)
    {
        System.out.println(data);
    }

    static void sendData(String data)
    {
        if (null != handler) handler.sendMessage(data);
    }
    
    static void printPrompt()
    {
        System.out.print(">>> ");
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        try
        {
            handler = new ConnectionHandler(ServerConnectionTest::displayData);
        }
        catch (IOException error)
        {
            System.err.println("Error creating handler\n" + error.getMessage());
            handler = null;
        }
        
        Thread worker = new Thread(handler);
        worker.start();
        
        String line;
        Scanner input = new Scanner(System.in);
        
        printPrompt();
        
        while(!(line = input.nextLine()).equals("quit"))
        {
            sendData(line);
            
            try
            {
                Thread.sleep(300);
            }
            catch (InterruptedException ignore)
            {
                
            }
            
            printPrompt();
        }
        
        handler.stop();
    }
    
}
