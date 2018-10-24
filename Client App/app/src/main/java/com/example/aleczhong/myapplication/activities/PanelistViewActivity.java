package com.example.aleczhong.myapplication.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.example.aleczhong.myapplication.R;
import com.example.aleczhong.myapplication.applogic.ChatMessage;
import com.example.aleczhong.myapplication.applogic.ClientApp;
import com.example.aleczhong.myapplication.applogic.MatchDisplayInterface;
import com.example.aleczhong.myapplication.applogic.MessageAdapter;

import java.util.List;

public class PanelistViewActivity extends AppCompatActivity implements MatchDisplayInterface {

    private TextView matchAnnouncements;
    private TextView matchQuestion;
    private MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panelist_view);
        matchAnnouncements = findViewById(R.id.matchAnnouncements);
        matchQuestion = findViewById(R.id.matchQuestions);
        ListView listView = findViewById(R.id.messageArea);

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
                matchAnnouncements.setText(msg);
            }
        });
    }

    @Override
    public void displayQuestion(final String question) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                matchQuestion.setText(question);
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

    }
}
