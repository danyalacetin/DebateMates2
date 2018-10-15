/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.ArrayList;
import java.util.List;
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
    private Server server;
    private MainUI window;
    
    private static final IdManager CLIENT_NUMBER = new IdManager();
    
    MainController() {
        
    }
    
    private void log(String msg) {
        if (null != window) window.log(msg);
        System.out.println(msg);
    }
    
    private void errorLog(String msg) {
        if (null != window) window.errorLog(msg);
        System.err.println(msg);
    }
    
    public static void createTestClient() {
        ClientInstanceTest test = new ClientInstanceTest(CLIENT_NUMBER.getId(),
                client ->   {
                                CLIENT_NUMBER.recycleID(client.getID());
                            
                            });
        test.setVisible(true);
    }
    
    private void start() {
        
        Server.initialiseInstance(this::log, this::errorLog);
        server = Server.getInstance();
        window = new MainUI();
        window.setVisible(true);
        
        server.startServer();
    }
    
    public static void main(String[] args) {
        MainController mainApp = new MainController();

        mainApp.start();
    }
}
