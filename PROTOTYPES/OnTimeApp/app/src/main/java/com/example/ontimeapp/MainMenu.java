package com.example.ontimeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainMenu extends AppCompatActivity {

    private static final String TAG = "MainMenu";
    String androidId, userName, userToken;
    ArrayList<String> groupnames = new ArrayList<>();
    ArrayList<String> groupcodes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        Button joinGroup = findViewById(R.id.JoinGroupBtn);
        Button createGroup = findViewById(R.id.CreateGroupBtn);

        Intent intent = getIntent();
        androidId = intent.getStringExtra("androidId");
        userName = intent.getStringExtra("userName");
        userToken = intent.getStringExtra("userToken");
        Log.d("MESSAGECHECKMAINMENU", androidId + userName + userToken);

        FirebaseDatabase.getInstance().getReference().child("Groups")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        groupnames.clear();
                        groupcodes.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            if (snapshot.child("Members").hasChild(androidId)){
                                String groupid = snapshot.getKey();
                                String groupname = snapshot.child("groupName").getValue().toString();
                                System.out.println(groupid);
                                groupcodes.add(groupid);
                                groupnames.add(groupname);
                            }
                        }
                        GroupsAdapter groupsAdapter = new GroupsAdapter(MainMenu.this, groupnames, groupcodes);
                        recyclerView.setAdapter(groupsAdapter);
                        groupsAdapter.notifyDataSetChanged();

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(MainMenu.this, databaseError.getCode(), Toast.LENGTH_SHORT);
                    }
                });

        joinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainMenu.this, JoinGroup.class);
                intent2.putExtra("androidId", androidId);
                intent2.putExtra("userName", userName);
                intent2.putExtra("userToken", userToken);
                Log.d("MESSAGECHECKBUTTON", androidId + userName + userToken);
                startActivity(intent2);
            }
        });

        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainMenu.this, CreateGroup.class);
                intent2.putExtra("androidId", androidId);
                intent2.putExtra("userName", userName);
                intent2.putExtra("userToken", userToken);
                Log.d("MESSAGECHECKBUTTON", androidId + userName + userToken);
                startActivity(intent2);
            }
        });
    }
}


