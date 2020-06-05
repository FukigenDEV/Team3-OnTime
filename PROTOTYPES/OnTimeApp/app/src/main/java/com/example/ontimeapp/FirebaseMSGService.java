package com.example.ontimeapp;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMSGService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMSGService";

    @Override
    public void onNewToken(final String newToken){
        super.onNewToken(newToken);

        @SuppressLint("HardwareIds") final String androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(androidId).exists()){
                    databaseReference.child("Users").child(androidId).child("deviceToken").setValue(newToken);
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Groups").getChildren()){
                        if(dataSnapshot1.child("Members").hasChild(androidId)){
                            String groupcode = dataSnapshot1.getKey();
                            databaseReference.child("Groups").child(groupcode).child("Members").child(androidId).child("deviceToken").setValue(newToken);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getCode(), Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            AddNotification(remoteMessage.getData().get("body").toString(), remoteMessage.getData().get("title").toString());
        }

    }

    public void AddNotification(String body, String title) {
        int requestID = (int) System.currentTimeMillis();
        Intent intent = new Intent(this, MainMenu.class);
        String channelId = getString(R.string.notification_channel_id);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri alarmSound = RingtoneManager.getDefaultUri((RingtoneManager.TYPE_NOTIFICATION));

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setVibrate(new long[] { 0, 1000, 1000, 1000, 1000 })
                .setSound(alarmSound);;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "default channel",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[] {0,1000,1000,1000,1000});
            channel.setSound(alarmSound,null);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(requestID, notificationBuilder.build());
    }

}
