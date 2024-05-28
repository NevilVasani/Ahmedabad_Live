package com.example.ahmedabadlive;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ForgotpasswordActivity extends AppCompatActivity {

    EditText newpassword, confirmnewpassword, username;
    ImageView visible,notvisible,confirmvisible,confirmnotvisible;
    Button confirm_button;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        db = openOrCreateDatabase("ahmedabadlive_user.db",MODE_PRIVATE,null);
        String tablequery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY,USERNAME VARCHAR(100),NAME VARCHAR(100),PHONE BIGINT(10),EMAIL VARCHAR(100),PASSWORD VARCHAR(20),GENDER VARCHAR(6),CITY VARCHAR(20))";
        db.execSQL(tablequery);

        username = findViewById(R.id.forgotpassword_username);
        newpassword = findViewById(R.id.forgotpassword_password);
        confirmnewpassword = findViewById(R.id.forgotpassword_confirmpassword);
        confirm_button = findViewById(R.id.forgotpassword_button);
        visible =findViewById(R.id.forgot_new_visible);
        notvisible = findViewById(R.id.forgot_new_not_visible);
        confirmvisible = findViewById(R.id.forgot_new_confirm_visible);
        confirmnotvisible = findViewById(R.id.forgot_new_confirm_not_visible);

        notvisible.setVisibility(View.GONE);
        confirmnotvisible.setVisibility(View.GONE);


        visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visible.setVisibility(View.GONE);
                notvisible.setVisibility(View.VISIBLE);
                newpassword.setTransformationMethod(null);
            }
        });

        notvisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visible.setVisibility(View.VISIBLE);
                notvisible.setVisibility(View.GONE);
                newpassword.setTransformationMethod(new PasswordTransformationMethod());
            }
        });

        confirmvisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmvisible.setVisibility(View.GONE);
                confirmnotvisible.setVisibility(View.VISIBLE);
                confirmnewpassword.setTransformationMethod(null);
            }
        });

        confirmnotvisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmvisible.setVisibility(View.VISIBLE);
                confirmnotvisible.setVisibility(View.GONE);
                confirmnewpassword.setTransformationMethod(new PasswordTransformationMethod());
            }
        });


        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().trim().equals("")) {
                    username.setError("Enter the username");
                }else if(newpassword.getText().toString().trim().equals("")){
                    newpassword.setError("Enter the new password");
                }
                else if (confirmnewpassword.getText().toString().trim().equals("")) {
                    confirmnewpassword.setError("Enter the confirmpassword");
                } else if (!newpassword.getText().toString().trim().matches(confirmnewpassword.getText().toString().trim())) {
                    new CommonMethod(ForgotpasswordActivity.this, "Password does not match");
                } else if (newpassword.getText().toString().trim().length()<6) {
                    new CommonMethod(ForgotpasswordActivity.this, "Password must have 6 charactar");
                } else{

                    String selectQuery = "SELECT * FROM USERS WHERE USERNAME = '"+username.getText().toString().trim()+"' OR EMAIL = '"+username.getText().toString().trim()+"' OR PHONE = '"+username.getText().toString().trim()+"'";
                    Cursor cursor = db.rawQuery(selectQuery,null);
                    if (cursor.getCount()>0){

                        String updateQuery = "UPDATE USERS SET PASSWORD = '"+confirmnewpassword.getText().toString().trim()+"' WHERE USERNAME = '"+username.getText().toString().trim()+"' OR EMAIL = '"+username.getText().toString().trim()+"' OR PHONE = '"+username.getText().toString().trim()+"'";
                        db.execSQL(updateQuery);
                        new CommonMethod(ForgotpasswordActivity.this, "Password Updated successfuly");
                        onBackPressed();
                    }
                    else {
                        new CommonMethod(ForgotpasswordActivity.this, "Enter the valid username");
                    }

                }

            }
        });

    }
}