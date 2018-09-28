package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends Thread {
    private final int serverPort;
    private ServerSocket serverSocket;
    
    private final ArrayList<ServerWorker> workerList= new ArrayList<>();
    
    public Server(int serverPort) {
        this.serverPort = serverPort;
        serverSocket = null;
    }
    
    public List<ServerWorker> getWorkerList() {
        return workerList;
    }
    
    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(serverPort);
            while(true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accept connection from " + clientSocket);
                ServerWorker worker = new ServerWorker(this, clientSocket);
                workerList.add(worker);
                worker.start();
            }
        } catch (IOException e) {
            System.out.println("Server Closed");
        }
        
        serverSocket = null;
    }

    void removeWorker(ServerWorker serverWorker) {
        workerList.remove(serverWorker);
    }
    
    /**
     * Temporary
     */
    public void end()
    {
        try {
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
