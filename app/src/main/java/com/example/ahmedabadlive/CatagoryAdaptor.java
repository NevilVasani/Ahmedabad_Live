package com.example.ahmedabadlive;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CatagoryAdaptor extends RecyclerView.Adapter<CatagoryAdaptor.MyHolder> {

    Context context;
    String[] nameArray;
    Integer[] imageArray;
    public CatagoryAdaptor(Context context, String[] nameArray, Integer[] imageArray) {

        this.context = context;
        this.nameArray = nameArray;
        this. imageArray = imageArray;
    }

    @NonNull
    @Override
    public CatagoryAdaptor.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_catagory,parent,false);
        return new MyHolder(view);
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
    public void onBindViewHolder(@NonNull CatagoryAdaptor.MyHolder holder, int position) {
        holder.imageView.setImageResource(imageArray[position]);
        holder.textView.setText(nameArray[position]);
    }

    @Override
    public int getItemCount() {
        return nameArray.length;
    }
}
