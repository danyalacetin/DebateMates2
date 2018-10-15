/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

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
    
    public SyncListWrapper()
    {
        this(new ArrayList<>());
    }
    
    public void lockSection(Runnable func) {
        lock.lock();
        try {
            func.run();
        } finally {
            lock.unlock();
        }
    }
    
    public void remove(T item)
    {
        lockSection(() -> data.remove(item));
    }
    
    public void forEach(Consumer<? super T> function) {
        lockSection(() -> data.forEach(function));
    }
    
    public void addAll(Collection<T> items)
    {
        lockSection(() -> data.addAll(items));
    }
    
    public void add(T item)
    {
        lockSection(() -> data.add(item));
    }
}
