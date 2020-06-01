package com.example.ontimeapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

public class CallFunc extends Activity {
    private Button callbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callperson);
        callbtn = (Button) findViewById(R.id.callbtn);
        callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mbuilder = new AlertDialog.Builder(CallFunc.this);
                mbuilder.setTitle("Meow")
                        .setMessage("Do you give permission")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:0377778888"));

                                if (ActivityCompat.checkSelfPermission(CallFunc.this,
                                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                startActivity(callIntent);
                            }
                        });
                mbuilder.setNegativeButton("Oops no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = mbuilder.create();
                alertDialog.show();
            }
        });


    }

    public void permission(View view) {
        //asking for permission
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public  void onPermissionGranted() {
                Toast.makeText(CallFunc.this,  "Permission granted", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(CallFunc.this, "Permission not given", Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(CallFunc.this)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.CALL_PHONE)
                .check();
    }
}
