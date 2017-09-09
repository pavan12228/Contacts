package com.example.ravinderreddy.contacts.Utils;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import retrofit.RestAdapter;

public class Utils extends AppCompatActivity

{
      public static ApiServiceCall callApi(String middleUrl) {
        RestAdapter adapter = new  RestAdapter.Builder().
                setEndpoint(StringConstants.base_url+middleUrl)
                .build();
       return  adapter.create(ApiServiceCall.class);
    }


    public static void message(Context mContext, String message){
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
}