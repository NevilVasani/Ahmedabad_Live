package com.example.ahmedabadlive;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class CatagoryActivity extends AppCompatActivity {

    String[] nameArray = {"Shirts","T-Shirts","Jeans","Shorts & Trousers","Casual Shoes","Infant Essentials"};
    Integer[] imageArray = {R.drawable.shirts,R.drawable.tshirts,R.drawable.jeans,R.drawable.shorts,R.drawable.causual_shoes,R.drawable.infant};
    RecyclerView  recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catagory);

        recyclerView = findViewById(R.id.category_recyclerview);


        //for sat as a grid horizontal
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));

        //for set as a gird vertical
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        //for set as a List
        //recyclerView.setLayoutManager(new LinearLayoutManager(CatagoryActivity.this));

        recyclerView.setItemAnimator(new DefaultItemAnimator());

       CatagoryAdaptor adaptor = new CatagoryAdaptor(CatagoryActivity.this,nameArray, imageArray);
       recyclerView.setAdapter(adaptor);
    }
}