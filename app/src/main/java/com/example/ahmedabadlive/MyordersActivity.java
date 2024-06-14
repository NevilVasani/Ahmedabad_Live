package com.example.ahmedabadlive;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyordersActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    SQLiteDatabase db;
    SharedPreferences sp;
    ArrayList<MyordersList> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorders);

        recyclerView = findViewById(R.id.myorders_recyclerview);
        sp = getSharedPreferences(contentsp.PREF,MODE_PRIVATE);

        db = openOrCreateDatabase("ahmedabadlive_user.db",MODE_PRIVATE,null);
        String tablequery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,USERNAME VARCHAR(100),NAME VARCHAR(100),PHONE BIGINT(10),EMAIL VARCHAR(100),PASSWORD VARCHAR(20),GENDER VARCHAR(6),CITY VARCHAR(20))";
        db.execSQL(tablequery);

        String categorytablequery = "CREATE TABLE IF NOT EXISTS CATEGORY(CATEGORYID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),IMAGE VARCHAR(100))";
        db.execSQL(categorytablequery);

        String subcategorytablequery = "CREATE TABLE IF NOT EXISTS SUBCATEGORY(SUBCATEGORYID INTEGER PRIMARY KEY AUTOINCREMENT,CATEGORYID VARCHAR(10),NAME VARCHAR(100),IMAGE VARCHAR(100))";
        db.execSQL(subcategorytablequery);

        String producttablequery = "CREATE TABLE IF NOT EXISTS PRODUCTS(PRODUCTSID INTEGER PRIMARY KEY AUTOINCREMENT,SUBCATEGORYID VARCHAR(10),CATEGORYID VARCHAR(10),NAME VARCHAR(100),IMAGE VARCHAR(100),DESCRIPTION TEXT,PRICE VARCHAR(20))";
        db.execSQL(producttablequery);

        String wishlistTablequery = "CREATE TABLE IF NOT EXISTS WISHLIST(WISHLISTID INTEGER PRIMARY KEY AUTOINCREMENT, USERID VARCHAR(10), PRODUCTID VARCHAR(10))";
        db.execSQL(wishlistTablequery);

        String cartTablequery = "CREATE TABLE IF NOT EXISTS CART(CARTID INTEGER PRIMARY KEY AUTOINCREMENT, USERID VARCHAR(10),ORDERID VARCHAR(10), PRODUCTID VARCHAR(10),QTY VARCHAR(10))";
        db.execSQL(cartTablequery);

        String checkoutTablequery = "CREATE TABLE IF NOT EXISTS CHECKOUT(ORDERID INTEGER PRIMARY KEY AUTOINCREMENT ,USERID VARCHAR(10),NAME VARCHAR(100),PHONE BIGINT(10),EMAIL VARCHAR(100), ADDRESS TEXT, PINCODE VARCHAR(6), CITY VARCHAR(20), PAYVIA VARCHAR(20) ,TRANSACTIONID VARCHAR(50),TOTALAMOUNT VARCHAR(20))";
        db.execSQL(checkoutTablequery);


        recyclerView.setLayoutManager(new LinearLayoutManager(MyordersActivity.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        String selectQuery = "SELECT * FROM CHECKOUT WHERE USERID = '"+sp.getString(contentsp.USERID,"")+"' ORDER BY ORDERID DESC";
        Cursor cursor = db.rawQuery(selectQuery,null);
        arrayList = new ArrayList<>();
        if (cursor.getCount()>0){
            while (cursor.moveToNext()){
                MyordersList list = new MyordersList();
                list.setOrderId(cursor.getString(0));
                list.setName(cursor.getString(2));
                list.setAddress(cursor.getString(5));
                list.setPincode(cursor.getString(6));
                list.setCity(cursor.getString(7));
                list.setPayVia(cursor.getString(8));
                list.setTransactionId(cursor.getString(9));
                list.setTotalAmount(cursor.getString(10));
                arrayList.add(list);
            }
        }
        MyordersAdaptor adaptor = new MyordersAdaptor(MyordersActivity.this,arrayList);
        recyclerView.setAdapter(adaptor);

    }
}