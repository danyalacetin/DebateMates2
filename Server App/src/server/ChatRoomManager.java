/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.HashMap;
import java.util.Map;

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
    
    void createChatRoom() {
        ChatRoom newRoom = new ChatRoom();
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
        
    }
    
    int findChatRoom() {
        return -1;
    }
}
