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
class CommandProcessor {
    
    private Server server;
    
    void initialise() {
        server = Server.getInstance();
    }
    
    void processCommand(Command command) {
        
        Worker worker = command.getSource();
        
        if (null == worker)
        {
            if (command.isCommand("open", 0))
                server.acceptClients();
            else if (command.isCommand("close", 0))
                server.rejectClients();
            else if (command.isCommand("exit", 0))
                server.stopServer();
            else if (command.isCommand("start", 0))
                server.startServer();
            else if (command.isCommand("chatroominfo", 0))
                server.viewChatRoomInfo();
            
            //Testing commands
            else if (command.isCommand("addDB", 0))
                server.addDBItem("123", "Nickname123", 1, 1, 1234, 1);
            else if (command.isCommand("viewDB", 0))
                server.viewDBItem("123", "NICKNAME");
            else if (command.isCommand("updateDB", 0))
                server.updateDBItem("123", "NICKNAME", "'jamjam'");
            else if (command.isCommand("dropDB", 0))
                server.dropDBtable();
            
        }
        else if (worker.isLoggedIn())
        {
            if (command.isCommand("logout", 0)) {
                System.out.println("logging out user");
                    server.logOutUser(worker);
            }
            else if (worker.isInChat())
            {
                if (command.isCommand("leave", 0))
                    server.getChatRoomManager().leaveChat(worker);
                else if (command.isCommand("chat"))
                    server.getChatRoomManager().sendMessage(command.nextCommand());
                else if (command.isCommand("announce"))
                    server.getChatRoomManager().sendAnnouncement(command.nextCommand());
            }
            else
            {
                if (command.isCommand("join", 0))
                    server.getChatRoomManager().joinChatRoom(worker);
            }
        }
        else
        {
            if (command.isCommand("login", 1))
                server.logInUser(command.getArg(0), worker);
        }   
    }
    
}
