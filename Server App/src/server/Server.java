package server;

import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Server {
    
    private final WorkerManager workerManager;
    private final CommandProcessor commandManager;
    private final ConnectionFinder connectionFinder;
    private final MatchManager matchManager;
    private final ChatRoomManager chatRoomManager;
    
    private final ExecutorService executor;
    private final Consumer<String> logFunction;
    
    public Server(int serverPort, Consumer<String> logFunction) {
        
        executor = Executors.newCachedThreadPool();
        
        connectionFinder = new ConnectionFinder();
        connectionFinder.registerConnectionFunction(this::newConnection);
        
        commandManager = new CommandProcessor(this);
        workerManager = new WorkerManager(commandManager::processCommand);
        matchManager = new MatchManager();
        chatRoomManager = new ChatRoomManager();
        
        this.logFunction = logFunction;
    }
    
    ChatRoomManager getChatRoomManager() {
        return chatRoomManager;
    }
    
    void logInUser(String id, Worker source) {
        logFunction.accept("User logged in as: " + id);
        source.setLogin(id);
    }
    
    void logOutUser(String id, Worker source) {
        logFunction.accept(id + " logged out.");
        source.setLogin(null);
    }
    
    public void startServer() {
        logFunction.accept("Server started.");
        executor.execute(connectionFinder::start);
    }
    
    void printLog(String[] str) {
        printLog(String.join(" ", str));
    }
    
    void printLog(String str) {
        logFunction.accept(str);
    }
    
    void stopServer() {
        logFunction.accept("Closing server.");
        connectionFinder.stop();
        executor.shutdown();
    }
    
    void printServerConnectionStatus() {
        
    }
    
    void acceptClients() {
        connectionFinder.open();
    }
    
    void rejectClients() {
        connectionFinder.close();
    }
    
    private void newConnection(Socket socket) {
        printLog("Accept connection from " + socket);
        executor.execute(() -> workerManager.addWorker(socket));
    }
}
