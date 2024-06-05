package com.example.ahmedabadlive;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductAdaptor extends RecyclerView.Adapter<ProductAdaptor.MyHolder> {
    Context context;
    ArrayList<ProductList> arrayList;
    SharedPreferences sp;
    SQLiteDatabase db;

    public ProductAdaptor(Context context, ArrayList<ProductList> arrayList, SharedPreferences sp, SQLiteDatabase db) {
        this.context = context;
        this.arrayList = arrayList;
        this.sp = sp;
        this.db = db;
    }

    @NonNull
    @Override
    public ProductAdaptor.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_product,parent,false);
        return new ProductAdaptor.MyHolder(view);
    }


    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView imageView,wishlist,addtobag;
        TextView textView,price,brand;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.custom_product_image);
            textView = itemView.findViewById(R.id.custom_product_text);
            price = itemView.findViewById(R.id.custom_product_price);
            wishlist = itemView.findViewById(R.id.custom_product_wishlist);
            addtobag = itemView.findViewById(R.id.custom_product_bag);
            brand = itemView.findViewById(R.id.custom_product_brand);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdaptor.MyHolder holder, int position) {
        holder.imageView.setImageResource(Integer.parseInt(arrayList.get(position).getImage()));
        holder.textView.setText(arrayList.get(position).getName());
        holder.price.setText(contentsp.PRICE_SYMBOL + arrayList.get(position).getPrice());
        holder.wishlist.setImageResource(R.drawable.wishlist_empty);
        holder.addtobag.setImageResource(R.drawable.shoping_bag);
        holder.brand.setText(arrayList.get(position).getBrand());


        String selectQuery = "SELECT * FROM WISHLIST WHERE USERID = '" + sp.getString(contentsp.USERID, "0") + "' AND PRODUCTID = '" + arrayList.get(position).getId() + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.getCount() > 0) {
            holder.wishlist.setImageResource(R.drawable.wishlist_fill);
        } else {
            holder.wishlist.setImageResource(R.drawable.wishlist_empty);
        }
        cursor.close();
        holder.wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = db.rawQuery(selectQuery, null);
                if (cursor.getCount()>0){
                    String deleteQuery = "DELETE FROM WISHLIST WHERE USERID = '"+sp.getString(contentsp.USERID,"")+"' AND PRODUCTID = '"+arrayList.get(position).getId()+"'";
                    db.execSQL(deleteQuery);
                    holder.wishlist.setImageResource(R.drawable.wishlist_empty);
                }
                else {
                    String insertQuery = "INSERT INTO WISHLIST VALUES(NULL, '" + sp.getString(contentsp.USERID, "") + "', '" + arrayList.get(position).getId() + "')";
                    db.execSQL(insertQuery);
                    holder.wishlist.setImageResource(R.drawable.wishlist_fill);
                }
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().putString(contentsp.PRODUCT_ID,arrayList.get(position).getId()).commit();
                new CommonMethod(context, ProductDetailsActivity.class);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList != null ? arrayList.size() : 0;
    }
}
