package com.example.aleczhong.myapplication.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.aleczhong.myapplication.R;

public class PlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == android.R.id.home)
        {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void judge(View view) {
//        Intent intent = new Intent(this, JudgeActivity.class);
//        startActivity(intent);
    }

    public void watch(View view) {
//        Intent intent = new Intent(this, SpectateActivity.class);
//        startActivity(intent);
    }

    public void find(View view) {
        Intent intent = new Intent(this, JoiningMatchActivity.class);
        Bundle bun = new Bundle();
        intent.putExtra("type", PlayerViewActivity.class);
        startActivity(intent);
    }
}
