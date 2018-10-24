package com.example.aleczhong.myapplication.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.aleczhong.myapplication.R;
import com.example.aleczhong.myapplication.applogic.ChatMessage;
import com.example.aleczhong.myapplication.applogic.ClientApp;
import com.example.aleczhong.myapplication.applogic.DelayedReturn;
import com.example.aleczhong.myapplication.applogic.MatchDisplayInterface;
import com.example.aleczhong.myapplication.applogic.MessageAdapter;

import java.util.List;

public class PlayerViewActivity extends AppCompatActivity implements MatchDisplayInterface {
    private EditText userInput;
    private TextView matchAnnouncements;
    private TextView matchQuestions;
    private MessageAdapter adapter;
    private String winner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_view);
        matchAnnouncements = findViewById(R.id.matchAnnouncements);
        matchQuestions = findViewById(R.id.matchQuestions);
        userInput = findViewById(R.id.inputArea);
        ListView listView = findViewById(R.id.messageArea);
        userInput.setEnabled(false);
        ClientApp.getClientApp().addMatchDisplayInterface(this);

        final List<ChatMessage> messages = ClientApp.getClientApp().getMessages();
        adapter = new MessageAdapter(this, messages);
        listView.setAdapter(adapter);

        ClientApp.getClientApp().updateAnnouncements();
        ClientApp.getClientApp().addWaitFunc(new DelayedReturn() {
            @Override
            public void onSuccess() {
                showEnd();
            }

            @Override
            public void onFailure() {

            }

            @Override
            public int testString(String toTest) {
                String[] tokens = toTest.split(" ");
                if (tokens[0].equalsIgnoreCase("end")) {
                    winner = ClientApp.removeTailToString(tokens, 1);
                    return 1;
                }
                return 0;
            }
        });
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
                matchQuestions.setText(question);
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
    public void enableInput(final boolean value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                userInput.setEnabled(value);
            }
        });
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

    private void showEnd() {
        String winlose;
        if (winner.equals(ClientApp.getClientApp().getLogin())) winlose = "win";
        else winlose = "lose";
        Intent intent = new Intent(this, MatchEndActivity.class);
        intent.putExtra("winlose", winlose);
        startActivity(intent);
    }
}
