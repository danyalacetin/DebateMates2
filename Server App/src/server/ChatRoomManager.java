/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

/**
 *
 * @author Demo
 */
class ChatRoomManager
{
    private final Map<Integer, ChatRoom> rooms;
    private final IdManager idManager;
    
    private final Consumer<Command> commandProcessor;
    
    ChatRoomManager(Consumer<Command> commandProcessor) {
        rooms = new HashMap<>();
        idManager = new IdManager();
        this.commandProcessor = commandProcessor;
    }
    
    void createChatRoom() {
        ChatRoom newRoom = new ChatRoom(5, commandProcessor);
        rooms.put(idManager.getId(), newRoom);
    }
    
    void removeChatRoom(int id) {
        rooms.get(id).close();
        rooms.remove(id);
        idManager.recycleID(id);
    }
    
    void joinChatRoom(Worker player) {
        joinChatRoom(player, findChatRoom());
    }
    
    void sendMessage(int room, Command msg) {
        rooms.get(room).sendMessage(msg);
    }
    
    void joinChatRoom(Worker player, int id) {
        if (-1 != id) rooms.get(id).addMember(player);
    }
    
    int findChatRoom() {
        for (Entry<Integer, ChatRoom> entry : rooms.entrySet()) {
            if (!entry.getValue().isFull()) return entry.getKey();
        }
        return -1;
    }
}
