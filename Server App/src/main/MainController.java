/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import server.Server;
import userinterface.MainUI;

/**
 *
 * @author Demo
 */
class MainController
{
    private static final int SERVER_PORT = 8818;
    
    private Server server;
    private MainUI window;
    
    MainController() {
        
    }
    
    private void log(String msg) {
        if (null != window) window.log(msg);
    }
    
    private void start() {
        server = new Server(SERVER_PORT, this::log);
        window = new MainUI();
        window.setVisible(true);
        server.startServer();
        
    }
    
    
    
    
    
    
    public static void main(String[] args) {
        MainController mainApp = new MainController();
        mainApp.start();
    }
}
