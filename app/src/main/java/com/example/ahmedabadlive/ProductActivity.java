//package com.example.ahmedabadlive;
//
//import android.content.SharedPreferences;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.os.Bundle;
//import android.widget.ImageView;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//import androidx.recyclerview.widget.DefaultItemAnimator;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.recyclerview.widget.StaggeredGridLayoutManager;
//
//import java.util.ArrayList;
//
//public class ProductActivity extends AppCompatActivity {
//
//    String[] categoryIDArray = {"1","1","2"};
//    String[] subcategoryIDArray = {"1","1","7"};
//    String[] nameArray = {"Men Solid Casual White Shirt"
//            ,"Black Checked Regular Fit Casual Shirt"
//            ,"Men Cotton Plain Polo Neck Grey T Shirts"};
//    String[] descriptionArray = {
//            "White and blue striped opaque casual shirt, has a spread collar, button placket, long regular sleeves, curved hem",
//            "U S POLO ASSN BLACK CHECKED REGULAR FIT CASUAL SHIRT MEN",
//            "Men Cotton Plain Polo Neck Allen Solly Grey T Shirts"
//    };
//
//    String[] priceArray = {"1999","1499","599"};
//    Integer[] imageArray = {R.drawable.us_polo_shirt,R.drawable.uspolo_regular_fit,R.drawable.allen_tshirt};
//    RecyclerView recyclerView;
//    SQLiteDatabase db;
//    ArrayList<ProductList> productList;
//    SharedPreferences sp;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_product);
//
//        sp = getSharedPreferences(contentsp.PREF,MODE_PRIVATE);
//
//        recyclerView = findViewById(R.id.product_recyclerview);
//
//        db = openOrCreateDatabase("ahmedabadlive_user.db",MODE_PRIVATE,null);
//        String tablequery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,USERNAME VARCHAR(100),NAME VARCHAR(100),PHONE BIGINT(10),EMAIL VARCHAR(100),PASSWORD VARCHAR(20),GENDER VARCHAR(6),CITY VARCHAR(20))";
//        db.execSQL(tablequery);
//
//        String categorytablequery = "CREATE TABLE IF NOT EXISTS CATEGORY(CATEGORYID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),IMAGE VARCHAR(100))";
//        db.execSQL(categorytablequery);
//
//        String subcategorytablequery = "CREATE TABLE IF NOT EXISTS SUBCATEGORY(SUBCATEGORYID INTEGER PRIMARY KEY AUTOINCREMENT,CATEGORYID VARCHAR(10),NAME VARCHAR(100),IMAGE VARCHAR(100))";
//        db.execSQL(subcategorytablequery);
//
//        String producttablequery = "CREATE TABLE IF NOT EXISTS PRODUCTS(PRODUCTSID INTEGER PRIMARY KEY AUTOINCREMENT,SUBCATEGORYID VARCHAR(10),CATEGORYID VARCHAR(10),NAME VARCHAR(100),IMAGE VARCHAR(100),DESCRIPTION TEXT,PRICE VARCHAR(20))";
//        db.execSQL(producttablequery);
//
//        String wishlistTablequery = "CREATE TABLE IF NOT EXISTS WISHLIST(WISHLISTID INTEGER PRIMARY KEY AUTOINCREMENT, USERID VARCHAR(10), PRODUCTID VARCHAR(10))";
//        db.execSQL(wishlistTablequery);
//
//        for(int i=0;i<nameArray.length;i++){
//            String selectQuery = "SELECT * FROM PRODUCTS WHERE NAME='"+nameArray[i]+"' AND CATEGORYID = '"+categoryIDArray[i]+"' AND SUBCATEGORYID = '"+subcategoryIDArray[i]+"'";
//            Cursor cursor = db.rawQuery(selectQuery,null);
//
//            if(cursor.getCount()>0){
//
//            }
//            else {
//                String insertQuery = "INSERT INTO PRODUCTS VALUES(NULL,'" + subcategoryIDArray[i] + "','" + categoryIDArray[i] + "','" + nameArray[i] + "','" + imageArray[i] + "','" + descriptionArray[i] + "','" + priceArray[i] + "')";
//                db.execSQL(insertQuery);
//            }
//        }
//
//        //for sat as a grid horizontal
//        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));
//
//        //for set as a gird vertical
//        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
//
//        //for set as a List
//        recyclerView.setLayoutManager(new LinearLayoutManager(ProductActivity.this));
//
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//
//        productList = new ArrayList<>();
//        String selectQuery = "SELECT * FROM PRODUCTS WHERE SUBCATEGORYID = '"+sp.getString(contentsp.SUB_CATEGORY_ID,"")+"'";
//        Cursor cursor = db.rawQuery(selectQuery,null);
//
//        if (cursor.getCount()>0) {
//
//            while (cursor.moveToNext()) {
//                ProductList list = new ProductList();
//                list.setId(cursor.getString(0));
//                list.setCatagoryID(cursor.getString(1));
//                list.setSubcatagoryID(cursor.getString(2));
//                list.setName(cursor.getString(3));
//                list.setImage(cursor.getString(4));
//                list.setDescription(cursor.getString(5));
//                list.setPrice(cursor.getString(6));
//                productList.add(list);
//            }
//        } else {
//            new CommonMethod(ProductActivity.this,"Product not found");
//        }
//
//        ProductAdaptor adaptor = new ProductAdaptor(ProductActivity.this,productList,sp,db);
//        recyclerView.setAdapter(adaptor);
//        }
//}




























