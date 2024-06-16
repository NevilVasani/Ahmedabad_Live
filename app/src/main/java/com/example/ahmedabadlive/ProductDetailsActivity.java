package com.example.ahmedabadlive;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.camera2.CameraExtensionSession;
import android.health.connect.datatypes.StepsRecord;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProductDetailsActivity extends AppCompatActivity {


    SQLiteDatabase db;
    SharedPreferences sp;

    ImageView image, wishlist, addtobag, plus, minus;
    TextView name,price,description, qty;
    LinearLayout qtyLayout;
    Boolean iswishlist = false;
    int iqty = 0;
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

        String producttablequery = "CREATE TABLE IF NOT EXISTS PRODUCTS(PRODUCTSID INTEGER PRIMARY KEY AUTOINCREMENT,SUBCATEGORYID VARCHAR(10),CATEGORYID VARCHAR(10),NAME VARCHAR(100),IMAGE VARCHAR(100),DESCRIPTION TEXT,PRICE VARCHAR(20),BRAND VARCHAR(20))";
        db.execSQL(producttablequery);

        String wishlistTablequery = "CREATE TABLE IF NOT EXISTS WISHLIST(WISHLISTID INTEGER PRIMARY KEY AUTOINCREMENT, USERID VARCHAR(10), PRODUCTID VARCHAR(10))";
        db.execSQL(wishlistTablequery);

        String cartTablequery = "CREATE TABLE IF NOT EXISTS CART(CARTID INTEGER PRIMARY KEY AUTOINCREMENT, USERID VARCHAR(10),ORDERID VARCHAR(10), PRODUCTID VARCHAR(10),QTY VARCHAR(10))";
        db.execSQL(cartTablequery);

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


        addtobag = findViewById(R.id.product_detail_bag);
        qtyLayout = findViewById(R.id.product_detail_qtyLayout);
        qty  = findViewById(R.id.product_detailt_qty);
        plus = findViewById(R.id.product_detail_plus);
        minus  = findViewById(R.id.product_detail_minus);

        addtobag.setVisibility(View.VISIBLE);
        qtyLayout.setVisibility(View.GONE);

        String cartselectQuery  = "SELECT * FROM CART WHERE PRODUCTID = '"+sp.getString(contentsp.PRODUCT_ID,"")+"' AND ORDERID = '0'";
        Cursor cursor2  = db.rawQuery(cartselectQuery,null);

        if (cursor2.getCount()>0){
            while (cursor2.moveToNext()) {
                qty.setText(cursor2.getString(4));
                iqty = Integer.parseInt(cursor2.getString(4));
            }
            addtobag.setVisibility(View.GONE);
            qtyLayout.setVisibility(View.VISIBLE);
        }
        addtobag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cartselectQuery  = "SELECT * FROM CART WHERE PRODUCTID = '"+sp.getString(contentsp.PRODUCT_ID,"")+"' AND ORDERID = '0'";
                Cursor cursor3  = db.rawQuery(cartselectQuery,null);

                if (cursor3.getCount()>0){
                    new CommonMethod(ProductDetailsActivity.this,"Product Already Added In Cart");
                }
                else {
                    String insertQuery  = "INSERT INTO CART VALUES (NULL, '"+sp.getString(contentsp.USERID,"")+"','0','"+sp.getString(contentsp.PRODUCT_ID,"")+"','1')";
                    db.execSQL(insertQuery);
                    addtobag.setVisibility(View.GONE);
                    qtyLayout.setVisibility(View.VISIBLE);
                    iswishlist =false;
                    String deleteQuery = "DELETE FROM WISHLIST WHERE USERID = '"+sp.getString(contentsp.USERID,"")+"' AND PRODUCTID = '"+sp.getString(contentsp.PRODUCT_ID,"")+"'";
                    db.execSQL(deleteQuery);
                    wishlist.setImageResource(R.drawable.wishlist_empty);
                    iqty = 1;
                    qty.setText(String.valueOf(iqty));
                    new CommonMethod(ProductDetailsActivity.this,"Product Added In Cart");
                }
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatecart("Plus");
            }
        });


        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iqty-=1;

                if (iqty<=0){
                    String cartselectQuery  = "SELECT * FROM CART WHERE PRODUCTID = '"+sp.getString(contentsp.PRODUCT_ID,"")+"' AND ORDERID = '0'";
                    Cursor cursor  = db.rawQuery(cartselectQuery,null);
                    if (cursor.getCount()>0){
                        while (cursor.moveToNext()) {
                            String deleteQuery = "DELETE FROM CART WHERE CARTID = '"+cursor.getString(0)+"'";
                            db.execSQL(deleteQuery);
                            iqty = 0;
                            addtobag.setVisibility(View.VISIBLE);
                            qtyLayout.setVisibility(View.GONE);
                        }
                    }
                }
                else{
                    updatecart("Minus");
                }
            }
        });
    }

    private void updatecart(String sType) {

        String cartselectQuery  = "SELECT * FROM CART WHERE PRODUCTID = '"+sp.getString(contentsp.PRODUCT_ID,"")+"' AND ORDERID = '0'";
        Cursor cursor4  = db.rawQuery(cartselectQuery,null);

        if (sType.equalsIgnoreCase("Plus")){
            if (cursor4.getCount()>0){
                while (cursor4.moveToNext()) {
                    iqty = Integer.parseInt(cursor4.getString(4)) + 1;
                    String updateQuery = "UPDATE CART SET QTY = '"+iqty+"' WHERE CARTID = '"+cursor4.getString(0)+"'";
                    db.execSQL(updateQuery);
                }
                qty.setText(String.valueOf(iqty));
            }
        }
        else {
            if (cursor4.getCount()>0){
                while (cursor4.moveToNext()) {
                    iqty = Integer.parseInt(cursor4.getString(4)) - 1;
                    String updateQuery = "UPDATE CART SET QTY = '"+iqty+"' WHERE CARTID = '"+cursor4.getString(0)+"'";
                    db.execSQL(updateQuery);
                }
                qty.setText(String.valueOf(iqty));
            }
        }
    }
}