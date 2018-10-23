package server;

import connections.ConnectionManager;
import utilities.Command;
import match.MatchManager;
import database.Database;
import java.util.function.Consumer;

/**
 * Main controlling class for the server application.
 * @author DebateMates
 */
public class Server implements ServerWorker {
    
    private static Server currentInstance = null;
    
    private final WorkerManager workerManager;
    private final CommandProcessor commandProcessor;
    private final ConnectionManager connectionManager;
    private final MatchManager matchManager;
    
    private final Consumer<String> logger;
    private final Consumer<String> errorLogger;
    
    private final Database database;
    
    /**
     * Constructor for the server class.
     * @param logger function which handles printing output to the GUI
     */
    private Server(Consumer<String> logger, Consumer<String> errorLogger) {
        
        database = new Database();  // Testing Purposes Only
        
        workerManager = new WorkerManager();
        connectionManager = new ConnectionManager(workerManager::addWorker);
        commandProcessor = new CommandProcessor();
        matchManager = new MatchManager();
        this.logger = logger;
        this.errorLogger = errorLogger;
    }
    
    /**
     * @return the database object for the server
     */
    public Database getDB(){
        return database;
    }
    
    /**
     * @return the manager responsible for running matches
     */
    MatchManager getMatchManager() {
        return matchManager;
    }
    
    /**
     * @return manager responsible for managing the worker threads
     */
    WorkerManager getWorkerManager() {
        return workerManager;
    }
    
    /**
     * @return manager responsible for managing the client connections
     */
    ConnectionManager getConnectionManager() {
        return connectionManager;
    }
    
    /**
     * Updates the nickname for the user represented by the given worker.
     * @param nickname new nickname
     * @param source worker handling the user
     */
    void updateNickname(String nickname, Worker source){
        database.updateItem(source.getLogin(), "NICKNAME", "'"+nickname+"'");
        source.send("nickname "+nickname);
    }
    
    /**
     * Updates the score for a given question for a given user in the database
     * @param questionNum number of the question to change
     * @param answerNum new score value
     * @param source worker thread associated with the user
     */
    void updateQuestion(String questionNum, String answerNum, Worker source){
        database.updateItem(source.getLogin(), "QUESTION"+questionNum, answerNum);
    }
    
    /**
     * sends the questions and scores to the given user
     * @param source worker associated with the worker
     */
    void getQuestions(Worker source){
        source.send("setQuestions " + database.getQuery(source.getLogin(), "QUESTION1") + " "
                                    + database.getQuery(source.getLogin(), "QUESTION2") + " "
                                    + database.getQuery(source.getLogin(), "QUESTION3") + " "
                                    + database.getQuery(source.getLogin(), "QUESTION4") + " "
                                    + database.getQuery(source.getLogin(), "QUESTION5") + " "
                                    + database.getQuery(source.getLogin(), "QUESTION6") + " "
                                    + database.getQuery(source.getLogin(), "QUESTION7") + " "
                                    + database.getQuery(source.getLogin(), "QUESTION8") + " "
                                    + database.getQuery(source.getLogin(), "QUESTION9") + " "
                                    + database.getQuery(source.getLogin(), "QUESTION10") + " "
                                    + database.getQuery(source.getLogin(), "QUESTION11") + " "
                                    + database.getQuery(source.getLogin(), "QUESTION12") + " "
                                    + database.getQuery(source.getLogin(), "QUESTION13") + " "
                                    + database.getQuery(source.getLogin(), "QUESTION14") + " "
                                    + database.getQuery(source.getLogin(), "QUESTION15")
        );
    }
    
    /**
     * Handles login for given user
     * @param id id given to the user from FaceBook API
     * @param source worker associated with the user
     */
    void loginUser(String id, Worker source) {
        workerManager.loginWorker(id, source);
        //Checks if user is new
        if(database.getQuery(id, "FACEBOOKID") == null) {
            //Adds user to database
            database.addItem(id, "Nickname", 0, 0, 1000, 1);
        }
        else {
            database.updateItem(id, "ONLINESTATUS", "1");
        }
    }
    
    /**
     * Handles logout for the given user
     * @param source worker associated with the user
     */
    void logoutUser(Worker source) {
        
        database.updateItem(source.getLogin(), "ONLINESTATUS", "0");
    }
    
    /**
     * Opens the server, searching for new connections
     */
    public void startServer() {
        System.out.println("Server started.");
        database.establishConnection();
        connectionManager.start();
    }
    
    /**
     * Stops the server
     * *not implemented and tested yet*
     */
    void stopServer() {
        System.out.println("Closing server.");
        connectionManager.stop();
        workerManager.kickAll();
        workerManager.shutdown();
    }
    
    /**
     * Used for testing, called by the command processor when a command that
     * is not yet implemented is called from the gui
     */
    void unsupportedCommand() {
        System.out.println("Not yet implemented");
    }
    
    /**
     * Displays server connection details
     * *not yet implemented*
     */
    void viewServerConnectionInfo() {
        String output = "";
        
        output += connectionManager.getServerDetails();
        output += "    Open Connections: " + workerManager.getNumWorkers();
        
        System.out.println(output);
    }

    /**
     * Mainly for testing, displays information on currently open matches
     */
    void viewChatRoomInfo()
    {
        System.out.println(matchManager.getMatchInfo());
    }
    
    // ========================================================================
    // Override Methods
    
    /**
     * Processes a given command with the set command processor
     * @param command command to be processed
     */
    @Override
    public void processCommand(Command command)
    {
        commandProcessor.processCommand(command);
    }
    
    // ========================================================================
    // Initialisation Methods
    
    /**
     * contains functions that should be run on startup
     */
    private void initialise() {
        commandProcessor.initialise();
        workerManager.initialise();
    }
    
    /**
     * Called to initialise the Server class
     * @param loggerFunction 
     * @param errorLoggerFunction 
     */
    public static void initialiseInstance(Consumer<String> loggerFunction,
            Consumer<String> errorLoggerFunction) {
        if (null == currentInstance) {
            currentInstance = new Server(loggerFunction, errorLoggerFunction);
            currentInstance.initialise();
        }
    }
    
    /**
     * @return current Server instance
     */
    public static Server getInstance() {
        return  currentInstance;
    }
    
    // ========================================================================
}
