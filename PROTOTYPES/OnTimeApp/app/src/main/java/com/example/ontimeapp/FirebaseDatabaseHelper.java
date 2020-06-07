package com.example.ontimeapp;

import androidx.annotation.NonNull;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceTaken;
    private ArrayList<Taken> taken = new ArrayList<Taken>();
    String tasktext,  androidId;

   public interface DataStatus{


       void DataIsLoaded(List<Taken> taken, List<String> keys);

       void DataIsInserted();
       void DataIsUpdated();
       void DataIsDeleted();
   }
    public FirebaseDatabaseHelper() {
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceTaken = mDatabase.getReference().child("Users").child(androidId).child("Tasks");
    }

    public void readTaken(final DataStatus dataStatus){
        mReferenceTaken.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               taken.clear();
               List<String> keys = new ArrayList<>();
               for(DataSnapshot keyNode : dataSnapshot.getChildren()) {
                   keys.add(keyNode.getKey());
                   Taken taak = keyNode.getValue(Taken.class);
                   taken.add((Taken) taak);
               }
               dataStatus.DataIsLoaded(taken, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
