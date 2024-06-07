package com.example.ahmedabadlive;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.security.PublicKey;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    public static RelativeLayout dataview,emptyview;
    RecyclerView recyclerView;
    public static TextView totalprice;
    TextView checkout;
    SQLiteDatabase db;
    ArrayList<CartList> cartLists;
    SharedPreferences sp;

    public static int itotalcount= 0;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        sp = getSharedPreferences(contentsp.PREF,MODE_PRIVATE);

        recyclerView = findViewById(R.id.cart_recyclerview);
        dataview = findViewById(R.id.cart_datalayout);
        emptyview = findViewById(R.id.cart_emptylayout);

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


        //for sat as a grid horizontal
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));

        //for set as a gird vertical
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        //for set as a List
        recyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));

        recyclerView.setItemAnimator(new DefaultItemAnimator());


        totalprice = findViewById(R.id.cart_total);
        checkout = findViewById(R.id.cart_checkout);




        cartLists = new ArrayList<>(); // Initialize the ArrayList here

        String selectQuery = "SELECT * FROM CART WHERE USERID = '"+sp.getString(contentsp.USERID,"")+"' AND ORDERID = '0'";
        Cursor cursor = db.rawQuery(selectQuery,null);

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    CartList list = new CartList();
                    list.setCartId(cursor.getString(0));
                    list.setQty(cursor.getString(4));

                    String productQuery = "SELECT * FROM PRODUCTS WHERE PRODUCTSID = '" + cursor.getString(3) + "'";
                    Cursor cursor1 = db.rawQuery(productQuery, null);

                    if (cursor1.moveToNext()) {
                        list.setProductId(cursor1.getString(0));
                        list.setSubCategoryId(cursor1.getString(1));
                        list.setCategoryId(cursor1.getString(2));
                        list.setName(cursor1.getString(3));
                        list.setImage(cursor1.getString(4));
                        list.setDescription(cursor1.getString(5));
                        list.setPrice(cursor1.getString(6));
                        itotalcount += Integer.parseInt(cursor.getString(4)) * Integer.parseInt(cursor1.getString(6)) ;
                    }
                    cursor1.close();
                    cartLists.add(list);
                }
                cursor.close();
                CartAdaptor adaptor = new CartAdaptor(CartActivity.this, cartLists, sp, db);
                recyclerView.setAdapter(adaptor);

                totalprice.setText("Total : "+contentsp.PRICE_SYMBOL + itotalcount);

                dataview.setVisibility(View.VISIBLE);
                emptyview.setVisibility(View.GONE);
            } else {
                new CommonMethod(CartActivity.this, "Product not found");
                dataview.setVisibility(View.GONE);
                emptyview.setVisibility(View.VISIBLE);
            }
    }
}