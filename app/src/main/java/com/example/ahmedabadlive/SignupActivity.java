package com.example.ahmedabadlive;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseFirestore firestore;

    EditText username,name,phone,email,password,confirmpassword;
    ImageView notvisible,visible,confirmvisible,confirmnotvisible;
    Button signup;
    RadioGroup gender;
    Spinner city;
    String scity = "", sgender = "";
    CheckBox terms;

    String email_syntax = "[A-Z0-9a-z._-]+@[a-z]+\\.[a-z]+";

    String[] cityname = {"Select the city","Rajkot","Ahmedabad","Surat"};

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        username = findViewById(R.id.signup_username);
        name = findViewById(R.id.signup_name);
        phone = findViewById(R.id.signup_phone);
        email = findViewById(R.id.signup_email);
        password = findViewById(R.id.signup_password);
        confirmpassword = findViewById(R.id.signup_confirmpassword);
        notvisible = findViewById(R.id.signup_not_visible);
        visible = findViewById(R.id.signup_visible);
        confirmvisible = findViewById(R.id.signup_confirm_visible);
        confirmnotvisible = findViewById(R.id.signup_confirm_not_visible);
        gender = findViewById(R.id.signup_gender);
        city = findViewById(R.id.signup_city);
        terms = findViewById(R.id.signup_terms);
        signup = findViewById(R.id.signup_signup);

        confirmnotvisible.setVisibility(View.GONE);
        notvisible.setVisibility(View.GONE);


        db = openOrCreateDatabase("ahmedabadlive_user.db",MODE_PRIVATE,null);
        String tablequery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,USERNAME VARCHAR(100),NAME VARCHAR(100),PHONE BIGINT(10),EMAIL VARCHAR(100),PASSWORD VARCHAR(20),GENDER VARCHAR(6),CITY VARCHAR(20))";
        db.execSQL(tablequery);
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                sgender = radioButton.getText().toString();
                new CommonMethod(SignupActivity.this,radioButton.getText().toString());
            }
        });


        ArrayAdapter adapter = new ArrayAdapter(SignupActivity.this,android.R.layout.simple_list_item_checked,cityname);
        city.setAdapter(adapter);

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0){
                    scity = "";
                }
                else {

                    scity = cityname[position];
                    new CommonMethod(SignupActivity.this,cityname[position]);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

        confirmvisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmvisible.setVisibility(View.GONE);
                confirmnotvisible.setVisibility(View.VISIBLE);
                confirmpassword.setTransformationMethod(null);
            }
        });

        confirmnotvisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmvisible.setVisibility(View.VISIBLE);
                confirmnotvisible.setVisibility(View.GONE);
                confirmpassword.setTransformationMethod(new PasswordTransformationMethod());
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().trim().equals("")){
                    username.setError("Enter UseName");
                } else if (name.getText().toString().trim().equals("")) {
                    name.setError("Enter Name");
                }else if (phone.getText().toString().trim().equals("")) {
                    phone.setError("Enter Phone No");
                }else if (email.getText().toString().trim().equals("")) {
                    email.setError("Enter Email");
                }
                else if (!email.getText().toString().trim().matches(email_syntax)) {
                    email.setError("Enter Email in proper way");
                }
                else if (password.getText().toString().trim().equals("")) {
                    password.setError("Enter Password");
                }
                else if (password.getText().toString().trim().length()<6) {
                    password.setError("Password have minmun 6 charactars");
                }
                else if (confirmpassword.getText().toString().trim().equals("")) {
                    confirmpassword.setError("Enter ConfirmPassword");
                }
                else if (!password.getText().toString().trim().equals(confirmpassword.getText().toString().trim())) {
                    confirmpassword.setError("Password dose not match");
                }
                else if (gender.getCheckedRadioButtonId() == -1) {
                    new CommonMethod(v,"Select gender");
                }
                else if (scity == "") {
                    new CommonMethod(v,"Select the city");
                }
                else if (!terms.isChecked()) {
                    new CommonMethod(v,"Accept the terms and conditions");
                }
                else {


//                    String selectQuery = "SELECT * FROM USERS WHERE USERNAME = '"+username.getText().toString()+"' OR EMAIL = '"+email.getText().toString()+"' OR PHONE = '"+phone.getText().toString()+"'";
//                    Cursor cursor = db.rawQuery(selectQuery,null);
//                    if (cursor.getCount()>0){
//                        new CommonMethod(SignupActivity.this,"Username/email/phoneno already registerd");
//                        new CommonMethod(v,"Username/email/phoneno already registerd");
//                    }
//                    else {
//                        String insertQuery = "INSERT INTO USERS VALUES(NULL,'"+username.getText().toString()+"','"+name.getText().toString()+"','"+phone.getText().toString()+"','"+email.getText().toString()+"','"+password.getText().toString()+"','"+sgender+"','"+scity+"')";
//                        db.execSQL(insertQuery);
//                        System.out.println("Sign successfully");
//                        new CommonMethod(SignupActivity.this, "Signin successfully");
//                        new CommonMethod(v, "Sign successfully");
//                        onBackPressed();
//                    }



                        auth.createUserWithEmailAndPassword(email.getText().toString().trim(),password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    new CommonMethod(SignupActivity.this, "Signin successfully in auth");
                                    String userID = auth.getCurrentUser().getUid();
                                    DocumentReference  documentReference = firestore.collection("Users").document(userID);
                                    Map<String , Object> user = new HashMap<>();
                                    user.put("username", username.getText().toString().trim());
                                    user.put("name", name.getText().toString().trim());
                                    user.put("phone", phone.getText().toString().trim());
                                    user.put("email", email.getText().toString().trim());
                                    user.put("gender", sgender);
                                    user.put("city", scity);
                                    documentReference.set(user);
                                    onBackPressed();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                new CommonMethod(v, "Email address already exist!");
                            }
                        });

                }
            }
        });

    }
}