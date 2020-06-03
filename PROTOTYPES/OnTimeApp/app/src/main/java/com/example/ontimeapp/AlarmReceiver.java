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
        Toast.makeText(context, "ALARM....", Toast.LENGTH_LONG).show();
        Log.d("ALARMRECEIVER", "ALARM CALLED");

        Calendar calendar = Calendar.getInstance();
        FirebaseApp.initializeApp(context);
        FirebaseDatabase.getInstance().getReference().child("Testing_alarm").child(calendar.getTime().toString()).setValue("1");

        Intent i = new Intent();
        i.setClassName(context.getPackageName(), AlarmActive.class.getName());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
