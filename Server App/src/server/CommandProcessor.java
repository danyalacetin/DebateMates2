/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

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
    
    void processCommand(Command command) {
        
        Worker worker = command.getSource();
        
        if (null == worker) // server commands
        {
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
            else if (command.isCommand("addDB", 0))
                server.getDB().addItem("123", "Nickname123", 1, 1, 1234, 1);
            else if (command.isCommand("viewDB", 0))
                server.getDB().getQuery("123", "NICKNAME");
            else if (command.isCommand("updateDB", 0))
                server.getDB().updateItem("123", "NICKNAME", "'jamjam'");
            else if (command.isCommand("dropDB", 0))
                server.getDB().droptable();
            
            else if (command.isCommand("post") && 0 != command.getArgs().length)
                server.getWorkerManager().sendBroadcast(command.nextCommand());
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
            else if (worker.inMatch()) // in match
            {
                if (command.isCommand("leave", 0))
                    server.getMatchManager().leaveMatch(worker);
                else if (command.isCommand("match"))
                    server.getMatchManager().process(command.nextCommand());
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
