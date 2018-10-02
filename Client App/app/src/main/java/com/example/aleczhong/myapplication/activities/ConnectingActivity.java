package com.example.aleczhong.myapplication.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.aleczhong.myapplication.R;

import static com.example.aleczhong.myapplication.applogic.ClientApp.getClientApp;

public class ConnectingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connecting);connect();
        connect();
//        runLogin();
    }

    private void connect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (getClientApp().establishServerConnection()) {
                    runLogin();
                } else {
                    showLoginError();
                }
            }
        }).start();
    }

    private void runLogin() {
        Intent intent = new Intent(ConnectingActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void showLoginError() {

    }
}
