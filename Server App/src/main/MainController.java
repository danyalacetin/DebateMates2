/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import database.Database;
import server.Server;
import userinterface.MainUI;

/**
 *
 * @author Demo
 */
class MainController
{
    private Server server;
    private MainUI window;
    private Database database;
    
    MainController() {
        
    }
    
    private void log(String msg) {
        if (null != window) window.log(msg);
        System.out.println(msg);
    }
    
    private void start() {
        
        Server.initialiseInstance(this::log);
        server = Server.getInstance();
        window = new MainUI();
        window.setVisible(true);
        
        database = new Database();
        database.establishConnection();
        database.createTable();
        database.addItem(1, "test", 1, 1, 1250, 1);
        server.startServer();
    }
    
    public static void main(String[] args) {
        MainController mainApp = new MainController();

        mainApp.start();
    }
}
