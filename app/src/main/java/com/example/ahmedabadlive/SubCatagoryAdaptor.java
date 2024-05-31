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

public class SubCatagoryAdaptor extends RecyclerView.Adapter<SubCatagoryAdaptor.MyHolder> {

    Context context;
    ArrayList<SubCatagoryList> arrayList;

    SharedPreferences sp;

    public SubCatagoryAdaptor(Context context, ArrayList arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        sp = context.getSharedPreferences(contentsp.PREF,Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public SubCatagoryAdaptor.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_catagory,parent,false);
        return new SubCatagoryAdaptor.MyHolder(view);
    }


    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.custom_category_image);
            textView = itemView.findViewById(R.id.custom_category_text);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull SubCatagoryAdaptor.MyHolder holder, int position) {
        holder.imageView.setImageResource(Integer.parseInt(arrayList.get(position).getImage()));
        holder.textView.setText(arrayList.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().putString(contentsp.SUB_CATEGORY_ID,arrayList.get(position).getId()).commit();
                sp.edit().putString(contentsp.SUB_CATEGORY_NAME,arrayList.get(position).getName()).commit();
                new CommonMethod(context, ProductActivity.class);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
