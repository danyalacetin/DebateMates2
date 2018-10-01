package com.example.aleczhong.myapplication.applogic;

import android.util.Log;

import com.facebook.AccessToken;

public class ClientApp {

    private static ClientApp currentApp = null;
    private ServerConnection serverConnection;
    private AccessToken token;

    private ClientApp() {
        serverConnection = new ServerConnection();
        token = null;
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
