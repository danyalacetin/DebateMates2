package com.example.aleczhong.myapplication.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aleczhong.myapplication.R;
import com.example.aleczhong.myapplication.applogic.ClientApp;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


public class LoginActivity extends AppCompatActivity {
    private ClientApp app;

    private CallbackManager callbackManager;
    private TextView txtStatus;
    private LoginButton loginButton;

    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        app = ClientApp.getClientApp();

        callbackManager = CallbackManager.Factory.create();
        txtStatus = (TextView) findViewById(R.id.txt_status);
        loginButton =(LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile");

        initialiseFacebook();
    }

    private void initialiseFacebook() {
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                app.setAccessToken(loginResult.getAccessToken());

                Thread loginToServer = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ClientApp.getClientApp().login();
                    }
                });

                loginToServer.start();

                Toast.makeText(getApplicationContext(), "Login Success with facebook", Toast.LENGTH_SHORT).show();
                txtStatus.setText("Login Sucesss: " + loginResult.getAccessToken());
//                nextActivity();
            }

            @Override
            public void onCancel() {
                txtStatus.setText("Login Canceled");
            }

            @Override
            public void onError(FacebookException exception) {
                txtStatus.setText("Login Error: " + exception);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    private void nextActivity(){
            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
    }
}