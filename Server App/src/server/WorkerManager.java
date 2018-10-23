/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import connections.ClientConnection;
import utilities.Command;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import utilities.SyncListWrapper;

/**
 *
 * @author Demo
 */
class WorkerManager {
    
    private final SyncListWrapper<Worker> workers;
    private final SyncListWrapper<Worker> reconnecting;
    private final ExecutorService executor;
    private Server server;
    
    WorkerManager() {
        workers = new SyncListWrapper<>();
        reconnecting = new SyncListWrapper<>();
        executor = Executors.newCachedThreadPool();
        server = null;
    }
    
    void initialise() {
        server = Server.getInstance();
    }
    
    int getNumWorkers() {
        return workers.size();
    }
    
    void addWorker(ClientConnection connection) {
        Worker newWorker = new Worker(connection);
        workers.add(newWorker);
        executor.execute(newWorker);
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
    
    void logoutWorker(Worker worker) {
        if (worker.inMatch()) {
            Command cmd = Command.create("leave", worker);
            server.processCommand(cmd);
        }
        
        System.out.println(worker.getLogin() + " logged out.");
        worker.setLogin(null);
    }
    
    void loginWorker(String id, Worker worker) {
        System.out.println("User logged in as: " + id);
        worker.setLogin(id);
        worker.send(ServerConstants.LOGIN_SUCCESS);
    }
    
    void sendBroadcast(Command cmd) {
        String sendString = "serverannounce " + cmd.toString();
        workers.forEach(worker -> worker.send(sendString));
    }
    
    void shutdown() {
        executor.shutdown();
    }
}
