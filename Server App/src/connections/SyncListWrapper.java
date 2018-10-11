/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connections;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Demo
 */
public class SyncListWrapper<T>
{
    private final List<T> data;
    private final ReentrantLock lock;
    
    public SyncListWrapper(List<T> data)
    {
        this.data = data;
        lock = new ReentrantLock();
    }
    
    public void remove(T item)
    {
        lock.lock();
        try
        {
            data.remove(item);
        }
        finally
        {
            lock.unlock();
        }
    }
    
    public void add(T item)
    {
        lock.lock();
        try
        {
            data.add(item);
        }
        finally
        {
            lock.unlock();
        }
    }
}
