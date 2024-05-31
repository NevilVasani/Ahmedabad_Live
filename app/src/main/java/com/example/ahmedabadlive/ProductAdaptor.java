package com.example.ahmedabadlive;

import android.content.Context;
import android.content.SharedPreferences;
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

    public ProductAdaptor(Context context, ArrayList arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        sp = context.getSharedPreferences(contentsp.PREF,Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public ProductAdaptor.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_product,parent,false);
        return new ProductAdaptor.MyHolder(view);
    }


    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView,price;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.product_image);
            textView = itemView.findViewById(R.id.product_text);
            price = itemView.findViewById(R.id.product_price);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdaptor.MyHolder holder, int position) {
        holder.imageView.setImageResource(Integer.parseInt(arrayList.get(position).getImage()));
        holder.textView.setText(arrayList.get(position).getName());
        holder.price.setText(contentsp.PRICE_SYMBOL + arrayList.get(position).getPrice());

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
        return arrayList.size();
    }
}
