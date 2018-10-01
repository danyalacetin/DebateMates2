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
import java.util.function.Consumer;

/**
 *
 * @author Demo
 */
class WorkerManager {
    
    private final Consumer<Command> requestHandler;
    private final List<Worker> workers;
    
    WorkerManager(Consumer<Command> requestHandler) {
        this.requestHandler = requestHandler;
        workers = new ArrayList<>();
    }
    
    void addWorker(Socket socket) {
        try {
            Worker newWorker = new Worker(socket, requestHandler);
            workers.add(newWorker);
            newWorker.run();
        } catch (IOException ex) {
            
        }
    }
    
}
