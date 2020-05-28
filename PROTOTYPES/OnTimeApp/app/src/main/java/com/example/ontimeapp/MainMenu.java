package com.example.ontimeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;

public class MainMenu extends Fragment {

    private static final String TAG = "MainMenu";
    String androidId, userName, userToken;

    public MainMenu() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_mainmenu, container, false);

        final FragmentManagement fragmentManagement = new FragmentManagement();
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();

        View mainUI = inflater.inflate(R.layout.activity_main, null, false);
        final TextView activityTitle = mainUI.findViewById(R.id.title_activity);


        Button joinGroup = rootView.findViewById(R.id.JoinGroupBtn);
        Button createGroup = rootView.findViewById(R.id.CreateGroupBtn);
        Button setTasks = rootView.findViewById(R.id.SetTasksBtn);

        Bundle args = getArguments();
        androidId = args.getString("androidId");
        userName = args.getString("userName");
        userToken = args.getString("userToken");
//        Intent intent = getIntent();
//        androidId = intent.getStringExtra("androidId");
//        userName = intent.getStringExtra("userName");
//        userToken = intent.getStringExtra("userToken");
        Log.d("MESSAGECHECKMAINMENU", androidId + userName + userToken);

        setTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Fragment setTaskActivity = new SetTasksActivity();
//                Bundle bundle = new Bundle();
//                bundle.putString("androidId", androidId);
//                bundle.putString("userName", userName);
//                bundle.putString("userToken", userToken);
//                setTaskActivity.setArguments(bundle);
//
//                fragmentManagement.setMainFragment(activityTitle, transaction, setTaskActivity, "Main Menu");

//                Intent intent2 = new Intent(MainMenu.this, SetTasksActivity.class);
//                intent2.putExtra("androidId", androidId);
//                intent2.putExtra("userName", userName);
//                intent2.putExtra("userToken", userToken);
                Log.d("MESSAGECHECKBUTTON", androidId + userName + userToken);
//                startActivity(intent2);
            }

        });

        joinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent2 = new Intent(MainMenu.this, JoinGroup.class);
//                intent2.putExtra("androidId", androidId);
//                intent2.putExtra("userName", userName);
//                intent2.putExtra("userToken", userToken);
                Log.d("MESSAGECHECKBUTTON", androidId + userName + userToken);
//                startActivity(intent2);
            }
        });

        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent2 = new Intent(MainMenu.this, CreateGroup.class);
//                intent2.putExtra("androidId", androidId);
//                intent2.putExtra("userName", userName);
//                intent2.putExtra("userToken", userToken);
                Log.d("MESSAGECHECKBUTTON", androidId + userName + userToken);
//                startActivity(intent2);
            }
        });

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_mainmenu, container, false);
    }
}


