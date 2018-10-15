/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import utilities.Command;

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
        if (command.isCommand("open", 0))
            server.unsupportedCommand();
        else if (command.isCommand("close", 0))
            server.unsupportedCommand();
        else if (command.isCommand("exit", 0))
            server.unsupportedCommand();
        else if (command.isCommand("start", 0))
            server.unsupportedCommand();
        else if (command.isCommand("matchinfo", 0))
            server.viewChatRoomInfo();
        
        //Testing commands
        else if (command.isCommand("addDB", 5))
            server.getDB().addItem(command.getArg(0), command.getArg(1), Integer.parseInt(command.getArg(2)), Integer.parseInt(command.getArg(3)), Integer.parseInt(command.getArg(4)), 0);
        else if (command.isCommand("queryDB", 2))
            System.out.println(server.getDB().getQuery(command.getArg(0), command.getArg(1)));
        else if (command.isCommand("viewDB", 1))
            server.getDB().viewDB(command.getArg(0));
        else if (command.isCommand("viewDB", 0))
            server.getDB().viewDB("null");
        else if (command.isCommand("updateDB", 3))
            server.getDB().updateItem(command.getArg(0), command.getArg(1), "'"+command.getArg(2)+"'");
        else if (command.isCommand("dropDB", 0))
            server.getDB().droptable();

        else if (command.isCommand("post") && 0 != command.getArgs().length)
            server.getWorkerManager().sendBroadcast(command.extractCommand());
    }
    
    private void processMatchCommand(Command command, Worker worker) {
        if (command.isCommand("leave", 0))
            server.getMatchManager().leaveMatch(worker);
        else if (command.isCommand("chat") && 0 != command.getArgs().length)
            server.getMatchManager().process(command);
    }
    
    void processCommand(Command command) {
        
        Worker worker = command.getSource();
        
        if (null == worker) // server commands
        {
            processServerCommand(command);
        }
        else if (command.isCommand("disconnect", 0)) { // close connection
            server.getWorkerManager().removeWorker(worker);
        }
        else if (worker.isLoggedIn()) // logged in
        {
            if (command.isCommand("logout", 0)) {
                System.out.println("logging out user");
                    server.logOutUser(worker);
            }
            else if (command.isCommand("nickname", 1)){
                server.updateNickname(command.getArg(0), worker);
            }
            else if (command.isCommand("updateQuestion", 2)){
                server.updateQuestion(command.getArg(0), command.getArg(1), worker);
            }
            else if (command.isCommand("getQuestions", 0)){
                server.getQuestions(worker);
            }
            else if (worker.inMatch()) // in match
            {
                processMatchCommand(command, worker);
            }
            else // not in match
            {
                if (command.isCommand("join", 1)) {
                    String type = command.getArg(0);
                    server.getMatchManager().joinMatch(worker, type);
                }
            }
        }
        else // not logged in
        {
            if (command.isCommand("login", 1))
                server.logInUser(command.getArg(0), worker);
        }   
    }
}
