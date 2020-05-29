package com.example.ontimeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class MainMenu extends Fragment implements View.OnClickListener {

    private static final String TAG = "MainMenu";
    String androidId, userName, userToken;
    Button joinGroup, createGroup, setTasks;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Bundle args = getArguments();
        androidId = args.getString("androidId");
        userName = args.getString("userName");
        userToken = args.getString("userToken");
    }

    public MainMenu() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_mainmenu, container, false);

        joinGroup = rootView.findViewById(R.id.JoinGroupBtn);
        createGroup = rootView.findViewById(R.id.CreateGroupBtn);
        setTasks = rootView.findViewById(R.id.SetTasksBtn);

        Log.d("MESSAGECHECKMAINMENU", androidId + userName + userToken);

        setTasks.setOnClickListener(this);
        createGroup.setOnClickListener(this);
        joinGroup.setOnClickListener(this);

        // Inflate the layout for this fragment
        return rootView;
    }

    public void onClick(View v) {
        final FragmentManagement fragmentManagement = new FragmentManagement();
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();

        int viewId = v.getId();

        Bundle bundle = new Bundle();
        bundle.putString("androidId", androidId);
        bundle.putString("userName", userName);
        bundle.putString("userToken", userToken);

        if (viewId == R.id.SetTasksBtn) {
            Fragment setTaskActivity = new SetTasksActivity();
            setTaskActivity.setArguments(bundle);
            fragmentManagement.setMainFragment(setTasks, transaction, setTaskActivity, "Set Tasks");
        } else if (viewId == R.id.JoinGroupBtn) {
            Fragment joinGroup = new JoinGroup();
            joinGroup.setArguments(bundle);
            fragmentManagement.setMainFragment(setTasks, transaction, joinGroup, "Join Group");
        } else if(viewId == R.id.CreateGroupBtn) {
            Fragment createGroup = new CreateGroup();
            createGroup.setArguments(bundle);
            fragmentManagement.setMainFragment(setTasks, transaction, createGroup, "Create Group");
        }
    }
}