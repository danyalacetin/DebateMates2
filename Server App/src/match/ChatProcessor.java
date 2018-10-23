/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package match;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import utilities.Command;
import utilities.IdManager;

/**
 *
 * @author Demo
 */
class ChatProcessor
{
    private final List<Command> history;
    private final ReentrantLock chatLock;
    private final IdManager idManager;
    
    ChatProcessor() {
        history = new ArrayList<>();
        chatLock = new ReentrantLock();
        idManager = new IdManager();
    }
    
    private void logPost(Command msg) {
        history.add(msg);
    }
    
    Command processCommand(final Command cmd) {
        final Command reply;
        final String content;
        final String user;
        final int id;
        
        chatLock.lock();
        
        try {
            if (cmd.is("chat")) {
                id = idManager.getId();
                user = cmd.getSource().getLogin();
                content = cmd.extractCommand().toString();
                String replyString = String.format("chat %d %s %s", id, user,
                        content);
                reply = Command.create(replyString, cmd.getSource());
            } else if (cmd.is("matchmessage")) {
                id = idManager.getId();
                content = cmd.extractCommand().toString();
                String replyString = String.format("matchmessage %d %s", id,
                        content);
                reply = Command.create(replyString, cmd.getSource());
            } else if (cmd.is("announce")) {
                reply = cmd;
            } else {
                reply = cmd;
            }
            
            if (null != reply) logPost(reply); // change later?
        } finally {
            chatLock.unlock();
        }
        
        return reply;
    }
}
