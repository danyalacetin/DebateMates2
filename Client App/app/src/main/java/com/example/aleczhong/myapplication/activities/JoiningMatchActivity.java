package com.example.aleczhong.myapplication.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.aleczhong.myapplication.R;
import com.example.aleczhong.myapplication.applogic.ClientApp;
import com.example.aleczhong.myapplication.applogic.DelayedReturn;

public class JoiningMatchActivity extends AppCompatActivity {

    private Class clazz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joining_match);
        clazz = (Class) getIntent().getExtras().get("type");
        ClientApp.getClientApp().joinMatch(new DelayedReturn() {
            @Override
            public void onSuccess() {
                nextActivity();
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

    private void nextActivity() {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}
