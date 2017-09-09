package com.example.ravinderreddy.contacts.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ravinderreddy.contacts.R;
import com.example.ravinderreddy.contacts.Utils.ApiServiceCall;
import com.example.ravinderreddy.contacts.Utils.Utils;
import com.example.ravinderreddy.contacts.adapters.CustomAdapter;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private CustomAdapter customAdapter;
    private ArrayList<String> contactModelArrayList;
    private String sNumber;
    private String user_id,phoneNumber;
    Button btnpostData;
    private String requestedResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle b=this.getIntent().getExtras();
        if(b!=null){
            user_id=b.getString("user_id");
        }
        listView = (ListView) findViewById(R.id.listView);
        btnpostData= (Button) findViewById(R.id.postData);


        contactModelArrayList = new ArrayList<>();

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");
        while (phones.moveToNext())
        {
             phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            phoneNumber = phoneNumber.replaceAll("[\\D]", "");
            if (phoneNumber.length() >= 10) {
                 phoneNumber = phoneNumber.substring(phoneNumber.length() - 10);
                if (!contactModelArrayList.contains(phoneNumber)) {
                    contactModelArrayList.add(phoneNumber);
                    //  contactModelArrayList.add(contactModel);
                    Log.d("phone>>", "" + phoneNumber);
                }
                 }
              }
        phones.close();

        customAdapter = new CustomAdapter(this,contactModelArrayList);
        listView.setAdapter(customAdapter);


        btnpostData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               PostContatcsData();
               // new ParseJSONTask().execute();
            }
        });


    }
    private class ParseJSONTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //dialog.setMessage("Loading.....");
            // dialog.setCancelable(false);
            // dialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            //  if (dialog.isShowing()) {
            try {

                String[] result1;
                String status;
                String u_id, mobile;

                JSONObject jsonObject = new JSONObject(requestedResponse);
                status = jsonObject.getString("success");
                if (status.contains("true")) {

                    JSONArray data = jsonObject.getJSONArray("data");

                    // looping through all items
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);
                        mobile = object.getString("mobile");
                        u_id = object.getString("user_id");


                        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(mobile));

                        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

                        String contactName = "";
                        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                contactName = cursor.getString(0);

                                Log.e("Name", contactName);
                            }
                            cursor.close();
                        }




                    }





                } else if (status.contains("false")) {
                    Toast.makeText(getApplicationContext(), "No Records Found", Toast.LENGTH_LONG).show();
                    //dialog.dismiss();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            // dialog.dismiss();
            // }

        }

        @Override
        protected Void doInBackground(Void... params) {

            String api = "http://52.66.43.145/rama/app/get_numbers.php?contact_list=" + phoneNumber +user_id;
            String response = null;

            Log.e("URL", api);

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(api);
            try {
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                response = EntityUtils.toString(httpEntity);
                requestedResponse = response;

                Log.e("Response", response);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

    }







    private void PostContatcsData() {
        ApiServiceCall apiServiceCall = Utils.callApi("");
      apiServiceCall.PushContactsAPi(phoneNumber, user_id, new Callback<JsonObject>() {
          @Override
          public void success(JsonObject jsonObject, Response response) {

              Log.d("response", jsonObject.toString());
              JsonObject jsonObject1 = jsonObject.getAsJsonObject();
              String message = jsonObject1.get("success").getAsString();
              if (message.equals("true")) {
                  //status = jsonObject1.get("error").getAsString();
                  //Utils.message(getApplicationContext(), status);
                  JsonArray jsonArray = jsonObject1.get("data").getAsJsonArray();
                  for (int i = 0; i < jsonArray.size(); i++) {
                      JsonObject jsonObject2 = jsonArray.get(i).getAsJsonObject();
                      user_id = jsonObject2.get("user_id").getAsString();
                      String RegisteredNumber = jsonObject2.get("mobile").getAsString();
                      Log.d("user_id", user_id);
                      Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                      intent.putExtra("user_id", user_id);
                      startActivity(intent);
                  }

              }
              }

          @Override
          public void failure(RetrofitError error) {
              Utils.message(getApplicationContext(), "retrofit error" + error.toString());

          }
      });

      }

}