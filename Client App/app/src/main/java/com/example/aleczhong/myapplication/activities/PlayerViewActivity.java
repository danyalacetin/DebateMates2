package com.example.aleczhong.myapplication.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.aleczhong.myapplication.R;
import com.example.aleczhong.myapplication.applogic.ChatMessage;
import com.example.aleczhong.myapplication.applogic.ClientApp;
import com.example.aleczhong.myapplication.applogic.MatchDisplayInterface;
import com.example.aleczhong.myapplication.applogic.MessageAdapter;

import java.util.List;

public class PlayerViewActivity extends AppCompatActivity implements MatchDisplayInterface {
    private EditText userInput;
    private TextView matchMessages;
    private MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_view);
        matchMessages = findViewById(R.id.matchInstructions);
        userInput = findViewById(R.id.inputArea);
        ListView listView = findViewById(R.id.messageArea);
        userInput.setEnabled(false);
        ClientApp.getClientApp().addMatchDisplayInterface(this);

        final List<ChatMessage> messages = ClientApp.getClientApp().getMessages();
        adapter = new MessageAdapter(this, messages);
        listView.setAdapter(adapter);

        ClientApp.getClientApp().updateAnnouncements();
    }

    @Override
    public void matchAnnouncement(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                matchMessages.setText(msg);
            }
        });
    }

    @Override
    public void messageUpdate() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (null != adapter) adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void enableInput(boolean value) {
        userInput.setEnabled(value);
    }

    public void sendText(View view) {
        String text = userInput.getText().toString();
        userInput.getText().clear();
        if (!text.equals("")) ClientApp.getClientApp().sendChatMessage(text);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ClientApp.getClientApp().sendData("leave");
    }

    @Override
    protected void onStop() {
        super.onStop();
        ClientApp.getClientApp().sendData("leave");
    }

    @Override
    protected void onPause() {
        super.onPause();
        ClientApp.getClientApp().sendData("leave");
    }
}
