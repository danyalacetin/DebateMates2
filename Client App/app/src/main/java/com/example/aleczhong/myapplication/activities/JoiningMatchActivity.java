package com.example.aleczhong.myapplication.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
        final String type = (String) getIntent().getExtras().get("type");

        ClientApp.getClientApp().joinMatch(type, new DelayedReturn() {
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
                if (toTest.equalsIgnoreCase("join success")) compare = 1;
                else if (toTest.equalsIgnoreCase("join failed")) compare = -1;
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
