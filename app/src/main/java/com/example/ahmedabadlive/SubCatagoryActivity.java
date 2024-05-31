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
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

public class SubCatagoryActivity extends AppCompatActivity {

    String[] categoryIDArray = {"1","1","1","1","2","2","2","3","3"};
    String[] nameArray = {"U.S.Polo","H&M","Louis Pholippe","Allen Solly","U.S.Polo","Louis Pholippe","Allen Solly","Levis","Mufti"};
    Integer[] imageArray = {R.drawable.uspolo,R.drawable.hm_logo,R.drawable.louis_logo,R.drawable.allen,R.drawable.uspolo,R.drawable.louis_logo,R.drawable.allen,R.drawable.levis_logo,R.drawable.mufti};
    RecyclerView recyclerView;
    SQLiteDatabase db;
    ArrayList<SubCatagoryList> subcatagoryList;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_catagory);

        sp = getSharedPreferences(contentsp.PREF,MODE_PRIVATE);

        recyclerView = findViewById(R.id.sub_category_recyclerview);

        db = openOrCreateDatabase("ahmedabadlive_user.db",MODE_PRIVATE,null);
        String tablequery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,USERNAME VARCHAR(100),NAME VARCHAR(100),PHONE BIGINT(10),EMAIL VARCHAR(100),PASSWORD VARCHAR(20),GENDER VARCHAR(6),CITY VARCHAR(20))";
        db.execSQL(tablequery);

        String categorytablequery = "CREATE TABLE IF NOT EXISTS CATEGORY(CATEGORYID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),IMAGE VARCHAR(100))";
        db.execSQL(categorytablequery);

        String subcategorytablequery = "CREATE TABLE IF NOT EXISTS SUBCATEGORY(SUBCATEGORYID INTEGER PRIMARY KEY AUTOINCREMENT,CATEGORYID VARCHAR(10),NAME VARCHAR(100),IMAGE VARCHAR(100))";
        db.execSQL(subcategorytablequery);


        for(int i=0;i<nameArray.length;i++){
            String selectQuery = "SELECT * FROM SUBCATEGORY WHERE NAME='"+nameArray[i]+"' AND CATEGORYID = '"+categoryIDArray[i]+"'";
            Cursor cursor = db.rawQuery(selectQuery,null);

            if(cursor.getCount()>0){

            }
            else {
                String insertQuery = "INSERT INTO SUBCATEGORY VALUES(NULL,'" + categoryIDArray[i] + "','" + nameArray[i] + "','" + imageArray[i] + "')";
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


        String selectQuery = "SELECT * FROM SUBCATEGORY WHERE CATEGORYID = '"+sp.getString(contentsp.CATEGORY_ID,"")+"'";
        Cursor cursor = db.rawQuery(selectQuery,null);

        if (cursor.getCount()>0) {
            ArrayList arrayList = new ArrayList();
            while (cursor.moveToNext()){
                SubCatagoryList list = new SubCatagoryList();
                list.setId(cursor.getString(0));
                list.setCategoryID(cursor.getString(1));
                list.setName(cursor.getString(2));
                list.setImage(cursor.getString(3));
                arrayList.add(list);
            }

            SubCatagoryAdaptor adaptor = new SubCatagoryAdaptor(SubCatagoryActivity.this,arrayList);
            recyclerView.setAdapter(adaptor);
        }
        else {
            new CommonMethod(SubCatagoryActivity.this,"Subcatagory not Found!");
        }
    }
}