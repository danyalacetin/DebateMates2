/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    
    String getChatRoomInfo() {
        String info = "";
        if (rooms.isEmpty()) {
            info += "There are no chat rooms open";
        } else {
            info += "Chat Room info:";
            for (Entry<Integer, ChatRoom> entry : rooms.entrySet())
            {
                info += "    >> ID: " + entry.getKey() + ", Number of members: "
                        + entry.getValue().getNumMembers() + "\n";
            }
        }
        return info;
    }
    
    int createChatRoom() {
        int id = idManager.getId();
        ChatRoom newRoom = new ChatRoom(id, 5);
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
    
    void sendAnnouncement(Command msg) {
        int room = msg.getSource().getChatRoom();
        rooms.get(room).sendAnnouncement(msg);
    }
    
    void sendMessage(Command msg) {
        int room = msg.getSource().getChatRoom();
        rooms.get(room).sendMessage(msg);
    }
    
    void joinChatRoom(Worker player, int id) {
        if (-1 != id) {
            if (rooms.get(id).addMember(player)) // deal with null pointer
                player.setChatRoom(id);
        } 
    }
    
    void leaveChat(Worker player) {
        rooms.get(player.getChatRoom()).removeMember(player);
    }
    
    int findChatRoom() {
        for (Entry<Integer, ChatRoom> entry : rooms.entrySet()) {
            if (!entry.getValue().isFull()) return entry.getKey();
        }
        
        return createChatRoom();
    }
}
