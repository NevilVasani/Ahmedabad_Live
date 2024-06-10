package com.example.ahmedabadlive;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductAdaptor extends RecyclerView.Adapter<ProductAdaptor.MyHolder> {
    Context context;
    ArrayList<ProductList> arrayList;
    SharedPreferences sp;
    SQLiteDatabase db;
    int iqty = 0;


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

        ImageView imageView,wishlist,addtobag,minus,plus;
        LinearLayout qtyLayout;
        TextView textView,price,brand,qty;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.custom_product_image);
            textView = itemView.findViewById(R.id.custom_product_text);
            price = itemView.findViewById(R.id.custom_product_price);
            wishlist = itemView.findViewById(R.id.custom_product_wishlist);
            addtobag = itemView.findViewById(R.id.custom_product_bag);
            brand = itemView.findViewById(R.id.custom_product_brand);
            qty = itemView.findViewById(R.id.custom_product_qty);
            qtyLayout = itemView.findViewById(R.id.custom_product_qtyLayout);
            plus = itemView.findViewById(R.id.custom_product_plus);
            minus = itemView.findViewById(R.id.custom_product_minus);
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
        holder.plus.setImageResource(R.drawable.plus);
        holder.minus.setImageResource(R.drawable.minus);
        holder.qty.setText(String.valueOf(arrayList.get(position).getiQty()));

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


        holder.addtobag.setVisibility(View.VISIBLE);
        holder.qtyLayout.setVisibility(View.GONE);

        String cartselectQuery  = "SELECT * FROM CART WHERE PRODUCTID = '"+arrayList.get(position).getId()+"' AND USERID = '"+sp.getString(contentsp.USERID,"")+"' AND ORDERID = '0'";
        Cursor cursor1 = db.rawQuery(cartselectQuery,null);

        if (cursor1.getCount()>0){
            while (cursor1.moveToNext()) {
                holder.qty.setText(cursor1.getString(4));
                iqty = Integer.parseInt(cursor1.getString(4));
            }
            holder.addtobag.setVisibility(View.GONE);
            holder.qtyLayout.setVisibility(View.VISIBLE);
        }


        holder.addtobag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iqty = 1;
                holder.qty.setText(String.valueOf(iqty));
                new CommonMethod(context,"Product Added In Cart");
                holder.addtobag.setVisibility(View.GONE);
                holder.qtyLayout.setVisibility(View.VISIBLE);
                String insertQuery  = "INSERT INTO CART VALUES (NULL, '"+sp.getString(contentsp.USERID,"")+"','0','"+arrayList.get(position).getId()+"','1')";
                db.execSQL(insertQuery);

                String deleteQuery = "DELETE FROM WISHLIST WHERE USERID = '"+sp.getString(contentsp.USERID,"")+"' AND PRODUCTID = '"+arrayList.get(position).getId()+"'";
                db.execSQL(deleteQuery);
                holder.wishlist.setImageResource(R.drawable.wishlist_empty);
            }
        });



        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatecart("Plus");
                holder.qty.setText(String.valueOf(iqty));
            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
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
                            holder.addtobag.setVisibility(View.VISIBLE);
                            holder.qtyLayout.setVisibility(View.GONE);
                        }
                    }
                }
                else{
                    updatecart("Minus");
                    holder.qty.setText(String.valueOf(iqty));
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

            }
        }
        else {
            if (cursor4.getCount()>0){
                while (cursor4.moveToNext()) {
                    iqty = Integer.parseInt(cursor4.getString(4)) - 1;
                    String updateQuery = "UPDATE CART SET QTY = '"+iqty+"' WHERE CARTID = '"+cursor4.getString(0)+"'";
                    db.execSQL(updateQuery);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return arrayList != null ? arrayList.size() : 0;
    }
}
