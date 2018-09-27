package com.example.aleczhong.myapplication.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aleczhong.myapplication.R;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.share.widget.ShareDialog;

import java.io.IOException;
import java.io.InputStream;

public class Activity2 extends AppCompatActivity {
    private ShareDialog shareDialog;
    private Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_2);
        shareDialog=new ShareDialog(this);

        Bundle inBundle =getIntent().getExtras();
        String name=inBundle.get("name").toString();
        String Surname=inBundle.get("Surname").toString();
        String imageUrl=inBundle.get("imageURL").toString();

        TextView username=(TextView)findViewById(R.id.Textview);
        username.setText(""+name+" "+Surname);
        Button logout=(Button) findViewById(R.id.button2);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                Intent login=new Intent (Activity2.this,LoginActivity.class);
                startActivity(login);
                finish();

            }
        });
        new DownloadImage((ImageView)findViewById(R.id.imageView)).execute(imageUrl);
    }
    public class DownloadImage extends AsyncTask<String,Void,Bitmap>{
        ImageView bmimage;

        public DownloadImage(ImageView bmimage){
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
}
