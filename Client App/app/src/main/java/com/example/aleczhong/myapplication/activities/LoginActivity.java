package com.example.aleczhong.myapplication.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aleczhong.myapplication.R;
import com.example.aleczhong.myapplication.applogic.ClientApp;
import com.example.aleczhong.myapplication.applogic.DelayedReturn;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


public class LoginActivity extends AppCompatActivity {
    private ClientApp app;

    private CallbackManager callbackManager;
    private LoginButton loginButton;

    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        app = ClientApp.getClientApp();

        callbackManager = CallbackManager.Factory.create();
        loginButton =(LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile");
        loginButton.setLoginBehavior(LoginBehavior.WEB_ONLY);
        initialiseFacebook();
    }

    private void initialiseFacebook() {
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                app.setAccessToken(loginResult.getAccessToken());
                app.login(new DelayedReturn() {
                    @Override
                    public void onSuccess() {
                        nextActivity();
                    }

                    @Override
                    public void onFailure() {
                        displayLoginError();
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

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void displayLoginError() {

    }

    private void nextActivity() {
            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
    }
}