package com.example.aleczhong.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.aleczhong.myapplication.R;
import com.example.aleczhong.myapplication.applogic.ClientApp;
import com.example.aleczhong.myapplication.applogic.DelayedReturn;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {
    private ClientApp app;

    private CallbackManager callbackManager;
    private LoginButton loginButton;

    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private AccessToken token;
    private Profile profile;
    private boolean isLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        app = ClientApp.getClientApp();
        isLoggedIn = false;

        callbackManager = CallbackManager.Factory.create();
        loginButton =(LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile");
        loginButton.setLoginBehavior(LoginBehavior.WEB_ONLY);

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                token = currentAccessToken;
                loginUser();
            }
        };
        profileTracker= new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                profile = currentProfile;
                loginUser();
            }
        };
        accessTokenTracker.startTracking();
        initialiseFacebook();
    }

    private void initialiseFacebook() {
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //Gives time to retrieve Profile and Token
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                token = loginResult.getAccessToken();
                profile = Profile.getCurrentProfile();
                GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject user, GraphResponse graphResponse) {
                        LoginManager.getInstance().logOut();
                        String name = (user.optString("name"));
                    }
                }).executeAsync();
                Toast.makeText(getApplicationContext(), "Login Success with facebook", Toast.LENGTH_SHORT).show();
                loginUser();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
            }
        });
    }

    private synchronized void loginUser() {
        if (null != profile && null != token && !isLoggedIn) {
            app.setAccessToken(token);
            isLoggedIn = true;
            app.login(new DelayedReturn() {
                @Override
                public void onSuccess() {
                    ClientApp.log("onSuccess called");
                    nextActivity();
                }

                @Override
                public void onFailure() {
                    isLoggedIn = false;
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void displayLoginError() {

    }

    @Override
    protected void onStop(){
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }
    @Override
    protected void onPause(){
        super.onPause();
    }

    private void nextActivity() {
        ClientApp.log("next activity attempted");
        if (profile != null) {
            Intent intent = new Intent(this, MainMenuActivity.class);
            intent.putExtra("name", profile.getFirstName());
            intent.putExtra("Surname", profile.getLastName());
            final Intent imageURL = intent.putExtra("imageURL", profile.getProfilePictureUri(200, 200).toString());
            startActivity(intent);
        }
    }
}