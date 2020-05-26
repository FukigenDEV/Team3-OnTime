package com.example.ontimeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;

public class MainMenu extends AppCompatActivity {

    private static final String TAG = "MainMenu";
    String androidId, userName, userToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        Button joinGroup = findViewById(R.id.JoinGroupBtn);
        Button createGroup = findViewById(R.id.CreateGroupBtn);
        Button setTasks = findViewById(R.id.SetTasksBtn);

        Intent intent = getIntent();
        androidId = intent.getStringExtra("androidId");
        userName = intent.getStringExtra("userName");
        userToken = intent.getStringExtra("userToken");
        Log.d("MESSAGECHECKMAINMENU", androidId + userName + userToken);

        setTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainMenu.this, SetTasksActivity.class);
                intent2.putExtra("androidId", androidId);
                intent2.putExtra("userName", userName);
                intent2.putExtra("userToken", userToken);
                Log.d("MESSAGECHECKBUTTON", androidId + userName + userToken);
                startActivity(intent2);
            }

        });

        joinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainMenu.this, JoinGroup.class);
                intent2.putExtra("androidId", androidId);
                intent2.putExtra("userName", userName);
                intent2.putExtra("userToken", userToken);
                Log.d("MESSAGECHECKBUTTON", androidId + userName + userToken);
                startActivity(intent2);
            }
        });

        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainMenu.this, CreateGroup.class);
                intent2.putExtra("androidId", androidId);
                intent2.putExtra("userName", userName);
                intent2.putExtra("userToken", userToken);
                Log.d("MESSAGECHECKBUTTON", androidId + userName + userToken);
                startActivity(intent2);
            }
        });
    }
}


