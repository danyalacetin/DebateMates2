package com.example.aleczhong.myapplication.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aleczhong.myapplication.R;
import com.example.aleczhong.myapplication.applogic.ClientApp;

public class SettingsActivity extends AppCompatActivity {

    private EditText nicknameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        nicknameInput = findViewById(R.id.settingsNickname);
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

    public void nickname(View view)
    {
        String newNickname = nicknameInput.getText().toString();
        nicknameInput.getText().clear();
        if (!(newNickname.isEmpty())){
            ClientApp.getClientApp().sendData("nickname "+newNickname);
            Toast.makeText(getApplicationContext(), "Name Changed!", Toast.LENGTH_LONG).show();
        }
    }

    public void changeQuestions(View view) {
        Intent intent = new Intent(this, SetQuestionsActivity.class);
        startActivity(intent);
    }
}
