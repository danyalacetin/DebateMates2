package com.example.aleczhong.myapplication.applogic;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.Consumer;

class ServerConnection {
    private static final String SERVER_ADDRESS = "172.28.60.240";
    private static final int SERVER_PORT = 8818;

    private Socket connection;
    private BufferedReader inStream;
    private PrintWriter outStream;

    private ClientApp app;

    ServerConnection(ClientApp app) {
        this.app = app;
        connection = null;
        inStream = null;
        outStream = null;
    }

    boolean joinChatRoom() {
        try {
            String line;
            while (null != (line = inStream.readLine())) {
                app.userInput(line);
            }
        } catch (IOException ex) {

        }

        return true;
    }

//    void handleServer() {
//
//    }

    void sendString(String str) {
        Log.d("SEND_STRING_TAG", str);
        outStream.println(str);
        outStream.flush();
    }

    boolean login(String userID) {
        boolean isSuccessful = false;
        try {
            sendString("login " + userID);
            isSuccessful = inStream.readLine().equals("login success");

        } catch (IOException ex) {

        }

        return isSuccessful;
    }

    boolean connect() {
        boolean isSuccessful = false;

        try {
            connection = new Socket(SERVER_ADDRESS, SERVER_PORT);
            inStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            outStream = new PrintWriter(new BufferedWriter(new OutputStreamWriter(connection.getOutputStream())));

            String confirmationMessage = inStream.readLine();
            isSuccessful = confirmationMessage.equals("Connected");
        } catch (IOException ex) {
            if (null != connection) {
                try {
                    connection.close();
                } catch (IOException ex2) {
                    connection = null;
                } finally {
                    outStream = null;
                    inStream = null;
                }
            }
        }

        return isSuccessful;
    }
}
