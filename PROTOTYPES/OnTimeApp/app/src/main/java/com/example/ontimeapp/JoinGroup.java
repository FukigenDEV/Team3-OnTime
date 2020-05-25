package com.example.ontimeapp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JoinGroup extends AppCompatActivity {

    private EditText etJoinGroupID;
    private Button joinGroupbtn;
    String groupid, androidId, userName, userToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joingroup);

        etJoinGroupID = (EditText)findViewById(R.id.etJoinGroupId);
        joinGroupbtn = (Button)findViewById(R.id.joinGroupbtn);

        Intent intent = getIntent();
        androidId = intent.getStringExtra("androidId");
        userName = intent.getStringExtra("userName");
        userToken = intent.getStringExtra("userToken");
        Log.d("MESSAGECHECKJOINGROUP", androidId + userName + userToken);

        joinGroupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupid = etJoinGroupID.getText().toString();

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                final DatabaseReference databaseReference1 = firebaseDatabase.getReference().child("Groups");

                databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(groupid).exists()){
                            Log.d("JOINGROUP", "Group Exists, joining");
                            if (dataSnapshot.child(groupid).child("Members").child(androidId).exists()){
                                Toast.makeText(JoinGroup.this, "You are already a member of this group", Toast.LENGTH_SHORT).show();
                            }else{
                                AddMemberAdapter addMemberAdapter = new AddMemberAdapter(userName, userToken);
                                databaseReference1.child(groupid).child("Members").child(androidId).setValue(addMemberAdapter);
                                Toast.makeText(JoinGroup.this, "Successfully joined the group!", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(JoinGroup.this, groupid + " - This group does not exist. Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(JoinGroup.this, databaseError.getCode(), Toast.LENGTH_SHORT);
                    }
                });
            }
        });
    }
}
