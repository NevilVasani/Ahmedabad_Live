package com.example.ahmedabadlive;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyordersAdaptor extends RecyclerView.Adapter<MyordersAdaptor.Myholder> {
    Context context;
    ArrayList<MyordersList> arrayList;
    SharedPreferences sp;

    public MyordersAdaptor(Context context, ArrayList<MyordersList> arrayList) {
        this.context =context;
        this.arrayList = arrayList;
        sp = context.getSharedPreferences(contentsp.PREF,Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_myorders,parent,false);
        return new Myholder(view);
    }

    public class Myholder extends RecyclerView.ViewHolder{

        TextView orderid,price,address,payvia;
        LinearLayout orderLayout;
        @SuppressLint("WrongViewCast")
        public Myholder(@NonNull View itemView) {
            super(itemView);
            orderid = itemView.findViewById(R.id.custom_myorders_orderid);
            price = itemView.findViewById(R.id.custom_myorders_price);
            address = itemView.findViewById(R.id.custom_myorders_address);
            payvia = itemView.findViewById(R.id.custom_myorders_payvia);
            orderLayout = itemView.findViewById(R.id.custom_myorders_layout);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {

        holder.orderid.setText("Order Id : "+ arrayList.get(position).getOrderId());
        holder.price.setText(contentsp.PRICE_SYMBOL + arrayList.get(position).getTotalAmount());
        holder.address.setText(arrayList.get(position).getAddress() + "-" + arrayList.get(position).getCity()+ "-" +arrayList.get(position).getPincode());

        if (arrayList.get(position).getPayVia().equalsIgnoreCase("COD")){
            holder.payvia.setText(arrayList.get(position).getPayVia());
        }
        else {
            holder.payvia.setText(arrayList.get(position).getPayVia() +" ("+ arrayList.get(position).getTransactionId()+")");
        }

        holder.orderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().putString(contentsp.ORDER_ID,arrayList.get(position).getOrderId()).commit();
                new CommonMethod(context,ViewOrderItemsActivity.class);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
