package com.example.ontimeapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class EntryScreen extends AppCompatActivity {

    private static final String TAG = "EntryScreen";
    private Context Context;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entryscreen);

        final String androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(androidId).exists()){
                    Toast.makeText(EntryScreen.this, "Android ID exists in database", Toast.LENGTH_SHORT).show();
                    User user = dataSnapshot.child(androidId).getValue(User.class);

                    String userName = user.getName();
                    String userToken = user.getDeviceToken();

                    Intent intent2 = new Intent(EntryScreen.this, MainMenu.class);
                    intent2.putExtra("androidId", androidId);
                    intent2.putExtra("userName", userName);
                    intent2.putExtra("userToken", userToken);
                    startActivity(intent2);
                    finish();
                }else{
                    Toast.makeText(EntryScreen.this, androidId + "Android ID does not exist in database", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EntryScreen.this, CreateUser.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EntryScreen.this, databaseError.getCode(), Toast.LENGTH_SHORT);
            }
        });

        Constraints constraints = new Constraints.Builder()
                .build();

        PeriodicWorkRequest syncAlarmRequest =
                new PeriodicWorkRequest.Builder(AlarmSyncWorker.class, 15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(Context).enqueue(syncAlarmRequest);
    }
}
