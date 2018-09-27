package com.example.aleczhong.myapplication.applogic;

public class ClientApp {

    private static ClientApp currentApp = null;

    private ClientApp() {

    }

    public boolean establishServerConnection() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return true;
    }





    public static ClientApp getClientApp() {
        if (null == currentApp) currentApp = new ClientApp();
        return currentApp;
    }
}
