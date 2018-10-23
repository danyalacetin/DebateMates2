/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import utilities.Command;
import main.MainController;

/**
 * Processes all Server and Client Commands
 *  
 * @author Aidan Stehbens
 */
class CommandProcessor {
    
    private Server server;
    
    void initialise() {
        server = Server.getInstance();
    }
    
    private void processServerCommand(Command command) {
        if (command.is("open", 0))
            server.unsupportedCommand();
        else if (command.is("close", 0))
            server.unsupportedCommand();
        else if (command.is("exit", 0))
            server.unsupportedCommand();
        else if (command.is("start", 0))
            server.unsupportedCommand();
        else if (command.is("matchinfo", 0))
            server.viewChatRoomInfo();
        else if(command.is("connectioninfo", 0))
            server.viewServerConnectionInfo();
        else if (command.is("createclient", 0))
            MainController.createTestClient();
        else if (command.is("createclient", 1))
            MainController.createTestClient(command.getArg(0));
        else if (command.is("clear", 0))
            MainController.clear();
        else if (command.is("clearerr", 0))
            MainController.clearErr();
        
        //Testing commands
        else if (command.is("addDB", 5))
            server.getDB().addItem(command.getArg(0), command.getArg(1), Integer.parseInt(command.getArg(2)), Integer.parseInt(command.getArg(3)), Integer.parseInt(command.getArg(4)), 0);
        else if (command.is("queryDB", 2))
            System.out.println(server.getDB().getQuery(command.getArg(0), command.getArg(1)));
        else if (command.is("viewDB", 1))
            server.getDB().viewDB(command.getArg(0));
        else if (command.is("viewDB", 0))
            server.getDB().viewDB("null");
        else if (command.is("updateDB", 3))
            server.getDB().updateItem(command.getArg(0), command.getArg(1), "'"+command.getArg(2)+"'");
        else if (command.is("dropDB", 0))
            server.getDB().droptable();

        else if (command.is("post") && 0 != command.getArgs().length)
            server.getWorkerManager().sendBroadcast(command.extractCommand());
    }
    
    private void processMatchCommand(Command command, Worker worker) {
        if (command.is("leave", 0))
            server.getMatchManager().leaveMatch(worker);
        else if (command.is("chat") && 0 != command.getArgs().length)
            server.getMatchManager().process(command);
        else if (command.is("vote", 1))
            server.getMatchManager().vote(command.getSource().getMatchID(), command.getArg(0));
    }
    
    void processCommand(Command command) {
        
        Worker worker = command.getSource();
        
        if (null == worker) // server commands
        {
            processServerCommand(command);
        }
        else if (command.is("disconnect", 0)) { // close connection
            server.getWorkerManager().removeWorker(worker);
        }
        else if (worker.isLoggedIn()) // logged in
        {
            if (command.is("logout", 0)) {
                System.out.println("logging out user");
                    server.logoutUser(worker);
            }
            else if (command.is("nickname", 1)){
                server.updateNickname(command.getArg(0), worker);
            }
            else if (command.is("updateQuestion", 2)){
                server.updateQuestion(command.getArg(0), command.getArg(1), worker);
            }
            else if (command.is("getQuestions", 0)){
                server.getQuestions(worker);
            }
            else if (worker.inMatch()) // in match
            {
                processMatchCommand(command, worker);
            }
            else // not in match
            {
                if (command.is("join", 1)) {
                    String type = command.getArg(0);
                    server.getMatchManager().joinMatch(worker, type);
                }
            }
        }
        else // not logged in
        {
            if (command.is("login", 1))
                server.loginUser(command.getArg(0), worker);
        }   
    }
}
