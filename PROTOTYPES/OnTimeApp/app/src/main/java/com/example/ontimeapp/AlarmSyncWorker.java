package com.example.ontimeapp;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.ALARM_SERVICE;

public class AlarmSyncWorker extends Worker {

    private int currentYear, currentMonth, currentDay, currentHour, currentMinute;
    private String currentDate, currentTime;
    private String alarmName, alarmDate, alarmTime;
    private int alarmHour, alarmMinute;
    private Date currentDateFull, alarmDateFull;
    Context myContext;
    AlarmManager alarmManager;
    private PendingIntent pendingIntent;



    public AlarmSyncWorker(
        @NonNull Context context,
        @NonNull WorkerParameters params) {
        super(context, params);
        myContext = context;
    }

    @Override
    public Result doWork(){

        Log.d("ALARMSYNCWORKER", "DOING BACKGROUND WORK");
        //final String androidId = getInputData().getString("androidId");
        @SuppressLint("HardwareIds") final String androidId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        alarmManager = (AlarmManager) myContext.getSystemService(ALARM_SERVICE);

        final Calendar calendar = Calendar.getInstance();

        final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH);
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        currentMinute = calendar.get(Calendar.MINUTE);

        currentDate = currentDay + "-" + (currentMonth + 1) + "-" + currentYear;
        currentTime = currentHour + ":" + currentMinute;

        try {
            currentDateFull = format.parse(currentDate + " " + currentTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        FirebaseDatabase.getInstance().getReference().child("Testing").child(currentDate + " " + currentTime).setValue("1");

        FirebaseDatabase.getInstance().getReference().child("Groups")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                            if (dataSnapshot1.child("Members").hasChild(androidId)){
                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.child("Alarms").getChildren()){
                                    alarmName = dataSnapshot2.child("name").getValue().toString();
                                    alarmDate = dataSnapshot2.child("date").getValue().toString();
                                    alarmTime = dataSnapshot2.child("time").getValue().toString();
                                    try {
                                        alarmDateFull = format.parse(alarmDate + " " + alarmTime);
                                        long difference = alarmDateFull.getTime() - currentDateFull.getTime();
                                        float differenceMinutes = difference / (60 * 1000);
                                        if(differenceMinutes > 0 && differenceMinutes < 20){
                                            Log.d("ALARMSYNCMANAGER", "ALARM CALLED");
                                            Log.d("ALARMSYNCMANAGER", "Time for alarm: " + alarmDateFull);
                                            Intent intent = new Intent(myContext, AlarmReceiver.class);
                                            pendingIntent = PendingIntent.getBroadcast(myContext, 0, intent, 0);
                                            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmDateFull.getTime(), pendingIntent);
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("AlarmSyncWorker", "Read Failed");
                    }
                });
        return Result.success();

    }
}