package com.example.ahmedabadlive;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SQLiteDatabase db;
    ArrayList<ProductList> productList;
    SharedPreferences sp;
    ProductAdaptor adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        sp = getSharedPreferences(contentsp.PREF, MODE_PRIVATE);

        recyclerView = findViewById(R.id.product_recyclerview);

        db = openOrCreateDatabase("ahmedabadlive_user.db", MODE_PRIVATE, null);
        createTablesIfNotExists();

        recyclerView.setLayoutManager(new LinearLayoutManager(ProductActivity.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        productList = new ArrayList<>();

        loadProducts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
    }

    private void createTablesIfNotExists() {
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,USERNAME VARCHAR(100),NAME VARCHAR(100),PHONE BIGINT(10),EMAIL VARCHAR(100),PASSWORD VARCHAR(20),GENDER VARCHAR(6),CITY VARCHAR(20))";
        db.execSQL(tableQuery);

        String categoryTableQuery = "CREATE TABLE IF NOT EXISTS CATEGORY(CATEGORYID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),IMAGE VARCHAR(100))";
        db.execSQL(categoryTableQuery);

        String subcategoryTableQuery = "CREATE TABLE IF NOT EXISTS SUBCATEGORY(SUBCATEGORYID INTEGER PRIMARY KEY AUTOINCREMENT,CATEGORYID VARCHAR(10),NAME VARCHAR(100),IMAGE VARCHAR(100))";
        db.execSQL(subcategoryTableQuery);

        String productTableQuery = "CREATE TABLE IF NOT EXISTS PRODUCTS(PRODUCTSID INTEGER PRIMARY KEY AUTOINCREMENT,SUBCATEGORYID VARCHAR(10),CATEGORYID VARCHAR(10),NAME VARCHAR(100),IMAGE VARCHAR(100),DESCRIPTION TEXT,PRICE VARCHAR(20))";
        db.execSQL(productTableQuery);

        String wishlistTableQuery = "CREATE TABLE IF NOT EXISTS WISHLIST(WISHLISTID INTEGER PRIMARY KEY AUTOINCREMENT, USERID VARCHAR(10), PRODUCTID VARCHAR(10))";
        db.execSQL(wishlistTableQuery);
    }

    private void loadProducts() {
        productList.clear(); // Clear the list to avoid duplication

        String selectQuery = "SELECT * FROM PRODUCTS WHERE SUBCATEGORYID = '" + sp.getString(contentsp.SUB_CATEGORY_ID, "") + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ProductList list = new ProductList();
                list.setId(cursor.getString(0));
                list.setCatagoryID(cursor.getString(1));
                list.setSubcatagoryID(cursor.getString(2));
                list.setName(cursor.getString(3));
                list.setImage(cursor.getString(4));
                list.setDescription(cursor.getString(5));
                list.setPrice(cursor.getString(6));
                productList.add(list);
            }
        } else {
            new CommonMethod(ProductActivity.this, "Product not found");
        }

        if (adaptor == null) {
            adaptor = new ProductAdaptor(ProductActivity.this, productList, sp, db);
            recyclerView.setAdapter(adaptor);
        } else {
            adaptor.notifyDataSetChanged();
        }

        cursor.close();
    }
}
