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

import java.util.ArrayList;

public class WishlistActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SQLiteDatabase db;
    ArrayList<WishlistList> wishlistLists;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        sp = getSharedPreferences(contentsp.PREF,MODE_PRIVATE);

        recyclerView = findViewById(R.id.wishlist_recyclerview);

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


        //for sat as a grid horizontal
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));

        //for set as a gird vertical
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        //for set as a List
        recyclerView.setLayoutManager(new LinearLayoutManager(WishlistActivity.this));

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        wishlistLists = new ArrayList<>(); // Initialize the ArrayList here

        String selectQuery = "SELECT * FROM WISHLIST WHERE USERID = '"+sp.getString(contentsp.USERID,"")+"'";
        Cursor cursor = db.rawQuery(selectQuery,null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                WishlistList list = new WishlistList();
                list.setWishlistId(cursor.getString(cursor.getColumnIndexOrThrow("WISHLISTID")));

                String productQuery = "SELECT * FROM PRODUCTS WHERE PRODUCTSID = '" + cursor.getString(2) + "'";
                Cursor cursor1 = db.rawQuery(productQuery, null);

                if (cursor1.moveToNext()) {
                    list.setProductId(cursor1.getString(0));
                    list.setSubCategoryId(cursor1.getString(1));
                    list.setCategoryId(cursor1.getString(2));
                    list.setName(cursor1.getString(3));
                    list.setImage(cursor1.getString(4));
                    list.setDescription(cursor1.getString(5));
                    list.setPrice(cursor1.getString(6));
                }
                cursor1.close();
                wishlistLists.add(list);
            }
            cursor.close();
            WishlistAdaptor adaptor = new WishlistAdaptor(WishlistActivity.this,wishlistLists, sp, db);
            recyclerView.setAdapter(adaptor);
        }
        else {
            new CommonMethod(WishlistActivity.this,"Product not found");
        }


    }
}