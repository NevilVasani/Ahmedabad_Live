package com.example.ahmedabadlive;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

public class CatagoryActivity extends AppCompatActivity {

    String[] nameArray = {"Shirts","T-Shirts","Jeans","Shorts & Trousers","Casual Shoes","Infant Essentials"};
    Integer[] imageArray = {R.drawable.shirts,R.drawable.tshirts,R.drawable.jeans,R.drawable.shorts,R.drawable.causual_shoes,R.drawable.infant};
    RecyclerView  recyclerView;
    SQLiteDatabase db;
    ArrayList<CatagoryList> catagoryList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catagory);

        recyclerView = findViewById(R.id.category_recyclerview);

        db = openOrCreateDatabase("ahmedabadlive_user.db",MODE_PRIVATE,null);
        String tablequery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,USERNAME VARCHAR(100),NAME VARCHAR(100),PHONE BIGINT(10),EMAIL VARCHAR(100),PASSWORD VARCHAR(20),GENDER VARCHAR(6),CITY VARCHAR(20))";
        db.execSQL(tablequery);

        String categorytablequery = "CREATE TABLE IF NOT EXISTS CATEGORY(CATEGORYID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),IMAGE VARCHAR(100))";
        db.execSQL(categorytablequery);


        for(int i=0;i<nameArray.length;i++){
            String selectQuery = "SELECT * FROM CATEGORY WHERE NAME='"+nameArray[i]+"'";
            Cursor cursor = db.rawQuery(selectQuery,null);

            if(cursor.getCount()>0){

            }
            else {
                String insertQuery = "INSERT INTO CATEGORY VALUES(NULL,'" + nameArray[i] + "','" + imageArray[i] + "')";
                db.execSQL(insertQuery);
            }
        }

        //for sat as a grid horizontal
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));

        //for set as a gird vertical
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        //for set as a List
        //recyclerView.setLayoutManager(new LinearLayoutManager(CatagoryActivity.this));

        recyclerView.setItemAnimator(new DefaultItemAnimator());


        String selectQuery = "SELECT * FROM CATEGORY";
        Cursor cursor = db.rawQuery(selectQuery,null);

            ArrayList arrayList = new ArrayList();
            while (cursor.moveToNext()) {
                CatagoryList list = new CatagoryList();
                list.setCatagoryID(cursor.getString(0));
                list.setName(cursor.getString(1));
                list.setImage(cursor.getString(2));
                arrayList.add(list);
            }

            CatagoryAdaptor adaptor = new CatagoryAdaptor(CatagoryActivity.this, arrayList);
            recyclerView.setAdapter(adaptor);
    }
}