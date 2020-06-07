package com.example.ontimeapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        String groupId = intent.getStringExtra("groupId");
        String alarmName = intent.getStringExtra("alarmName");
        Toast.makeText(context, "ALARM....", Toast.LENGTH_LONG).show();
        Log.d("ALARMRECEIVER", "ALARM CALLED");

        Intent i = new Intent();
        i.putExtra("groupId", groupId);
        i.putExtra("alarmName", alarmName);
        i.setClassName(context.getPackageName(), AlarmActive.class.getName());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(i);
    }
}
