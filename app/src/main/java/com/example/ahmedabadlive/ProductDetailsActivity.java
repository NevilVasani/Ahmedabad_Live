package com.example.ahmedabadlive;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProductDetailsActivity extends AppCompatActivity {


    SQLiteDatabase db;
    SharedPreferences sp;

    ImageView image, wishlist, addtobag;
    TextView name,price,description;
    Boolean iswishlist = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        sp = getSharedPreferences(contentsp.PREF,MODE_PRIVATE);
        image = findViewById(R.id.product_details_image);
        name = findViewById(R.id.product_details_name);
        price = findViewById(R.id.product_details_price);
        description = findViewById(R.id.product_details_description);

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

        String selectQuery = "SELECT * FROM PRODUCTS WHERE PRODUCTSID = '"+sp.getString(contentsp.PRODUCT_ID,"")+"'";
        Cursor cursor = db.rawQuery(selectQuery,null);

        if (cursor.getCount()>0){
            while (cursor.moveToNext()){
                name.setText(cursor.getString(3));
                price.setText(contentsp.PRICE_SYMBOL + cursor.getString(6));
                description.setText(cursor.getString(5));
                image.setImageResource(Integer.parseInt(cursor.getString(4)));
            }
        }
        else {
            new CommonMethod(ProductDetailsActivity.this,"Product not found!");
        }



        wishlist = findViewById(R.id.product_detail_wishlist);
        addtobag = findViewById(R.id.product_detail_bag);

        String wishlistselectQuery = "SELECT * FROM WISHLIST WHERE USERID = '"+sp.getString(contentsp.USERID,"")+"' AND PRODUCTID = '"+sp.getString(contentsp.PRODUCT_ID,"")+"'";
        Cursor cursor1 = db.rawQuery(wishlistselectQuery,null);

        if (cursor1.getCount()>0){
            wishlist.setImageResource(R.drawable.wishlist_fill);
            iswishlist = true;
        }
        else {
            wishlist.setImageResource(R.drawable.wishlist_empty);
            iswishlist = false;
        }
        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (iswishlist) {
                    String deleteQuery = "DELETE FROM WISHLIST WHERE USERID = '"+sp.getString(contentsp.USERID,"")+"' AND PRODUCTID = '"+sp.getString(contentsp.PRODUCT_ID,"")+"'";
                    db.execSQL(deleteQuery);
                    wishlist.setImageResource(R.drawable.wishlist_empty);
                    iswishlist = false;
                }
                else {
                    String insertQuery = "INSERT INTO WISHLIST VALUES(NULL, '" + sp.getString(contentsp.USERID, "") + "', '" + sp.getString(contentsp.PRODUCT_ID, "") + "')";
                    db.execSQL(insertQuery);
                    wishlist.setImageResource(R.drawable.wishlist_fill);
                    iswishlist = true;
                }
            }
        });
    }
}