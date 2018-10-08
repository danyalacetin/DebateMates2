package com.example.aleczhong.myapplication.applogic;

import android.util.Log;

import com.example.aleczhong.myapplication.R;
import com.example.aleczhong.myapplication.activities.PrototypeChatRoomActivity;
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

    private PrototypeChatRoomActivity.DisplayAreaListener displayListener;
    private ReentrantLock waitFuncLock;

    public String nickname;

    private ClientApp() {
        serverConnection = new ServerConnection(this);
        token = null;
        waitFuncLock = new ReentrantLock();
        waitingFunctions = new ArrayList<>();
    }

    public void joinChatRoom(DelayedReturn waitFunc) {
        addWaitFunc(waitFunc);
        serverConnection.joinChatRoom();
    }

    String joinString(String[] tokens) {
        String output = tokens[0];
        for (int i = 1; i < tokens.length; ++i) {
            output += " " + tokens[i];
        }
        return output;
    }

    public void sendData(String data){
        serverConnection.sendString(data);
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

    void handleInput(String msg) {
        checkWaitingFunctions(msg);
        String[] tokens = msg.split(" ");
        if (tokens[0].equals("chat")) {
            displayListener.displayMessage(tokens[2]
                    + ": " + joinString(Arrays.copyOfRange(tokens, 3, tokens.length)));
        } else if (tokens[0].equals("announce")) {
            displayListener.displayMessage(joinString(
                    Arrays.copyOfRange(tokens, 2, tokens.length)));
        } else if (tokens[0].equalsIgnoreCase("serverannounce")) {
            displayListener.displayMessage("SERVER ANNOUNCEMENT: " + joinString(
                    Arrays.copyOfRange(tokens, 1, tokens.length)
            ));
        } else if (tokens[0].equalsIgnoreCase("nickname")){
            nickname = tokens[1];
        }
    }

    public void setMessageListener(PrototypeChatRoomActivity.DisplayAreaListener listener) {
        displayListener = listener;
    }

    public void sendChatMessage(String message) {
        String sendString = "chat " + message;
        serverConnection.sendString(sendString);
    }

    public boolean establishServerConnection() {
        return serverConnection.connect();
    }

    public void setAccessToken(AccessToken token) {
        this.token = token;
    }

    public void login() {
        serverConnection.login(token.getUserId());
    }

    public void login(String id, DelayedReturn wf)
    {
        addWaitFunc(wf);
        serverConnection.login(id);

    }

    public static ClientApp getClientApp() {
        if (null == currentApp) currentApp = new ClientApp();
        return currentApp;
    }

    public static void log(String msg) {
        Log.d("APP_DEBUG_TAG", msg);
    }
}
