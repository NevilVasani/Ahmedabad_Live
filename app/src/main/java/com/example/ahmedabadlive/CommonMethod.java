package com.example.ahmedabadlive;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;


public class CommonMethod {
    CommonMethod(Context context, String text){
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
    CommonMethod(View view, String text){
        Snackbar.make(view,text,Snackbar.LENGTH_SHORT).show();
    }
    CommonMethod(Context context,Class<?> nextclass){
        Intent intent = new Intent(context,nextclass);
        context.startActivity(intent);
    }
}

