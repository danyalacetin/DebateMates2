/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author Demo
 */
class CommandManager {
    
    private final Server server;

    CommandManager(Server server)
    {
        this.server = server;
    }
    
    void processCommand(Command command) {
        
        if (command.isCommand("open", 0))
            server.acceptClients();
        else if (command.isCommand("close", 0))
            server.rejectClients();
        else if (command.isCommand("exit", 0))
            server.stopServer();
        else if (command.isCommand("start", 0))
            server.startServer();
        else if (command.isCommand("login", 1))
            server.loginUser(command.getArg(0), command.getSource());
        else if (command.isCommand("logout", 1))
            server.logOutUser(command.getArg(0), command.getSource());
        else if (command.isCommand("chatroom")) {
            int room = command.getSource().getChatRoom();
            server.getChatRoomManager().sendMessage(0, command.nextCommand());
        }
    }
    
}
