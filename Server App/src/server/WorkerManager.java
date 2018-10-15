/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import utilities.Command;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import utilities.SyncListWrapper;

/**
 *
 * @author Demo
 */
class WorkerManager {
    
    private final SyncListWrapper<Worker> workers;
    
    WorkerManager() {
        workers = new SyncListWrapper<>();
    }
    
    void addWorker(Worker worker) {
        workers.add(worker);
    }
    
    void kickAll() {
        workers.forEach(this::removeWorker);
    }
    
    void removeWorker(Worker worker) {
        worker.shutdown();
        workers.remove(worker);
    }
    
    void sendBroadcast(Command cmd) {
        String sendString = "serverannounce " + cmd.toString();
        workers.forEach(worker -> worker.send(sendString));
    }
}
