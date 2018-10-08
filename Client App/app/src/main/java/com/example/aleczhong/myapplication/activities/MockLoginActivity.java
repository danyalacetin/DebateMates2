package com.example.aleczhong.myapplication.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.aleczhong.myapplication.R;
import com.example.aleczhong.myapplication.applogic.ClientApp;
import com.example.aleczhong.myapplication.applogic.DelayedReturn;

public class MockLoginActivity extends AppCompatActivity {

    private EditText idInput;
    private TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock_login);
        idInput = findViewById(R.id.mockUserID);
        error = findViewById(R.id.loginFailMessage);
    }

    public void loginPressed(View view) {
        String id = idInput.getText().toString();
        idInput.getText().clear();
        if (!id.equalsIgnoreCase("User ID")) {
            ClientApp.getClientApp().login(id, new DelayedReturn() {
                @Override
                public void onSuccess() {
                    goToGame();
                }

                @Override
                public void onFailure() {
                    displayFailMessage();
                }

                @Override
                public int testString(String toTest) {
                    int compare;
                    if ("login success".equalsIgnoreCase(toTest)) compare = 1;
                    else if ("login failed".equalsIgnoreCase(toTest)) compare = -1;
                    else compare = 0;
                    return compare;
                }
            });
        }
    }

    private void goToGame() {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }

    private void displayFailMessage() {
        error.setText("Login Failed");
    }
}
