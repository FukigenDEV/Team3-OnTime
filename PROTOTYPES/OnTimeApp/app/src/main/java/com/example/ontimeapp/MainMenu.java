package com.example.ontimeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainMenu extends Fragment {

    private static final String TAG = "MainMenu";
    String androidId, userName, userToken;
    TextView title;
    ArrayList<String> groupnames = new ArrayList<>();
    ArrayList<String> groupcodes = new ArrayList<>();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        title = getActivity().findViewById(R.id.title_activity);
        title.setText("TEAMS");

        Bundle args = getArguments();
        androidId = args.getString("androidId");
        userName = args.getString("userName");
        userToken = args.getString("userToken");

        getActivity().findViewById(R.id.global_titlebar).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.title_activity).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.ic_nav).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.subNav).setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        title.setText("TEAMS");
    }

    public MainMenu() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_mainmenu, container, false);

        final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

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
                                groupcodes.add(groupid);
                                groupnames.add(groupname);
                            }
                        }
                        GroupsAdapter groupsAdapter = new GroupsAdapter(getContext(), groupnames, groupcodes);
                        recyclerView.setAdapter(groupsAdapter);
                        groupsAdapter.notifyDataSetChanged();

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), databaseError.getCode(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Inflate the layout for this fragment
        return rootView;
    }

}