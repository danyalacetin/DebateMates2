package com.example.aleczhong.myapplication.applogic;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

class ServerConnection {
    private static final String SERVER_ADDRESS = "192.168.0.3";
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



    void joinChatRoom() {
        send("join player");
    }

    private void handleServer(){
        String input;
        try {
            while (null != (input = inStream.readLine())) {
                app.handleInput(input);
            }
        } catch (IOException error) {
            ClientApp.log("error");
        }

        ClientApp.log("finished executing");
    }

    void send(String str) {
        ClientApp.log(str);
        new SendTask(outStream).execute(str);
        ClientApp.log("string sent");
    }

    void login(String userID) {
        send("login " + userID);
        ClientApp.log("login command sent");
    }

    boolean connect() {
        boolean isSuccessful = false;

        try {
            connection = new Socket(SERVER_ADDRESS, SERVER_PORT);
            inStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            outStream = new PrintWriter(new BufferedWriter(new OutputStreamWriter(connection.getOutputStream())));

            new Thread(new Runnable() {
                @Override
                public void run() {
                    handleServer();
                }
            }).start();

            isSuccessful = true;

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

    private class SendTask extends AsyncTask<String, Void, Void> {

        private PrintWriter out;

        private SendTask(PrintWriter stream) {
            out = stream;
        }

        @Override
        protected Void doInBackground(String... strings) {
            for (String s : strings) {
                outStream.println(s);
                outStream.flush();
            }

            return null;
        }
    }
}
