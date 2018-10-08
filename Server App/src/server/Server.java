package server;

import database.Database;
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
    
    private final ExecutorService executor;
    private final Consumer<String> logFunction;
    
    private final Database database;
    
    private Server(Consumer<String> logFunction) {
        
        executor = Executors.newCachedThreadPool();
        database = new Database(); //Testing Purposes Only
        
        connectionFinder = new ConnectionFinder();
        commandProcessor = new CommandProcessor();
        workerManager = new WorkerManager();
        matchManager = new MatchManager();
        this.logFunction = logFunction;
    }
    
    public Database getDB(){
        return database;
    }
    
    MatchManager getMatchManager() {
        return matchManager;
    }
    
    WorkerManager getWorkerManager() {
        return workerManager;
    }
    
    void updateNickname(String nickname, Worker source){
        database.updateItem(source.getLogin(), "NICKNAME", "'"+nickname+"'");
        source.send("nickname "+nickname);
    }
    
    void updateQuestion(String questionNum, String answerNum, Worker source){
        database.updateItem(source.getLogin(), "QUESTION"+questionNum, answerNum);
    }
    
    void getQuestions(Worker source){
        source.send("setQuestions " +database.getQuery(source.getLogin(), "QUESTION1") + " "
                                    +database.getQuery(source.getLogin(), "QUESTION2") + " "
                                    +database.getQuery(source.getLogin(), "QUESTION3") + " "
                                    +database.getQuery(source.getLogin(), "QUESTION4") + " "
                                    +database.getQuery(source.getLogin(), "QUESTION5") + " "
                                    +database.getQuery(source.getLogin(), "QUESTION6") + " "
                                    +database.getQuery(source.getLogin(), "QUESTION7") + " "
                                    +database.getQuery(source.getLogin(), "QUESTION8") + " "
                                    +database.getQuery(source.getLogin(), "QUESTION9") + " "
                                    +database.getQuery(source.getLogin(), "QUESTION10") + " "
                                    +database.getQuery(source.getLogin(), "QUESTION11") + " "
                                    +database.getQuery(source.getLogin(), "QUESTION12") + " "
                                    +database.getQuery(source.getLogin(), "QUESTION13") + " "
                                    +database.getQuery(source.getLogin(), "QUESTION14") + " "
                                    +database.getQuery(source.getLogin(), "QUESTION15")
        );
    }
    
    void logInUser(String id, Worker source) {
        serverLog("User logged in as: " + id);
        source.setLogin(id);
        source.send(ServerConstants.LOGIN_SUCCESS);
        //Checks if user is new
        if(database.getQuery(id, "FACEBOOKID") == null){
            //Adds user to database
            database.addItem(id, "Nickname", 0, 0, 1000, 1);
        }
        else{
            database.updateItem(id, "ONLINESTATUS", "1");
        }
    }
    
    void logOutUser(Worker source) {
        if (source.inMatch()) {
            Command cmd = new Command("leave", source);
            processCommand(cmd);
        }
        serverLog(source.getLogin() + " logged out.");
        database.updateItem(source.getLogin(), "ONLINESTATUS", "0");
        source.setLogin(null);
    }
    
    public void startServer() {
        serverLog("Server started.");
        database.establishConnection();
        executor.execute(connectionFinder::start);
    }
    
    void stopServer() {
//        serverLog("Closing server.");
//        connectionFinder.stop();
//        executor.shutdown();
    }
    
    void unsupportedCommand() {
        serverLog("Not yet implemented");
    }
    
    void printServerConnectionStatus() {
        
    }
    
    void acceptClients() {
//        connectionFinder.open();
    }
    
    void rejectClients() {
//        connectionFinder.close();
    }

    void viewChatRoomInfo()
    {
        serverLog(matchManager.getMatchInfo());
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
        serverLog("Accept connection from " + socket.getInetAddress());
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
