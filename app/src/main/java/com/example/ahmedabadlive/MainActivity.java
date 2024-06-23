package com.example.ahmedabadlive;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {


    FirebaseAuth auth;
    EditText username,password;
    TextView forgotpassword;
    Button login,signup;
    ImageView visible,notvisible;

    SQLiteDatabase db;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        sp = getSharedPreferences(contentsp.PREF,MODE_PRIVATE);

        db = openOrCreateDatabase("ahmedabadlive_user.db",MODE_PRIVATE,null);
        String tablequery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY,USERNAME VARCHAR(100),NAME VARCHAR(100),PHONE BIGINT(10),EMAIL VARCHAR(100),PASSWORD VARCHAR(20),GENDER VARCHAR(6),CITY VARCHAR(20))";
        db.execSQL(tablequery);

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

//                    String selectQuery = "SELECT * FROM USERS WHERE(USERNAME = '"+username.getText().toString().trim()+"' OR EMAIL = '"+username.getText().toString().trim()+"' OR PHONE = '"+username.getText().toString().trim()+"') AND PASSWORD = '"+password.getText().toString().trim()+"'";
//                    Cursor cursor = db.rawQuery(selectQuery,null);
//
//
//                    if (cursor.getCount()>0){
//                        while (cursor.moveToNext()){
//
//                            String suserid = cursor.getString(0);
//                            String susername = cursor.getString(1);
//                            String sname = cursor.getString(2);
//                            String sphone = cursor.getString(3);
//                            String semail = cursor.getString(4);
//                            String spassword = cursor.getString(5);
//                            String sgender = cursor.getString(6);
//                            String scity = cursor.getString(7);
//
//                            sp.edit().putString(contentsp.USERID,suserid).commit();
//                            sp.edit().putString(contentsp.USERNAME,susername).commit();
//                            sp.edit().putString(contentsp.NAME,sname).commit();
//                            sp.edit().putString(contentsp.PHONE,sphone).commit();
//                            sp.edit().putString(contentsp.EMAIL,semail).commit();
//                            sp.edit().putString(contentsp.PASSWORD,spassword).commit();
//                            sp.edit().putString(contentsp.GENDER,sgender).commit();
//                            sp.edit().putString(contentsp.CITY,scity).commit();
//
//                        }
//
//                        System.out.println("Login successfully");
//                        new CommonMethod(MainActivity.this, "Login successfully");
//                        new CommonMethod(v, "Login successfully");
//
//
//                        //Intent intent = new Intent(MainActivity.this, DeshboardActivity.class);
//                        //MainActivity.this.startActivity(intent);
//                        new CommonMethod(MainActivity.this,DeshboardActivity.class);
//                        finish();
//                    }
//
//                    else {
//                        //Toast.makeText(MainActivity.this, "Username/password INCORRECT", Toast.LENGTH_SHORT).show();
//                        new CommonMethod(MainActivity.this,  "Username/password INCORRECT");
//                    }

                    auth.signInWithEmailAndPassword(username.getText().toString().trim(),password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                new CommonMethod(MainActivity.this, "Login successfully");
                                sp.edit().putString(contentsp.EMAIL,username.getText().toString().trim()).commit();
                                new CommonMethod(MainActivity.this,DeshboardActivity.class);
                                finish();
                            }
                            else {
                                new CommonMethod(MainActivity.this,  "Email/password INCORRECT");
                            }
                        }
                    });
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