package com.example.ontimeapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;


public class AlarmActive extends AppCompatActivity {

    Button dismissAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmactive);
        final String groupId = getIntent().getStringExtra("groupId");
        final String alarmName = getIntent().getStringExtra("alarmName");

        @SuppressLint("HardwareIds") final String androidId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        dismissAlarm = findViewById(R.id.dismissAlarm);
        dismissAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("Groups").child(groupId).child("Alarms").child(alarmName).child("MemberState").child(androidId).setValue("AWAKE");

                Intent intent = new Intent(getApplicationContext(), MainUI.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

