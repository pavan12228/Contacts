package com.example.ravinderreddy.contacts.Utils;

import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Ravinder Reddy on 09-09-2017.
 */

public interface ApiServiceCall {

    @GET("/user_reg.php?mobile=")
    public void SignupAPi(@Query("mobile") String number, Callback<JsonObject> jsonObjectCallback);


    @GET("/get_numbers.php?contact_list=&sender_id=")
    public void PushContactsAPi(@Query("contact_list") String contact_list,@Query("sender_id") String sender_id, Callback<JsonObject> jsonObjectCallback);

}
