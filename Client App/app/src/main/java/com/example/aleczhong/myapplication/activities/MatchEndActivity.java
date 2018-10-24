package com.example.aleczhong.myapplication.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.aleczhong.myapplication.R;

public class MatchEndActivity extends AppCompatActivity {

    private TextView display;
    private TextView rankDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_end);
        display = findViewById(R.id.endGameMessage);
        rankDisplay = findViewById(R.id.rankDisplay);
        String winLose = getIntent().getExtras().getString("winlose");
        if ("win".equals(winLose)) showWin();
        else if ("lose".equals(winLose)) showLose();
        else display.setText("Error: " + winLose);
    }

    private void showWin() {
        display.setText(R.string.winLabel);
        rankDisplay.setText("Rank +30");
    }

    private void showLose() {
        display.setText(R.string.loseLabel);
        rankDisplay.setText("Rank -30");
    }

    public void continuePressed(View view) {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }
}
