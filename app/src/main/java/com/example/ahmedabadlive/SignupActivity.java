package com.example.ahmedabadlive;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class SignupActivity extends AppCompatActivity {

    EditText username,name,phone,eamil,password,confirmpassword;
    ImageView notvisible,visible,confirmvisible,confirmnotvisible;
    Button signup;
    RadioGroup gender;
    Spinner city;
    String scity = "";
    CheckBox terms;

    String[] cityname = {"Select the city","Rajkot","Ahmedabad","Surat"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username = findViewById(R.id.signup_username);
        name = findViewById(R.id.signup_name);
        phone = findViewById(R.id.signup_phone);
        eamil = findViewById(R.id.signup_email);
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

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
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
                password.setTransformationMethod(null);
            }
        });

        confirmnotvisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmvisible.setVisibility(View.VISIBLE);
                confirmnotvisible.setVisibility(View.GONE);
                password.setTransformationMethod(new PasswordTransformationMethod());
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
                }else if (eamil.getText().toString().trim().equals("")) {
                    eamil.setError("Enter Email");
                }else if (password.getText().toString().trim().equals("")) {
                    password.setError("Enter Password");
                }else if (confirmpassword.getText().toString().trim().equals("")) {
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
                    System.out.println("Sign successfully");
                    new CommonMethod(SignupActivity.this, "Login successfully");
                    new CommonMethod(v, "Login successfully");
                    onBackPressed();
                }
            }
        });

    }
}