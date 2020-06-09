package com.example.ontimeapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserChecklist extends Fragment {
    @SuppressLint("HardwareIds")
    String androidId, groupId;
    ArrayList<String> userTasks = new ArrayList<>();
    ArrayList<String> taskStates = new ArrayList<>();

    @SuppressLint("HardwareIds")
    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        Bundle bundle = getArguments();
        groupId = bundle.getString("groupId");
        androidId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public UserChecklist(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.activity_taskchecklist, container, false);

        final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userTasks.clear();
                taskStates.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Users").child(androidId).child("Tasks").getChildren()){
                    userTasks.add(dataSnapshot1.getKey());
                    taskStates.add(dataSnapshot1.getValue().toString());
                }
                UserChecklistAdapter userChecklistAdapter = new UserChecklistAdapter(getContext(), userTasks, taskStates, groupId);
                recyclerView.setAdapter(userChecklistAdapter);
                userChecklistAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}
