package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

public class ServerWorker extends Thread {
    
    private final Socket clientSocket;
    private final Server server;
    private OutputStream outputStream;
    
    private String username = null;
    private boolean loginStatus = false;
    private boolean usernameTaken = false;
    
    public ServerWorker(Server server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
    }
    
//-----Handles-client-information-sent/recieved---------------------------------
    private void handleClientSocket() throws InterruptedException, IOException {

        InputStream inputStream = clientSocket.getInputStream();
        this.outputStream = clientSocket.getOutputStream();
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        
        send("Connected\n");
        System.out.print(clientSocket.getLocalSocketAddress()+" Connected\n");

        //Reads what the client sent        
        while((line = reader.readLine()) != null) {
            String[] tokens = StringUtils.split(line);
            if(tokens != null && tokens.length > 0){
                String cmd = tokens[0];
                
                //Checks if string was a disconnect command
                if("quit".equalsIgnoreCase(cmd) ||
                   "disconnect".equalsIgnoreCase(cmd) ||
                   "leave".equalsIgnoreCase(cmd)) {
                    if(loginStatus == true) {
                        handleLogout();
                    }
                    send("Disconnected\n");
                    System.out.print(clientSocket.getLocalSocketAddress()+" Disconnected\n");
                    break;
                }
                //Checks if string was a login command
                else if("login".equalsIgnoreCase(cmd)) {
                    handleLogin(tokens);
                }
                //Checks if string was a logout command
                else if("logout".equalsIgnoreCase(cmd)) {
                    handleLogout();
                }
                //Assumes the user input is a message, not a command.
                else {
                    handleChat(line);
                }
            }
        }
        //closes the socket once logged out
        clientSocket.close();
    }
    
//-----Handles-the-client-login-------------------------------------------------
    private void handleLogin(String[] tokens) throws IOException {
        List<ServerWorker> workerList = server.getWorkerList();
        
        if(tokens.length == 2) {
            
            for(ServerWorker worker : workerList) {
                if(tokens[1].equalsIgnoreCase(worker.username)) {
                    usernameTaken = true;
                }
            }
            //Checks if user is already logged in
            if(loginStatus) {
                send("You Are Already Logged In\n");
            }
            //Checks if the username is already taken
            else if(usernameTaken) {
                send("Username Already Taken\n");
                usernameTaken = false;
            }
            //Logs in and broadcasts log in messages
            else {   
                loginStatus = true;    
                username = tokens[1];
                
                send("Logged In Successfully\n");
                System.out.print(clientSocket.getLocalSocketAddress()+" Logged In As '"+username+"'\n");

//                for(ServerWorker worker : workerList) {
//                    if(worker.loginStatus && !worker.username.equals(username)) {
//                        worker.send(username+" Is Online\n");
//                    }
//                }

                broadcast(username + " Is Online\n", username);
            }
        }
        //Assumes the login command was used incorrectly (Doesn't equal 2 tokens)
        else {
            send("Incorrect Usage Of 'Login' Command\n");
        }
    }
    
//-----Handles the client logout------------------------------------------------
    private void handleLogout() throws IOException{

        //checks if user is already logged out
        if(loginStatus == false) {
            send("You Are Already Logged Out\n");
        }
        //logs out
        else {
            server.removeWorker(this);
//            List<ServerWorker> workerList = server.getWorkerList();
            send("Logged Out Successfully\n");
            System.out.print(clientSocket.getLocalSocketAddress()+" Logged Out As "+username+"\n");
//            for(ServerWorker worker : workerList) {
//                if(!worker.username.equals(username)) {
//                    worker.send(username+" Went Offline\n");
//                }
//            }
            
            broadcast(username + " Went Offline\n", username);
            
            username = null;
            loginStatus = false;
        }
    }
    
//----Handles messages being sent to all clients--------------------------------
    private void handleChat(String line) throws IOException {
//        List<ServerWorker> workerList = server.getWorkerList();
        
        //checks if user is logged in
        if(loginStatus == true) {
            //sends message to the cleint that sent it
            send(("You: "+line+"\n"));
            System.out.print(username+": "+line+"\n");
//            for(ServerWorker worker : workerList) {
//                if(!worker.username.equals(username) && worker.username != null) {
//                    //sends message to all other clients
//                    worker.send(username+": "+line+"\n");
//                }
//            }
            
            broadcast(username + ": " + line + "\n", username);
        }
        //user is not logged in, therefore cannot send messages
        else {
            send("Must Log In Before Sending Messages\n");
        }
    }
    
    /**
     * Experimental method of a systems broadcast
     * @param msg - message to send
     * @param ignore - list of usernames to avoid sending to
     * @throws IOException if any send method calls throw an IOException
     */
    private void broadcast(String msg, String...ignore) throws IOException {
        for (ServerWorker worker : server.getWorkerList()) {
            if (worker.loginStatus
                    && !Arrays.asList(ignore).contains(worker.username)) {
                worker.send(msg);
            }
        }
    }
    
    private void send(String msg) throws IOException {
        outputStream.write(msg.getBytes());
    }
    
    @Override
    public void run() {
        try {
            handleClientSocket();
        } catch (InterruptedException | IOException ex) {
            System.out.print(clientSocket.getLocalSocketAddress() + " Logged "
                    + "Out As " + username + "\n");
            System.out.print(clientSocket.getLocalSocketAddress() + " "
                    + "Disconnected\n");
            server.removeWorker(this);
            
            List<ServerWorker> workerList = server.getWorkerList();
            for(ServerWorker worker : workerList) {
                if(!worker.username.equals(username)) {
                    try {
                        worker.send(username + " Went Offline\n");
                    } catch (IOException ex1) {
                        Logger.getLogger(ServerWorker.class.getName())
                                .log(Level.SEVERE, null, ex1);
                    }
                }
            }
        }
    }
}
