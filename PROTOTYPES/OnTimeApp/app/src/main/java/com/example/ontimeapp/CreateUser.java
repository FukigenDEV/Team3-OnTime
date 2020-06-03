package com.example.ontimeapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class CreateUser extends Fragment implements View.OnClickListener {

    private EditText etName;
    private EditText etPhone;
    private Button confirmName;

    String name, phone, androidId;
    private static final String TAG = "CreateUser";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Bundle args = getArguments();
        androidId = args.getString("androidId");
    }

    public CreateUser() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_createuser, container, false);

        etName = rootView.findViewById(R.id.etName);
        etPhone = rootView.findViewById(R.id.etPhone);
        confirmName = rootView.findViewById(R.id.confirmName);

        confirmName.setOnClickListener(this);
        return rootView;
    }

    public void onClick(View v) {
        final FragmentManagement fragmentManagement = new FragmentManagement();
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();

        if(v.getId() == R.id.confirmName) {
            name = etName.getText().toString();
            phone = etPhone.getText().toString();
            if (name.isEmpty() || phone.isEmpty()){
                Toast.makeText(getActivity(), "Please fill in the fields!", Toast.LENGTH_SHORT).show();
            }else {
                FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()){
                                Log.w(TAG, "getInstanceId failed", task.getException());
                                return;
                            }
                            String token = task.getResult().getToken();
                            Log.d(TAG, token);
                            Toast.makeText(getActivity(), token, Toast.LENGTH_SHORT).show();
                            User user = new User(name, token, phone);
                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users").child(androidId);
                            databaseReference.setValue(user);
                            Toast.makeText(getActivity(), "Database Updated", Toast.LENGTH_SHORT).show();

                            Fragment mainMenu = new MainMenu();
                            Bundle bundle = new Bundle();
                            bundle.putString("androidId", androidId);
                            Log.d(TAG, "androidId: "+androidId);
                            bundle.putString("userName", name);
                            bundle.putString("userToken", token);
                            mainMenu.setArguments(bundle);

                            fragmentManagement.setMainFragment((TextView)getActivity().findViewById(R.id.title_activity), transaction, mainMenu, "TEAMS");
                        }
                    });
            }
        }
    }

}
