package com.example.ravinderreddy.contacts.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ravinderreddy.contacts.R;
import com.example.ravinderreddy.contacts.Utils.ApiServiceCall;
import com.example.ravinderreddy.contacts.Utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SignupActivity extends Activity {
    RuntimePermissions runtimePermissions;
    EditText mEditText;
    Button mButton;
    private String status;
    private String user_id;
    private String SphoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        runtimePermissions = new RuntimePermissions(this);
        runtimePermissions.checkExplainRequestPermission(Manifest.permission.READ_CONTACTS);
        mEditText = (EditText) findViewById(R.id.etNumber);
        mButton = (Button) findViewById(R.id.btnSubmit);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validaation();
            }
        });

    }

    private void validaation() {

         SphoneNumber = mEditText.getText().toString().trim();
        if (SphoneNumber.length() != 10) {
            Toast.makeText(this, "please enter 10 digit mobile number!!!", Toast.LENGTH_SHORT).show();
        } else {
            postSignupApi();
        }

    }

    private void postSignupApi() {
        ApiServiceCall apiServiceCall = Utils.callApi("");
        apiServiceCall.SignupAPi(SphoneNumber, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                Log.d("response", jsonObject.toString());
                JsonObject jsonObject1 = jsonObject.getAsJsonObject();
                String message = jsonObject1.get("success").getAsString();
                if (message.equals("true")) {
                    status = jsonObject1.get("error").getAsString();
                    Utils.message(getApplicationContext(), status);
                    JsonArray jsonArray = jsonObject1.get("data").getAsJsonArray();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonObject jsonObject2 = jsonArray.get(i).getAsJsonObject();
                        user_id = jsonObject2.get("user_id").getAsString();
                        Log.d("user_id", user_id);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("user_id", user_id);
                        startActivity(intent);
                    }
                } else {
                    if (message.equals("false")) {
                        status = jsonObject1.get("error").getAsString();
                        Utils.message(getApplicationContext(), status);
                        JsonArray jsonArray = jsonObject1.get("data").getAsJsonArray();
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject jsonObject2 = jsonArray.get(i).getAsJsonObject();
                            user_id = jsonObject2.get("user_id").getAsString();
                            Log.d("user_id", user_id);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("user_id", user_id);
                            startActivity(intent);

                        }
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Utils.message(getApplicationContext(), "retrofit error" + error.toString());
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        runtimePermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (runtimePermissions.getStatusPermission()) {
            Toast.makeText(this, "Successfully granted", Toast.LENGTH_SHORT).show();
        }
    }
}
