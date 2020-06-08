package com.example.ontimeapp;

import android.content.Context;
import android.os.Bundle;
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
    String groupName, groupCode;
    Button addAlarm;
    ArrayList<String> AlarmName = new ArrayList<>();
    ArrayList<String> AlarmDate = new ArrayList<>();
    ArrayList<String> AlarmTime = new ArrayList<>();

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        Bundle args = getArguments();
        groupName = args.getString("groupName");
        groupCode = args.getString("groupCode");
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

        addAlarm = rootView.findViewById(R.id.addAlarmBtn);
        addAlarm.setOnClickListener(this);

        final LinearLayout progressbarHolder = (LinearLayout) rootView.findViewById(R.id.progressbarHolder);

        FirebaseDatabase.getInstance().getReference().child("Groups")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        AlarmName.clear();
                        AlarmDate.clear();
                        AlarmTime.clear();
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
                                        View progressbarItem = inflater.inflate(R.layout.task_progress_item, progressbarHolder, false);
                                        TextView progressbarName = progressbarItem.findViewById(R.id.userTask);

                                        String name = snapshot2.child("name").getValue().toString();
                                        final String deviceToken = snapshot2.child("deviceToken").getValue().toString();

//                                        FirebaseDatabase.getInstance().getReference().child("Users")
//                                                .addListenerForSingleValueEvent(new ValueEventListener() {
//
//                                                    @Override
//                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                                                            String token = snapshot.child("deviceToken").getValue().toString();
//                                                            if(token.equals(deviceToken)) {
////                                                                phone.add(snapshot.child("phone").getValue().toString());
//                                                                Log.d("Phone", ""+snapshot.child("phone").getValue().toString());
//                                                            }
//                                                        }
//                                                    }
//
//                                                    @Override
//                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                                                        Toast.makeText(getContext(), databaseError.getCode(), Toast.LENGTH_SHORT).show();
//                                                    }
//
//                                                });

                                        progressbarName.setText(name);
                                        progressbarHolder.addView(progressbarItem);
                                    }
                                }
                            }
                        }
                        AlarmsAdapter alarmsAdapter = new AlarmsAdapter(getContext(), AlarmName, AlarmDate, AlarmTime, groupCode);
                        recyclerView.setAdapter(alarmsAdapter);
                        alarmsAdapter.notifyDataSetChanged();
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
