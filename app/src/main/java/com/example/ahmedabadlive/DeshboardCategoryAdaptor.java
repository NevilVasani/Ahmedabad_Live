package com.example.ahmedabadlive;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class DeshboardCategoryAdaptor extends RecyclerView.Adapter<DeshboardCategoryAdaptor.viewHolder> {

    public ArrayList<CategoryDomain> arrayList;
    public Context context;

    public DeshboardCategoryAdaptor(ArrayList<CategoryDomain> arrayList ,Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public DeshboardCategoryAdaptor.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category_deshboard,parent,false);
        return new DeshboardCategoryAdaptor.viewHolder(view);
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.pic);
            textView = itemView.findViewById(R.id.text);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull DeshboardCategoryAdaptor.viewHolder holder, int position) {
        holder.textView.setText(arrayList.get(position).getTitle());

        Glide.with(context)
                .load(arrayList.get(position).getPicUrl())
                .into(holder.imageView);
        String abc = arrayList.get(position).getTitle();

        Log.d("CategoryData", "Title: " + arrayList.get(position).getTitle() + ", Image URL: " + arrayList.get(position).getPicUrl());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}