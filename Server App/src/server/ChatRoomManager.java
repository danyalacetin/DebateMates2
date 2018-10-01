/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Demo
 */
class ChatRoomManager
{
    private final Map<Integer, ChatRoom> rooms;
    private final IdManager idManager;
    
    ChatRoomManager() {
        rooms = new HashMap<>();
        idManager = new IdManager();
    }
    
    int createChatRoom() {
        int id = idManager.getId();
        ChatRoom newRoom = new ChatRoom(5);
        rooms.put(id, newRoom);
        
        return id;
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
        
        return createChatRoom();
    }
}
