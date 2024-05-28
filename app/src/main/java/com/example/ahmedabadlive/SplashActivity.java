package com.example.ahmedabadlive;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class SplashActivity extends AppCompatActivity {

    ImageView imageView;

    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sp = getSharedPreferences(contentsp.PREF,MODE_PRIVATE);
        imageView = findViewById(R.id.splash_image);

        Glide
                .with(SplashActivity.this)
                .load("https://cdn.dribbble.com/users/1018473/screenshots/3963419/loader.gif")
                .into(imageView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sp.getString(contentsp.USERID,"").equalsIgnoreCase("")){
                    new CommonMethod(SplashActivity.this,MainActivity.class);
                    finish();
                }
                else {
                    new CommonMethod(SplashActivity.this,DeshboardActivity.class);
                    finish();
                }
            }
        },3000);
    }
}