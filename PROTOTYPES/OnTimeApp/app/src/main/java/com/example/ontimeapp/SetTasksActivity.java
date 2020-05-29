package com.example.ontimeapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class SetTasksActivity extends Fragment implements View.OnClickListener {

    private EditText task;
    private Button confirm;

    String tasktext, userName, userToken, androidId;

    private static final String TAG = "SetTasks";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Bundle args = getArguments();
        androidId = args.getString("androidId");
        userName = args.getString("userName");
        userToken = args.getString("userToken");
    }

    public SetTasksActivity() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_set_tasks, container, false);

        task = rootView.findViewById(R.id.etName);
        confirm = rootView.findViewById(R.id.confirmbtn);

        confirm.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        final FragmentManagement fragmentManagement = new FragmentManagement();
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();

        int viewId = v.getId();

        if(viewId == R.id.confirmbtn) {
            tasktext = task.getText().toString();
            if (tasktext.isEmpty()){
                Toast.makeText(getContext(), "Please enter a name!", Toast.LENGTH_SHORT).show();
            }else{
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
                                Toast.makeText(getContext(), token, Toast.LENGTH_SHORT).show();
                                UserAdapter userAdapter = new UserAdapter(tasktext, token);
                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users").child(androidId).child("Tasks").child(tasktext);
                                databaseReference.setValue("Not finished");
                                //databaseReference.setValue(userAdapter);
                                Toast.makeText(getContext(), "Database Updated", Toast.LENGTH_SHORT).show();

                                Fragment mainMenu = new MainMenu();
                                Bundle bundle = new Bundle();
                                bundle.putString("androidId", androidId);
                                bundle.putString("userName", userName);
                                bundle.putString("userToken", userToken);
                                mainMenu.setArguments(bundle);
                                fragmentManagement.setMainFragment(confirm, transaction, mainMenu, "Set Task");
//                                Intent intent = new Intent(SetTasksActivity.this, MainMenu.class);
//                                intent.putExtra("androidId", androidId);
//                                intent.putExtra("userName", tasktext);
//                                intent.putExtra("userToken", token);
//                                startActivity(intent);
                            }
                        });
            }
        }
    }
}
