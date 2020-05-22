package com.example.ontimeapp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EntryScreen extends AppCompatActivity {

    private static final String TAG = "EntryScreen";

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
                    startActivity(new Intent(EntryScreen.this, MainMenu.class));
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
    }
}
