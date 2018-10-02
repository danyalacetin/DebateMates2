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
        serverLog("User logged in as: " + id);
        source.setLogin(id);
        source.send(ServerConstants.LOGIN);
    }
    
    void logOutUser(Worker source) {
        if (source.isInChat()) {
            Command cmd = new Command("leave", source);
            processCommand(cmd);
        }
        serverLog(source.getLogin() + " logged out.");
        source.setLogin(null);
    }
    
    public void startServer() {
        serverLog("Server started.");
        executor.execute(connectionFinder::start);
    }
    
    void stopServer() {
//        serverLog("Closing server.");
//        connectionFinder.stop();
//        executor.shutdown();
        serverLog("Not yet implemented"); // not implemented
    }
    
    void printServerConnectionStatus() {
        
    }
    
    void acceptClients() {
//        connectionFinder.open();
        serverLog("Not yet implemented"); // not implemented
    }
    
    void rejectClients() {
//        connectionFinder.close();
        serverLog("Not yet implemented"); // not implemented
    }

    void viewChatRoomInfo()
    {
        serverLog(chatRoomManager.getChatRoomInfo());
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
    
    @Override
    public void newConnection(Socket socket) {
        serverLog("Accept connection from " + socket);
        executor.execute(() -> workerManager.addWorker(socket));
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
