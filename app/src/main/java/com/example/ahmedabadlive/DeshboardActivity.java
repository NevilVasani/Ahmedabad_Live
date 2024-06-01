package com.example.ahmedabadlive;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DeshboardActivity extends AppCompatActivity {

    TextView text;
    Button logout,catagory,wishlist;
    ImageView profile;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deshboard);

        sp = getSharedPreferences(contentsp.PREF,MODE_PRIVATE);

        logout = findViewById(R.id.deshboard_Logout);
        catagory = findViewById(R.id.deshboard_catagory);
        profile = findViewById(R.id.deshboard_Profile);
        text = findViewById(R.id.deshboard_text);
        wishlist = findViewById(R.id.deshboard_wishlist);
        text.setText("Welcome "+sp.getString(contentsp.NAME,""));

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DeshboardActivity.this);
                builder.setTitle("Logout!");
                builder.setMessage("Are you sure want to Logout");
                builder.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sp.edit().clear().commit();
                        new CommonMethod(DeshboardActivity.this,MainActivity.class);
                        finish();
                    }
                });

                builder.show();
            }
        });

        catagory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CommonMethod(DeshboardActivity.this, CatagoryActivity.class);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CommonMethod(DeshboardActivity.this,ProfileActivity.class);
            }
        });

        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CommonMethod(DeshboardActivity.this,WishlistActivity.class);
            }
        });
    }
}