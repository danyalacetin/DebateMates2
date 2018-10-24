package com.example.aleczhong.myapplication.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aleczhong.myapplication.R;
import com.example.aleczhong.myapplication.applogic.ClientApp;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.share.widget.ShareDialog;

import java.io.IOException;
import java.io.InputStream;

public class MainMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Bundle inBundle = getIntent().getExtras();
        if (inBundle!=null ) {
            String imageUrl = ClientApp.getClientApp().getImageUrl();
            String nameString = ClientApp.getClientApp().getDetails();

            ((TextView) findViewById(R.id.textView4)).setText(nameString);

            new DownloadImage((ImageView)findViewById(R.id.imageView)).execute(imageUrl);
            DownloadImage view = new DownloadImage((ImageView)findViewById(R.id.imageView));
            view.bmimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent1 = new Intent(MainMenuActivity.this,ProfileActivity.class);
//                    startActivity(intent1);
                }
            });
        }

    }

    public void onImageClick(View view) {
        Intent intent1 = new Intent(MainMenuActivity.this,ProfileActivity.class);
        startActivity(intent1);
    }

    public void logoutPressed(View view) {
        LoginManager.getInstance().logOut();
        ClientApp.getClientApp().logout();
        back();
    }

    @Override
    public void onBackPressed() {

    }

    private class DownloadImage extends AsyncTask<String,Void,Bitmap> {
        private ImageView bmimage;

        DownloadImage(ImageView bmimage){
            this.bmimage=bmimage;
        }
        protected Bitmap doInBackground(String...Url){
            String urldisplay=Url[0];
            Bitmap Miconll=null;
            try {
                InputStream in = null;
                in = new java.net.URL(urldisplay).openStream();
                Miconll = BitmapFactory.decodeStream(in);
            } catch (IOException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return Miconll;
        }
        protected void onpostExecute(Bitmap result){
            bmimage.setImageBitmap(result);
        }
    }
    public void play(View view)
    {
        Intent intent = new Intent(this, PlayActivity.class);
        startActivity(intent);
    }

    public void back() {
        Intent login=new Intent (MainMenuActivity.this,LoginActivity.class);
        startActivity(login);
        finish();
    }

    public void settings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
