package com.example.aleczhong.myapplication.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.aleczhong.myapplication.R;
import com.example.aleczhong.myapplication.applogic.ClientApp;
import com.example.aleczhong.myapplication.applogic.DelayedReturn;

import java.util.HashMap;
import java.util.Map;

public class JoiningMatchActivity extends AppCompatActivity {

    private static final Map<String, Class> classMap = new HashMap<>();
    static {
        classMap.put("player", PlayerViewActivity.class);
        classMap.put("panelist", PanelistViewActivity.class);
        classMap.put("spectator", SpectatorViewActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joining_match);
        String type = getIntent().getExtras().getString("type");

        joinMatch(type);
        enter(type);
    }

    private void joinMatch(final String type) {
        ClientApp.getClientApp().joinMatch(type, new DelayedReturn() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView) findViewById(R.id.finMatchLabel)).setText(R.string.waiting_for_players);
                    }
                });
            }

            @Override
            public void onFailure() {
                finish();
            }

            @Override
            public int testString(String toTest) {
                int compare;
                if (toTest.equalsIgnoreCase("join success")) compare = 1;
                else if (toTest.equalsIgnoreCase("join failed")) compare = -1;
                else compare = 0;
                return compare;
            }
        });
    }

    private void enter(final String type) {
        ClientApp.getClientApp().addWaitFunc(new DelayedReturn() {
            @Override
            public void onSuccess() {
                nextActivity(classMap.get(type));
                finish();
            }

            @Override
            public void onFailure() {
                finish();
            }

            @Override
            public int testString(String toTest) {
                int compare;
                if (toTest.equalsIgnoreCase("start")) compare = 1;
                else if (toTest.equalsIgnoreCase("fail")) compare = -1;
                else compare = 0;
                return compare;
            }
        });
    }

    private void nextActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}
