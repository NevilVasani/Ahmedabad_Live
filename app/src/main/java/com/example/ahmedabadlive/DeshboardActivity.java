package com.example.ahmedabadlive;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DeshboardActivity extends AppCompatActivity {

    TextView text;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deshboard);


        text = findViewById(R.id.deshboard_text);


        sp = getSharedPreferences(contentsp.PREF,MODE_PRIVATE);


        text.setText("Welcome "+sp.getString(contentsp.NAME,""));
    }
}