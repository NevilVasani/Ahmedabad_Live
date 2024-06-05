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

public class WishlistAdaptor extends RecyclerView.Adapter<WishlistAdaptor.MyHolder> {
    Context context;
    ArrayList<WishlistList> arrayList;

    SQLiteDatabase db;
    SharedPreferences sp;

    public WishlistAdaptor(Context context, ArrayList arrayList,SharedPreferences sp, SQLiteDatabase db) {
        this.context = context;
        this.arrayList = arrayList;
        this.db = db;
        this.sp = sp;
    }

    @NonNull
    @Override
    public WishlistAdaptor.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_wishlist,parent,false);
        return new WishlistAdaptor.MyHolder(view);
    }


    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView imageView,wishlist,addtobag;
        TextView textView,price;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.custom_wishlist_image);
            textView = itemView.findViewById(R.id.custom_wishlist_text);
            price = itemView.findViewById(R.id.custom_wishlist_price);
            wishlist = itemView.findViewById(R.id.custom_wishlist_wishlist);
            addtobag = itemView.findViewById(R.id.custom_wishlist_bag);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistAdaptor.MyHolder holder, int position) {
        holder.imageView.setImageResource(Integer.parseInt(arrayList.get(position).getImage()));
        holder.textView.setText(arrayList.get(position).getName());
        holder.price.setText(contentsp.PRICE_SYMBOL + arrayList.get(position).getPrice());
        holder.wishlist.setImageResource(R.drawable.wishlist_fill);
        holder.addtobag.setImageResource(R.drawable.shoping_bag);

        holder.wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deleteQuery = "DELETE FROM WISHLIST WHERE WISHLISTID = '"+arrayList.get(position).getWishlistId()+"'";
                db.execSQL(deleteQuery);
                arrayList.remove(position);
                notifyDataSetChanged();
//                notifyItemRemoved(position);
//                notifyItemRangeChanged(position, arrayList.size());
            }
        });

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

