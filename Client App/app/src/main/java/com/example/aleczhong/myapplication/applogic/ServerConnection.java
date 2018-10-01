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

class ServerConnection {
    private static final String SERVER_ADDRESS = "172.28.60.240";
    private static final int SERVER_PORT = 8818;

    private Socket connection;
    private BufferedReader inStream;
    private PrintWriter outStream;

    ServerConnection() {
        connection = null;
        inStream = null;
        outStream = null;
    }

    boolean login(String userID) {
        boolean isSuccessful = false;
        Log.d("TAG_LOGIN_ATTEMPT", "logging in with: " + userID);
        try {
            outStream.println("login " + userID);
            outStream.flush();
            isSuccessful = inStream.readLine().equals("Logged In Successfully");

        } catch (IOException ex) {

        }

        Log.d("TAG_LOGIN_SUCCESS_T_F", String.valueOf(isSuccessful));
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
