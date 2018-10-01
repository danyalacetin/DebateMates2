package server;

import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Server implements ServerWorker, ConnectionInitiator {
    
    private static Server currentInstance = null;
    
    private final WorkerManager workerManager;
    private final CommandProcessor commandProcessor;
    private final ConnectionFinder connectionFinder;
    private final MatchManager matchManager;
    private final ChatRoomManager chatRoomManager;
    
    private final ExecutorService executor;
    private final Consumer<String> logFunction;
    
    private Server(Consumer<String> logFunction) {
        
        executor = Executors.newCachedThreadPool();
        
        connectionFinder = new ConnectionFinder();
        commandProcessor = new CommandProcessor();
        workerManager = new WorkerManager();
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
        source.send("login success");
    }
    
    void logOutUser(String id, Worker source) {
        logFunction.accept(id + " logged out.");
        source.setLogin(null);
    }
    
    public void startServer() {
        logFunction.accept("Server started.");
        executor.execute(connectionFinder::start);
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
    
    @Override
    public void newConnection(Socket socket) {
        serverLog("Accept connection from " + socket);
        executor.execute(() -> workerManager.addWorker(socket));
    }
    
    // ========================================================================
    // Override Methods
    
    @Override
    public void serverLog(String str) {
        logFunction.accept(str);
    }

    @Override
    public void processCommand(Command command)
    {
        commandProcessor.processCommand(command);
    }
    
    // ========================================================================
    // Initialisation Methods
    
    private void initialise() {
        connectionFinder.initialise();
        commandProcessor.initialise();
    }
    
    public static void initialiseInstance(Consumer<String> loggerFunction) {
        if (null == currentInstance) {
            currentInstance = new Server(loggerFunction);
            currentInstance.initialise();
        }
    }
    
    public static Server getInstance() {
        return  currentInstance;
    }
    
    // ========================================================================
}
