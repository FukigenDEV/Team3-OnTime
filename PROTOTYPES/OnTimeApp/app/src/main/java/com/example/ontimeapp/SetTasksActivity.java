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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.w3c.dom.Text;

public class SetTasksActivity extends Fragment implements View.OnClickListener {

    private EditText task;
    private Button confirm;

    String tasktext, userName, userToken, androidId, userPhone;

    private static final String TAG = "SetTasks";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Bundle args = getArguments();
        androidId = args.getString("androidId");
        userName = args.getString("userName");
        userToken = args.getString("userToken");
        userPhone = args.getString("userPhone");
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
                                final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users").child(androidId).child("Tasks").child(tasktext);
                                databaseReference.setValue("Not finished");

                                FirebaseDatabase.getInstance().getReference().child("Groups").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                            if(snapshot.child("Members").hasChild(androidId)){
                                                DatabaseReference databaseReference2 = firebaseDatabase.getReference().child("Groups").child(snapshot.getKey()).child("Members").child(androidId).child("Tasks").child(tasktext);
                                                databaseReference2.setValue("Not finished");
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(getActivity(), databaseError.getCode(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                                Toast.makeText(getContext(), "Task successfully added!", Toast.LENGTH_SHORT).show();

                                fragmentManagement.replaceMainFragment((TextView)getActivity().findViewById(R.id.title_activity), transaction, getFragmentManager().findFragmentByTag("TEAMS"), "TEAMS");
                            }
                        });
            }
        }
    }
}
