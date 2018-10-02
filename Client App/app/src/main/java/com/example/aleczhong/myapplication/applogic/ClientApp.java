package com.example.aleczhong.myapplication.applogic;

import android.util.Log;

import com.example.aleczhong.myapplication.activities.PrototypeChatRoomActivity;
import com.facebook.AccessToken;

import java.util.Arrays;

public class ClientApp {

    private static ClientApp currentApp = null;
    private ServerConnection serverConnection;
    private AccessToken token;

    private PrototypeChatRoomActivity.DisplayAreaListener displayListener;

    private ClientApp() {
        serverConnection = new ServerConnection(this);
        token = null;
    }

    public void joinChatRoom() {
        serverConnection.joinChatRoom();
    }

    String joinString(String[] tokens) {
        String output = tokens[0];
        for (int i = 1; i < tokens.length; ++i) {
            output += " " + tokens[i];
        }
        return output;
    }

    void handleInput(String msg) {
        String[] tokens = msg.split(" ");
        if (tokens[0].equals("chat")) {
            displayListener.displayMessage(tokens[2]
                    + ": " + joinString(Arrays.copyOfRange(tokens, 3, tokens.length)));
        } else if (tokens[0].equals("announce")) {
            displayListener.displayMessage(joinString(
                    Arrays.copyOfRange(tokens, 2, tokens.length)));
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

    public static ClientApp getClientApp() {
        if (null == currentApp) currentApp = new ClientApp();
        return currentApp;
    }

    public static void log(String msg) {
        Log.d("APP_DEBUG_TAG", msg);
    }
}
