package com.example.ontimeapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SelectedGroup extends Fragment implements View.OnClickListener {

    private static final String TAG = "SelectedGroup";
    String groupName, groupCode, androidId;
    Button addAlarm;
    ArrayList<String> AlarmName = new ArrayList<>();
    ArrayList<String> AlarmDate = new ArrayList<>();
    ArrayList<String> AlarmTime = new ArrayList<>();

    ArrayList<String> progressPhone = new ArrayList<>();
    ArrayList<String> progressName = new ArrayList<>();

    @SuppressLint("HardwareIds")
    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        Bundle args = getArguments();
        groupName = args.getString("groupName");
        groupCode = args.getString("groupCode");
        androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public SelectedGroup(){
        //Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.activity_selectedgroup, container, false);
        TextView setGroupName = rootView.findViewById(R.id.groupname);
        TextView setGroupCode = rootView.findViewById(R.id.groupcode);
        setGroupName.setText(groupName);
        setGroupCode.setText(groupCode);

        final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        final RecyclerView recyclerView1 = (RecyclerView) rootView.findViewById(R.id.recyclerview2);
        recyclerView1.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext());
        recyclerView1.setLayoutManager(linearLayoutManager1);

        addAlarm = rootView.findViewById(R.id.addAlarmBtn);
        addAlarm.setOnClickListener(this);


        FirebaseDatabase.getInstance().getReference().child("Groups")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        AlarmName.clear();
                        AlarmDate.clear();
                        AlarmTime.clear();
                        progressPhone.clear();
                        progressName.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            if (groupCode.equals(snapshot.getKey())){
                                if (snapshot.hasChild("Alarms")){
                                    for (DataSnapshot snapshot2 : snapshot.child("Alarms").getChildren()){
                                        String name = snapshot2.child("name").getValue().toString();
                                        String date = snapshot2.child("date").getValue().toString();
                                        String time = snapshot2.child("time").getValue().toString();
                                        AlarmName.add(name);
                                        AlarmDate.add(date);
                                        AlarmTime.add(time);
                                    }
                                }

                                if (snapshot.hasChild("Members")){
                                    for (DataSnapshot snapshot2 : snapshot.child("Members").getChildren()){
                                        String name = snapshot2.child("name").getValue().toString();
                                        String phone = snapshot2.child("phone").getValue().toString();
                                        progressName.add(name);
                                        progressPhone.add(phone);
                                    }
                                }
                            }
                        }
                        AlarmsAdapter alarmsAdapter = new AlarmsAdapter(getContext(), AlarmName, AlarmDate, AlarmTime, groupCode);
                        recyclerView.setAdapter(alarmsAdapter);
                        alarmsAdapter.notifyDataSetChanged();

                        ProgressBarAdapter progressBarAdapter = new ProgressBarAdapter(getContext(), progressName, progressPhone, groupCode, androidId);
                        recyclerView1.setAdapter(progressBarAdapter);
                        progressBarAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), databaseError.getCode(), Toast.LENGTH_SHORT).show();
                    }
                });

        return rootView;
    }

    @Override
    public void onClick(View v) {
        final FragmentManagement fragmentManagement = new FragmentManagement();
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();

        int viewId = v.getId();

        if (viewId == R.id.addAlarmBtn){
            Bundle bundle = new Bundle();
            bundle.putString("groupName", groupName);
            bundle.putString("groupCode", groupCode);
            Fragment addNewAlarm = new CreateAlarm();
            addNewAlarm.setArguments(bundle);
            fragmentManagement.replaceMainFragment(addAlarm, transaction, addNewAlarm, "Add new Alarm");
        }
    }
}
