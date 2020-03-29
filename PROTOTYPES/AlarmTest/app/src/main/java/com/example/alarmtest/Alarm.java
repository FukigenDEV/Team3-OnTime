package com.example.alarmtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;

public class Alarm extends BroadcastReceiver {

    Vibrator v;

    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "ALARM....", Toast.LENGTH_LONG).show();
        v = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        v.vibrate(10000);
        Intent i = new Intent();
        i.setClassName(context.getPackageName(), AlarmActive.class.getName());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
