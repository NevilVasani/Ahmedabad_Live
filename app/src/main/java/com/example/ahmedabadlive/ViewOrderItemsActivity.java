package com.example.ahmedabadlive;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.sql.SQLInput;
import java.util.ArrayList;

public class ViewOrderItemsActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    TextView orderid,price,address,payvia;
    SQLiteDatabase db;
    SharedPreferences sp;
    ArrayList<ViewOrderItemsArraylist> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_order_items);

        recyclerView = findViewById(R.id.vieworderitems_recyclerlayout);

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


        recyclerView.setLayoutManager(new LinearLayoutManager(ViewOrderItemsActivity.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        orderid = findViewById(R.id.vieworderitems_orderid);
        price = findViewById(R.id.vieworderitems_price);
        address = findViewById(R.id.vieworderitems_address);
        payvia = findViewById(R.id.vieworderitems_payvia);

        String selectQuery = "SELECT * FROM CHECKOUT WHERE ORDERID = '"+sp.getString(contentsp.ORDER_ID,"")+"'";
        Cursor cursor = db.rawQuery(selectQuery,null);

        if (cursor.getCount()>0){
            while (cursor.moveToNext()){
                orderid.setText("Order ID : "+cursor.getString(0));
                price.setText(contentsp.PRICE_SYMBOL+cursor.getString(10));
                address.setText(cursor.getString(5)+"-"+cursor.getString(7)+"-"+cursor.getString(6));
                if (cursor.getString(8).equalsIgnoreCase("COD")){
                    payvia.setText(cursor.getString(8));
                }
                else {
                    payvia.setText(cursor.getString(8)+" ("+cursor.getString(9)+")");
                }
            }
        }


        String productQuery = "SELECT * FROM CART WHERE ORDERID = '"+sp.getString(contentsp.ORDER_ID,"")+"'";
        Cursor cursor1 = db.rawQuery(productQuery,null);

        if (cursor1.getCount()>0){
            arrayList = new ArrayList<>();
            while (cursor1.moveToNext()){
                ViewOrderItemsArraylist list = new ViewOrderItemsArraylist();
                list.setCartId(cursor1.getString(0));
                list.setQty(cursor1.getString(4));

                String productselectQuery = "SELECT * FROM PRODUCTS WHERE PRODUCTSID = '" + cursor1.getString(3) + "'";
                Cursor cursor2 = db.rawQuery(productselectQuery, null);

                if (cursor2.moveToNext()) {
                    list.setProductId(cursor2.getString(0));
                    list.setSubCategoryId(cursor2.getString(1));
                    list.setCategoryId(cursor2.getString(2));
                    list.setName(cursor2.getString(3));
                    list.setImage(cursor2.getString(4));
                    list.setDescription(cursor2.getString(5));
                    list.setPrice(cursor2.getString(6));
                }
                arrayList.add(list);
            }
        }

        OrderItemsAdaptor adaptor = new OrderItemsAdaptor(ViewOrderItemsActivity.this,arrayList);
        recyclerView.setAdapter(adaptor);

    }
}