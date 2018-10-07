/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Demo
 */
class WorkerManager {
    
    private final List<Worker> workers;
    private final ReentrantLock accessLock;
    
    WorkerManager() {
        workers = new ArrayList<>();
        accessLock = new ReentrantLock();
    }
    
    void addWorker(Socket socket) {
        accessLock.lock();
        Worker newWorker = null;
        try {
            newWorker = new Worker(socket);
            workers.add(newWorker);
        } catch (IOException ex) {
            
        } finally {
            accessLock.unlock();
            if (null != newWorker) newWorker.run();
        }
    }
    
    void removeWorker(Worker worker) {
        worker.shutdown();
        accessLock.lock();
        try {
            workers.remove(worker);
        } finally {
            accessLock.unlock();
        }
    }
    
    void sendBroadcast(Command cmd) {
        String sendString = "serverannounce " + cmd.toString();
        accessLock.lock();
        try {
            for (Worker worker : workers) worker.send(sendString);
        } finally {
            accessLock.unlock();
        }
    }
}
