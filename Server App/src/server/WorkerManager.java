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

/**
 *
 * @author Demo
 */
class WorkerManager {
    
    private final List<Worker> workers;
    
    WorkerManager() {
        workers = new ArrayList<>();
    }
    
    void addWorker(Socket socket) {
        try {
            Worker newWorker = new Worker(socket);
            workers.add(newWorker);
            newWorker.run();
        } catch (IOException ex) {
            
        }
    }
    
    void removeWorker(Worker worker) {
        worker.shutdown();
        workers.remove(worker);
    }
}
