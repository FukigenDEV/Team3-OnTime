package com.example.ontimeapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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

        return rootView;
    }

    @Override
    public void onClick(View v) {

    }
}
