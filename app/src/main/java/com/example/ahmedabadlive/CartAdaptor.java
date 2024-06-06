package com.example.ahmedabadlive;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartAdaptor extends RecyclerView.Adapter<CartAdaptor.MyHolder> {
    Context context;
    ArrayList<CartList> arrayList;

    SQLiteDatabase db;
    SharedPreferences sp;

    public CartAdaptor(Context context, ArrayList arrayList,SharedPreferences sp, SQLiteDatabase db) {
        this.context = context;
        this.arrayList = arrayList;
        this.db = db;
        this.sp = sp;
    }

    @NonNull
    @Override
    public CartAdaptor.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cart,parent,false);
        return new CartAdaptor.MyHolder(view);
    }


    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView imageView,plus,minus;
        TextView textView,price,qty;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.custom_cart_image);
            textView = itemView.findViewById(R.id.custom_cart_name);
            qty = itemView.findViewById(R.id.custom_cart_qty);
            price = itemView.findViewById(R.id.custom_cart_price);
            plus = itemView.findViewById(R.id.custom_cart_plus);
            minus = itemView.findViewById(R.id.custom_cart_minus);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdaptor.MyHolder holder, int position) {
        holder.imageView.setImageResource(Integer.parseInt(arrayList.get(position).getImage()));
        holder.textView.setText(arrayList.get(position).getName());
        holder.price.setText(contentsp.PRICE_SYMBOL + arrayList.get(position).getPrice());
        holder.qty.setText(arrayList.get(position).getQty());
        holder.plus.setImageResource(R.drawable.plus);
        holder.minus.setImageResource(R.drawable.minus);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().putString(contentsp.PRODUCT_ID,arrayList.get(position).getProductId()).commit();
                new CommonMethod(context, ProductDetailsActivity.class);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}

