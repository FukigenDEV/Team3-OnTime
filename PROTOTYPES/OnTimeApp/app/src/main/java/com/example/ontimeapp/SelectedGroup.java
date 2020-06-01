package com.example.ontimeapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

//public class SelectedGroup extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_selectedgroup);
//
//        Intent selectedgroup = getIntent();
//        String groupName = selectedgroup.getStringExtra("groupName");
//        String groupCode = selectedgroup.getStringExtra("groupCode");
//
//        TextView setGroupName = findViewById(R.id.groupname);
//        TextView setGroupCode = findViewById(R.id.groupcode);
//
//        setGroupName.setText(groupName);
//        setGroupCode.setText(groupCode + " (others can use this code to join your group)");
//
//    }
//}

public class SelectedGroup extends Fragment implements View.OnClickListener {

    private static final String TAG = "SelectedGroup";
    String groupName, groupCode;
    Button addAlarm;

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
        setGroupCode.setText(groupCode + " (others can use this code to join your group)");

        addAlarm = rootView.findViewById(R.id.addAlarmBtn);
        addAlarm.setOnClickListener(this);


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
            fragmentManagement.setMainFragment(addAlarm, transaction, addNewAlarm, "Add new Alarm");
        }
    }
}
