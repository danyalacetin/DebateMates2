/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
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
            if (cmd.isCommand("chat")) {
                id = idManager.getId();
                user = cmd.getSource().getLogin();
                content = cmd.nextCommand().toString();
                String replyString = String.format("chat %d %s %s", id, user,
                        content);
                reply = new Command(replyString, cmd.getSource());
            } else if (cmd.isCommand("announce")) {
                id = idManager.getId();
                content = cmd.nextCommand().toString();
                String replyString = String.format("announce %d %s", id,
                        content);
                reply = new Command(replyString, cmd.getSource());
            } else {
                reply = null;
            }
            
            if (null != reply) logPost(reply); // change later?
        } finally {
            chatLock.unlock();
        }
        
        return reply;
    }
}
