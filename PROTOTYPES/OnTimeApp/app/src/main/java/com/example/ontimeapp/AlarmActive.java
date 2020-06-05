package com.example.ontimeapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import androidx.appcompat.app.AppCompatActivity;


public class AlarmActive extends AppCompatActivity {

    Button dismissAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmactive);
        final String groupId = getIntent().getStringExtra("groupId");
        final String alarmName = getIntent().getStringExtra("alarmName");

        dismissAlarm = findViewById(R.id.dismissAlarm);
        dismissAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainUI.class);
                intent.putExtra("TARGETSCREEN", "GROUPALARMSTATUS");
                intent.putExtra("groupId", groupId);
                intent.putExtra("alarmName", alarmName);
                startActivity(intent);
                finish();
            }
        });
    }
}

