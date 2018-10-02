package com.example.aleczhong.myapplication.activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.aleczhong.myapplication.R;
import com.example.aleczhong.myapplication.applogic.ClientApp;

import java.util.ArrayList;
import java.util.List;

public class PrototypeChatRoomActivity extends AppCompatActivity {
    private EditText textDisplay;
    private EditText userInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prototype_chat_room);

        textDisplay = findViewById(R.id.messageArea);
        userInput = findViewById(R.id.inputArea);

        ClientApp.getClientApp().setMessageListener(new DisplayAreaListener());

        joinRoom();

    }

    void joinRoom() {
        ClientApp.getClientApp().joinChatRoom();
    }

    void setDisplayText(String txt) {
        textDisplay.getText().append(txt + "\n");
    }

    public void sendText(View view) {
        String text = userInput.getText().toString();
        userInput.getText().clear();
        ClientApp.getClientApp().sendChatMessage(text);
    }

    public class DisplayAreaListener {
        public void displayMessage(String text) {
            setDisplayText(text);
        }
    }
}
