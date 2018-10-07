/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.util.Stack;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Demo
 */
public class IdManager
{
    private static final int MAX_ID = Integer.MAX_VALUE;
    
    private int nextID;
    private final Stack<Integer> reusableIDs;
    private final ReentrantLock lock;
    
    public IdManager() {
        nextID = 0;
        reusableIDs = new Stack<>();
        lock = new ReentrantLock();
    }
    
    public int getId() {
        lock.lock();
        try {
            if (!reusableIDs.isEmpty()) return reusableIDs.pop();
            else if (nextID > MAX_ID) throw new IllegalStateException("No new ID's");
            else return nextID++;
        } finally {
            lock.unlock();
        }
        
    }
    
    public void recycleID(int id) {
        lock.lock();
        try {
            reusableIDs.add(id);
        } finally {
            lock.unlock();
        }
    }
}
