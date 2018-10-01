/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.Stack;

/**
 *
 * @author Demo
 */
class IdManager
{
    private static final int MAX_ID = Integer.MAX_VALUE;
    
    private int nextID;
    private final Stack<Integer> reusableIDs;
    
    IdManager() {
        nextID = 0;
        reusableIDs = new Stack<>();
    }
    
    int getId() {
        if (!reusableIDs.isEmpty()) return reusableIDs.pop();
        else if (nextID > MAX_ID) throw new IllegalStateException("No new ID's");
        else return nextID++;
    }
    
    void recycleID(int id) {
        reusableIDs.add(id);
    }
}
