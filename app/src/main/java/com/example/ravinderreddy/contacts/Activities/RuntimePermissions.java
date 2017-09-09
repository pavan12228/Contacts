package com.example.ravinderreddy.contacts.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class RuntimePermissions {
    Context context;
    boolean mAccess_fine_location,mReadContacts;

    public RuntimePermissions(Context context) {
        this.context = context;
    }

    public boolean getStatusPermission() {
        return mAccess_fine_location;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        if (grantResults.length > 1 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            mReadContacts = true;
        } else {
            mReadContacts = false;
        }


    }



    public void checkExplainRequestPermission(final String mPermission) {
        if (ContextCompat.checkSelfPermission(context, mPermission) == PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(context, "Permission granted!", Toast.LENGTH_SHORT).show();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, mPermission)) {
            AlertDialog.Builder explantDilaog = new AlertDialog.Builder(context);
            explantDilaog.setMessage("Please allow the  contacts permission to access mobile Numbers");
            explantDilaog.setTitle("Please click ok to continue");
            explantDilaog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{mPermission}, 151);
                   // Toast.makeText(context, "You understood the permission!", Toast.LENGTH_SHORT).show();
                }
            });
            explantDilaog.setNegativeButton("canecl", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            explantDilaog.show();


        } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_CONTACTS}, 151);
            //Toast.makeText(context, "No permission", Toast.LENGTH_SHORT).show();
        }
    }



}