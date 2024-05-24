package com.example.ahmedabadlive;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {


    EditText username,password;
    TextView forgotpassword;
    Button login,signup;
    ImageView visible,notvisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.main_username);
        password = findViewById(R.id.main_password);

        login = findViewById(R.id.main_login);
        signup = findViewById(R.id.main_signup);

        visible = findViewById(R.id.main_visible);
        notvisible = findViewById(R.id.main_not_visible);
        forgotpassword = findViewById(R.id.main_forgotpassword);

        forgotpassword.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visible.setVisibility(View.GONE);
                notvisible.setVisibility(View.VISIBLE);
                password.setTransformationMethod(null);
            }
        });

        notvisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visible.setVisibility(View.VISIBLE);
                notvisible.setVisibility(View.GONE);
                password.setTransformationMethod(new PasswordTransformationMethod());
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().trim().equals("")){
                    username.setError("Enter UseName");
                } else if (password.getText().toString().trim().equals("")) {
                    password.setError("Enter Password");
                }else {
                    System.out.println("Login successfully");
                    new CommonMethod(MainActivity.this, "Login successfully");
                    new CommonMethod(v, "Login successfully");
                    new CommonMethod(MainActivity.this,DeshboardActivity.class);
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new CommonMethod(MainActivity.this,SignupActivity.class);
                Intent intent =new Intent(MainActivity.this,SignupActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CommonMethod(MainActivity.this, ForgotpasswordActivity.class);
            }
        });

        notvisible.setVisibility(View.GONE);
    }
}