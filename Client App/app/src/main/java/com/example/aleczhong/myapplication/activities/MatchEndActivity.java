package com.example.aleczhong.myapplication.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.aleczhong.myapplication.R;

public class MatchEndActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_end);
        String winLose = getIntent().getExtras().getString("winlose");
        if ("win".equals(winLose)) showWin();
        else if ("lose".equals(winLose)) showLose();
    }

    private void showWin() {

    }

    private void showLose() {

    }

    public void continuePressed(View view) {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }
}
