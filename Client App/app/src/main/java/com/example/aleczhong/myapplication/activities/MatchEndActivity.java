package com.example.aleczhong.myapplication.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.aleczhong.myapplication.R;
import com.example.aleczhong.myapplication.applogic.ClientApp;

public class MatchEndActivity extends AppCompatActivity {
    private TextView display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_end);
        display = findViewById(R.id.endGameMessage);
        String winLose = getIntent().getExtras().getString("winlose");
        ClientApp.getClientApp().clearMessages();
        if ("win".equals(winLose)) showWin();
        else if ("lose".equals(winLose)) showLose();
        else display.setText("Error: " + winLose);
    }

    private void showWin() {
        display.setText(R.string.winLabel);
    }

    private void showLose() {
        display.setText(R.string.loseLabel);
    }

    public void continuePressed(View view) {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }
}
