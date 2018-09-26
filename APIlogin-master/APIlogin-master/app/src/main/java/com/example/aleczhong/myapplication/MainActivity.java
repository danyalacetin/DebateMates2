package com.example.aleczhong.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

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
import com.facebook.login.Login;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    LoginButton login_button;
    CallbackManager callbackManager;
    TextView txtStatus;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.getApplicationContext();
        initializeControls();
        loginWithfb();
        accessTokenTracker =new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };
        profileTracker= new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                nextActivity(currentProfile);
            }
        };
        accessTokenTracker.startTracking();
    }
    private void initializeControls(){
        callbackManager = CallbackManager.Factory.create();
        login_button =(LoginButton)findViewById(R.id.login_button);
    }
    private void loginWithfb(){
        // Callback registration
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Profile profile=Profile.getCurrentProfile();
                nextActivity(profile);
                final AccessToken accessToken = loginResult.getAccessToken();
                GraphRequestAsyncTask request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject user, GraphResponse graphResponse) {
                        LoginManager.getInstance().logOut();
                       String username = (user.optString("name"));
                    }
                }).executeAsync();


                Toast.makeText(getApplicationContext(), "Login Success with facebook", Toast.LENGTH_SHORT).show();

                txtStatus.setText("Login Sucesss"+loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                txtStatus.setText("Login canceled");
            }

            @Override
            public void onError(FacebookException exception) {
                txtStatus.setText("Login Error:"+exception);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onResume(){
        super.onResume();
        Profile profile=Profile.getCurrentProfile();
        nextActivity(profile);
    }
    @Override
    protected void onPause(){
        super.onPause();
    }
    @Override
    protected void onStop(){
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    private void nextActivity(Profile profile){
        if(profile !=null)
        {
            Intent intent=new Intent (MainActivity.this, Activity2.class);
            intent.putExtra("name",profile.getFirstName());
            intent.putExtra("Surname",profile.getLastName());
            final Intent imageURL = intent.putExtra("imageURL", profile.getProfilePictureUri(200, 200).toString());
            startActivity(intent);


        }
    }
}