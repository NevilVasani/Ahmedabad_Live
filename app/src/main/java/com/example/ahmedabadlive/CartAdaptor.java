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
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cart,parent,false);
        return new MyHolder(view);
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
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.imageView.setImageResource(Integer.parseInt(arrayList.get(position).getImage()));
        holder.textView.setText(arrayList.get(position).getName());
        int productprice = Integer.parseInt(arrayList.get(position).getPrice());
        int cartqty = Integer.parseInt(arrayList.get(position).getQty());
        int cartprice = productprice * cartqty;
        holder.price.setText(contentsp.PRICE_SYMBOL + arrayList.get(position).getPrice() +"*"+arrayList.get(position).getQty() +" = " + cartprice);
        holder.qty.setText(arrayList.get(position).getQty());
        holder.plus.setImageResource(R.drawable.plus);
        holder.minus.setImageResource(R.drawable.minus);

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int iqty = Integer.parseInt(arrayList.get(position).getQty()) + 1;
                String icartid = arrayList.get(position).getCartId();
                SetCartlist(position,iqty,icartid,"plus");
            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int iqty = Integer.parseInt(arrayList.get(position).getQty()) - 1;
                String icartid = arrayList.get(position).getCartId();

                if (iqty<=0){
                    int iprice = Integer.parseInt(arrayList.get(position).getPrice());
                    CartActivity.itotalcount -= iprice;

                    if (CartActivity.itotalcount<=0){
                        CartActivity.dataview.setVisibility(View.GONE);
                        CartActivity.emptyview.setVisibility(View.VISIBLE);
                    }
                    CartActivity.totalprice.setText("Total : "+contentsp.PRICE_SYMBOL + CartActivity.itotalcount);

                    String deleteQuery = "DELETE FROM CART WHERE CARTID = '"+icartid+"' ";
                    db.execSQL(deleteQuery);
                    arrayList.remove(position);
                    notifyDataSetChanged();
                }
                else {
                    SetCartlist(position, iqty, icartid, "minus");
                }
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

    private void SetCartlist(int position, int iqty, String icartid, String sType) {
        String updateQuery = "UPDATE CART SET QTY = '"+iqty+"' WHERE CARTID = '"+icartid+"'";
        db.execSQL(updateQuery);

        CartList list = new CartList();
        list.setCartId(arrayList.get(position).getCartId());
        list.setProductId(arrayList.get(position).getProductId());
        list.setSubCategoryId(arrayList.get(position).getSubCategoryId());
        list.setCategoryId(arrayList.get(position).getCategoryId());
        list.setName(arrayList.get(position).getName());
        list.setImage(arrayList.get(position).getImage());
        list.setDescription(arrayList.get(position).getDescription());
        list.setPrice(arrayList.get(position).getPrice());
        list.setQty(String.valueOf(iqty));
        arrayList.set(position,list);


        int iprice = Integer.parseInt(arrayList.get(position).getPrice());
        if (sType.equalsIgnoreCase("plus")){
            CartActivity.itotalcount += iprice;
        }
        else {
            CartActivity.itotalcount -= iprice;
        }
        notifyDataSetChanged();
        CartActivity.totalprice.setText("Total : "+contentsp.PRICE_SYMBOL +String.valueOf(CartActivity.itotalcount));
        sp.edit().putString(contentsp.TOTAL_CART_PRICE, String.valueOf(CartActivity.itotalcount)).commit();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}