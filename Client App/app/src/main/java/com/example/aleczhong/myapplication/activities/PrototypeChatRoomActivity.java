package com.example.aleczhong.myapplication.activities;

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
    private ListView textDisplay;
    private EditText userInput;

    private ArrayAdapter<String> arrayAdapter;
    private List<String> messagses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prototype_chat_room);

        textDisplay = findViewById(R.id.messageArea);
        userInput = findViewById(R.id.inputArea);

        messagses = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, R.layout.activity_prototype_chat_room, messagses);

        textDisplay.setAdapter(arrayAdapter);

        ClientApp.getClientApp().setMessageListener(new DisplayAreaListener());

    }

    void setDisplayText(String txt) {
        messagses.add(txt);
        arrayAdapter.notifyDataSetChanged();
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
