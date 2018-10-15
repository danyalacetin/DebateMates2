/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import connections.ClientConnection;
import utilities.Command;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.locks.ReentrantLock;
import utilities.SyncListWrapper;

/**
 *
 * @author Demo
 */
class WorkerManager {
    
    private final SyncListWrapper<Worker> workers;
    private final ExecutorService executor;
    
    WorkerManager() {
        workers = new SyncListWrapper<>();
        executor = Executors.newCachedThreadPool();
    }
    
    int getNumWorkers() {
        return workers.size();
    }
    
    void addWorker(ClientConnection connection) {
        Worker newWorker = new Worker(connection);
        workers.add(newWorker);
        executor.execute(newWorker::run);
    }
    
    void kickAll() {
        workers.forEach(worker -> {
                    removeWorker(worker);
                    worker.shutdown();
                });
    }
    
    void removeWorker(Worker worker) {
        worker.shutdown();
        workers.remove(worker);
    }
    
    void sendBroadcast(Command cmd) {
        String sendString = "serverannounce " + cmd.toString();
        workers.forEach(worker -> worker.send(sendString));
    }
    
    void shutdown() {
        executor.shutdown();
    }
}
