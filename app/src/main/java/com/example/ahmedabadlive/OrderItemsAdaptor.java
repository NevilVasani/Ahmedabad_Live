package com.example.ahmedabadlive;

import static com.example.ahmedabadlive.WishlistActivity.recyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class OrderItemsAdaptor extends RecyclerView.Adapter<OrderItemsAdaptor.Myholder> {
    Context context;
    ArrayList<ViewOrderItemsArraylist> arrayList;
    public OrderItemsAdaptor(Context context, ArrayList<ViewOrderItemsArraylist> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_orderitems,parent,false);
        return new Myholder(view);
    }

    public class Myholder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView price,name;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.custom_orderitems_image);
            name = itemView.findViewById(R.id.custom_orderitems_name);
            price = itemView.findViewById(R.id.custom_orderitems_price);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        holder.imageView.setImageResource(Integer.parseInt(arrayList.get(position).getImage()));
        holder.name.setText(arrayList.get(position).getName());
        int productprice = Integer.parseInt(arrayList.get(position).getPrice());
        int cartqty = Integer.parseInt(arrayList.get(position).getQty());
        int cartprice = productprice * cartqty;
        holder.price.setText(contentsp.PRICE_SYMBOL + arrayList.get(position).getPrice() +"*"+arrayList.get(position).getQty() +" = " + cartprice);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
