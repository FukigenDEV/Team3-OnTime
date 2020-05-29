package com.example.ontimeapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMSGService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMSGService";

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