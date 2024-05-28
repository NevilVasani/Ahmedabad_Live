package com.example.ahmedabadlive;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProfileActivity extends AppCompatActivity {

    EditText username,name,phone,email,password,confirmpassword;
    ImageView notvisible,visible,confirmvisible,confirmnotvisible;
    Button editprofile,submit;
    RadioButton male,female;

    RadioGroup gender;
    Spinner city;
    String scity = "", sgender = "";

    FrameLayout passwordlayout, confirmpasswordlayout;
    String email_syntax = "[A-Z0-9a-z._-]+@[a-z]+\\.[a-z]+";

    String[] cityname = {"Select the city","Rajkot","Ahmedabad","Surat"};

    SQLiteDatabase db;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        username = findViewById(R.id.profile_username);
        name = findViewById(R.id.profile_name);
        phone = findViewById(R.id.profile_phone);
        email = findViewById(R.id.profile_email);
        password = findViewById(R.id.profile_password);
        confirmpassword = findViewById(R.id.profile_confirmpassword);
        notvisible = findViewById(R.id.profile_not_visible);
        visible = findViewById(R.id.profile_visible);
        confirmvisible = findViewById(R.id.profile_confirm_visible);
        confirmnotvisible = findViewById(R.id.profile_confirm_not_visible);
        gender = findViewById(R.id.profile_gender);
        city = findViewById(R.id.profile_city);
        editprofile = findViewById(R.id.profile_editprofile);
        submit = findViewById(R.id.profile_submit);
        male = findViewById(R.id.profile_male);
        female = findViewById(R.id.profile_female);
        passwordlayout = findViewById(R.id.profile_passwordlayout);
        confirmpasswordlayout = findViewById(R.id.profile_confirm_passwordlayout);

        confirmnotvisible.setVisibility(View.GONE);
        notvisible.setVisibility(View.GONE);
        sp = getSharedPreferences(contentsp.PREF,MODE_PRIVATE);

        db = openOrCreateDatabase("ahmedabadlive_user.db",MODE_PRIVATE,null);
        String tablequery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY,USERNAME VARCHAR(100),NAME VARCHAR(100),PHONE BIGINT(10),EMAIL VARCHAR(100),PASSWORD VARCHAR(20),GENDER VARCHAR(6),CITY VARCHAR(20))";
        db.execSQL(tablequery);
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                sgender = radioButton.getText().toString();
                new CommonMethod(ProfileActivity.this,radioButton.getText().toString());
            }
        });


        ArrayAdapter adapter = new ArrayAdapter(ProfileActivity.this,android.R.layout.simple_list_item_checked,cityname);
        city.setAdapter(adapter);

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0){
                    scity = "";
                }
                else {

                    scity = cityname[position];
                    new CommonMethod(ProfileActivity.this,cityname[position]);
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


        submit.setOnClickListener(new View.OnClickListener() {
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
                else {

                    String selectQuery = "SELECT * FROM USERS WHERE USERNAME = '"+username.getText().toString()+"' OR NAME = '"+name.getText().toString()+"'";
                    Cursor cursor = db.rawQuery(selectQuery,null);
                    if (cursor.getCount()>0){
                        new CommonMethod(ProfileActivity.this,"Username/email/phoneno already registerd");
                        new CommonMethod(v,"Username/email/phoneno already registerd");
                    }
                    else {
                        String updateQuery = "UPDATE USERS SET USERNAME = '"+username.getText().toString()+"',NAME = '"+name.getText().toString()+"',PHONE = '"+phone.getText().toString()+"',EMAIL = '"+email.getText().toString()+"',PASSWORD = '"+password.getText().toString()+"',GENDER = '"+sgender+"',CITY = '"+scity+"' WHERE USERID = '"+sp.getString(contentsp.USERID,"")+"'";
                        db.execSQL(updateQuery);
                        new CommonMethod(ProfileActivity.this, "Edit profile successfully");
                        new CommonMethod(v, "Edit profile successfully");

                        sp.edit().putString(contentsp.USERNAME,username.getText().toString()).commit();
                        sp.edit().putString(contentsp.NAME,name.getText().toString()).commit();
                        sp.edit().putString(contentsp.EMAIL,email.getText().toString()).commit();
                        sp.edit().putString(contentsp.PHONE,phone.getText().toString()).commit();
                        sp.edit().putString(contentsp.PASSWORD,password.getText().toString()).commit();
                        sp.edit().putString(contentsp.GENDER,sgender).commit();
                        sp.edit().putString(contentsp.CITY,scity).commit();


                        setdata(false);
                    }
                }
            }
        });

        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setdata(true);
            }
        });
        setdata(false);
    }

    private void setdata(Boolean b){
        username.setText(sp.getString(contentsp.USERNAME,""));
        name.setText(sp.getString(contentsp.NAME,""));
        phone.setText(sp.getString(contentsp.PHONE,""));
        email.setText(sp.getString(contentsp.EMAIL,""));
        password.setText(sp.getString(contentsp.PASSWORD,""));
        confirmpassword.setText(sp.getString(contentsp.PASSWORD,""));

        sgender = sp.getString(contentsp.GENDER,"");
        if (sgender.equalsIgnoreCase("Male")){
            male.setChecked(true);
            female.setChecked(false);
        }
        else if (sgender.equalsIgnoreCase("Female")){
            male.setChecked(false);
            female.setChecked(true);
        }
        else {
            male.setChecked(false);
            female.setChecked(false);
        }

        scity = sp.getString(contentsp.CITY,"");
        int city_position = 0;
        for (int i=0; i<cityname.length;i++){
            if (cityname[i].equalsIgnoreCase(scity)){
                city_position = i;
                break;
            }
        }
        city.setSelection(city_position);

        username.setEnabled(b);
        name.setEnabled(b);
        phone.setEnabled(b);
        email.setEnabled(b);
        male.setEnabled(b);
        female.setEnabled(b);
        city.setEnabled(b);
        if(b){
            editprofile.setVisibility(View.GONE);
            submit.setVisibility(View.VISIBLE);
            passwordlayout.setVisibility(View.VISIBLE);
            confirmpasswordlayout.setVisibility(View.VISIBLE);

        }
        else{
            editprofile.setVisibility(View.VISIBLE);
            submit.setVisibility(View.GONE);
            passwordlayout.setVisibility(View.GONE);
            confirmpasswordlayout.setVisibility(View.GONE);
        }
    }

}