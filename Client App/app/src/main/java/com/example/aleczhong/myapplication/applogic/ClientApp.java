package com.example.aleczhong.myapplication.applogic;

import android.util.Log;

import com.facebook.AccessToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ClientApp {

    private static ClientApp currentApp = null;
    private ServerConnection serverConnection;
    private AccessToken token;
    private List<DelayedReturn> waitingFunctions;

//    private PlayerViewActivity.DisplayAreaListener displayListener;
    private ReentrantLock waitFuncLock;

    private String nickname;
    //private int[] scores = new int[]{5,5,5,5,5,5,5,5,5,5,5,5,5,5};
    private String userID;
    private final List<Question> questions;
    private final List<ChatMessage> messages;
    private MatchDisplayInterface displayInterface;
    private String currentMatchAnnouncement;
    private String currentServerAnnouncement;

    private ClientApp() {
        serverConnection = new ServerConnection(this);
        token = null;
        waitFuncLock = new ReentrantLock();
        waitingFunctions = new ArrayList<>();
        questions = Arrays.asList
                (
                        new Question("Question 1"),
                        new Question("Question 2"),
                        new Question("Question 3"),
                        new Question("Question 4"),
                        new Question("Question 5"),
                        new Question("Question 6"),
                        new Question("Question 7"),
                        new Question("Question 8"),
                        new Question("Question 9"),
                        new Question("Question 10"),
                        new Question("Question 11"),
                        new Question("Question 12"),
                        new Question("Question 13"),
                        new Question("Question 14"),
                        new Question("Question 15")
                );
        messages = new ArrayList<>();
        displayInterface = null;
    }

    public void addMatchDisplayInterface(MatchDisplayInterface displayInterface) {
        this.displayInterface = displayInterface;
    }

    public void updateAnnouncements() {
        if (null != displayInterface) displayInterface.matchAnnouncement(currentMatchAnnouncement);
    }

    public void joinMatch(String type, DelayedReturn waitFunc) {
        addWaitFunc(waitFunc);
        serverConnection.joinChatRoom(type);
    }

    String joinString(String[] tokens) {
        String output = tokens[0];
        for (int i = 1; i < tokens.length; ++i) {
            output += " " + tokens[i];
        }
        return output;
    }

    String removeTailToString(String[] tokens, int start) {
        return joinString(Arrays.copyOfRange(tokens, start, tokens.length));
    }

    public void sendData(String data){
        serverConnection.send(data);
    }

    private void addWaitFunc(DelayedReturn waitFunc) {
        waitFuncLock.lock();
        try {
            waitingFunctions.add(waitFunc);
        } finally {
            waitFuncLock.unlock();
        }
    }

    private void checkWaitingFunctions(String msg) {
        waitFuncLock.lock();
        try {
            Iterator<DelayedReturn> wfIterator = waitingFunctions.iterator();
            while (wfIterator.hasNext()) {
                DelayedReturn waitFunc = wfIterator.next();
                if (waitFunc.check(msg)) {
                    wfIterator.remove();
                }
            }
        } finally {
            waitFuncLock.unlock();
        }
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    private ChatMessage convertChatMessage(String[] msg) {
        int id = Integer.parseInt(msg[1]);
        MessageType type;

        String user = msg[2];
        if (user.equals(userID)) {
            type = MessageType.SELF;
        } else {
            type = MessageType.OPPONENT;
        }

        String content = removeTailToString(msg, 3);
        return new ChatMessage(content, id, type);
    }

    private ChatMessage convertServerMessage(String[] msg) {
        int id = Integer.parseInt(msg[1]);
        MessageType type = MessageType.SERVER;
        String content = removeTailToString(msg, 2);
        return new ChatMessage(content, id, type);
    }

    private void sendMatchAnnouncement(String[] tokens) {
        String msg = removeTailToString(tokens, 1);
        currentMatchAnnouncement = msg;
        updateAnnouncements();
    }

    private void addMessage(ChatMessage msg) {
        messages.add(msg);
        if (null != displayInterface) displayInterface.messageUpdate();
    }

    void handleInput(String msg) {
        checkWaitingFunctions(msg);
        String[] tokens = msg.split(" ");
        if (tokens[0].equals("chat")) {
            addMessage(convertChatMessage(tokens));
        } else if (tokens[0].equals("announce")) {
            sendMatchAnnouncement(tokens);
        } else if (tokens[0].equalsIgnoreCase("matchmessage")) {
            addMessage(convertServerMessage(tokens));
//        } else if (tokens[0].equalsIgnoreCase("serverannounce")) {
//            displayListener.displayMessage("SERVER ANNOUNCEMENT: " + joinString(
//                    Arrays.copyOfRange(tokens, 1, tokens.length)
//            ));
        } else if (tokens[0].equalsIgnoreCase("nickname")){
            nickname = tokens[1];
        } //else if (tokens[0].equalsIgnoreCase("setQuestions")){
            //for(int i = 1; i < 16;i++){
            //    scores[i-1] = Integer.parseInt(tokens[i]);
            //}
        //}
    }

//    public void setMessageListener(PlayerViewActivity.DisplayAreaListener listener) {
//        displayListener = listener;
//    }

    public void sendChatMessage(String message) {
        String sendString = "chat " + message;
        serverConnection.send(sendString);
    }

    public boolean establishServerConnection() {
        return serverConnection.connect();
    }

    public void setAccessToken(AccessToken token) {
        this.token = token;
    }

    public void login(DelayedReturn waitFunction) {
        addWaitFunc(waitFunction);
        serverConnection.login(token.getUserId());
    }

    public void login(String id, DelayedReturn wf)
    {
        addWaitFunc(wf);
        serverConnection.login(id);
        userID = id;
    }

    public void updateQuestions(List<Question> newQuestions) {
        for (int i = 0; i < questions.size(); ++i) {
            questions.get(i).setScore(newQuestions.get(i).getScore());
            currentApp.sendData("updateQuestion "+Integer.toString(i+1)+" "+newQuestions.get(i).getScore());
        }
    }

    public List<Question> getPreferenceQuestions() {
        return questions;
    }

    public static ClientApp getClientApp() {
        if (null == currentApp) currentApp = new ClientApp();
        return currentApp;
    }

    public static void log(String msg) {
        Log.d("APP_DEBUG_TAG", msg);
    }
}
