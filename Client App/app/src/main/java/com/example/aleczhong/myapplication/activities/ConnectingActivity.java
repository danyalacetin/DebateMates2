package com.example.aleczhong.myapplication.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.aleczhong.myapplication.R;

import static com.example.aleczhong.myapplication.applogic.ClientApp.getClientApp;

public class ConnectingActivity extends AppCompatActivity {

    private TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connecting);
        error = findViewById(R.id.connectionError);
        connect();
    }

    private void connect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (getClientApp().establishServerConnection()) {
                    goToLogin();
                } else {
                    showConnectionError();
                }
            }
        }).start();
    }

    private void goToLogin() {
        Intent intent = new Intent(ConnectingActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void showConnectionError() {
        error.setText("Could not connect to server");
    }
}
