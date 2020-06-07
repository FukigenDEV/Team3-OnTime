package com.example.ontimeapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GroupAlarmStatus extends Fragment implements View.OnClickListener {
    String groupId, alarmName;
    Button goToTasks;
    TextView title;

    ArrayList<String> userNames = new ArrayList<>();
    ArrayList<String> userStatus = new ArrayList<>();
    ArrayList<String> userNumbers = new ArrayList<>();

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        Bundle args = getArguments();
        groupId = args.getString("groupId");
        alarmName = args.getString("alarmName");
    }

    public GroupAlarmStatus(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.activity_groupalarmstatus, container, false);

        final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        goToTasks = rootView.findViewById(R.id.goToTasksBtn);
        goToTasks.setOnClickListener(this);

        FirebaseDatabase.getInstance().getReference()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userNames.clear();
                        userStatus.clear();
                        userNumbers.clear();
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Groups").child(groupId).child("Alarms").child(alarmName).child("MemberState").getChildren()){
                            String androidId = dataSnapshot1.getKey();
                            String userState = dataSnapshot1.getValue().toString();
                            String userName = dataSnapshot.child("Users").child(androidId).child("name").getValue().toString();
                            String userNumber = dataSnapshot.child("Users").child(androidId).child("phone").getValue().toString();
                            userNames.add(userName);
                            userStatus.add(userState);
                            userNumbers.add(userNumber);
                        }
                        GroupAlarmStatusAdapter groupAlarmStatusAdapter = new GroupAlarmStatusAdapter(getContext(),userNames, userStatus, userNumbers);
                        recyclerView.setAdapter(groupAlarmStatusAdapter);
                        groupAlarmStatusAdapter.notifyDataSetChanged();
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
        if(viewId == R.id.goToTasksBtn){
            fragmentManagement.replaceMainFragment( (TextView) getActivity().findViewById(R.id.title_activity), transaction, new UserChecklist(), "YOUR CHECKLIST");
        }
    }
}
