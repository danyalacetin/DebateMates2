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

    boolean joinChatRoom() {
        return serverConnection.joinChatRoom();
    }

    public void setMessageListener(PrototypeChatRoomActivity.DisplayAreaListener listener) {
        displayListener = listener;
    }

    public void sendChatMessage(String message) {
        String sendString = "chat " + token.getUserId() + " " + message;
        serverConnection.sendString(sendString);
    }

    void userInput(String str) {
        String[] tokens = str.split(" ");
        if (tokens.length > 3 && tokens[0].equals("chat")) {
            String sendername = tokens[2].equals(token.getUserId()) ? "You" : tokens[2];

            String outputString = sendername + ":";
            for (int i = 3; i < tokens.length; ++i) {
                outputString += " " + tokens[i];
            }

            if (null != displayListener) displayListener.displayMessage(outputString);
        }
    }

    public boolean establishServerConnection() {
        return serverConnection.connect();
    }

    public void setAccessToken(AccessToken token) {
        this.token = token;
    }

    public boolean login() {
        Log.d("USER_ID", token.getUserId());
        return serverConnection.login(token.getUserId());
    }

    public static ClientApp getClientApp() {
        if (null == currentApp) currentApp = new ClientApp();
        return currentApp;
    }
}
