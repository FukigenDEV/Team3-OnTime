package com.example.ontimeapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JoinGroup extends Fragment implements View.OnClickListener {

    private static final String TAG = "JoinGroup";

    private EditText etJoinGroupID;
    private Button joinGroupbtn;
    String groupid, androidId, userName, userToken, userPhone;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Bundle args = getArguments();
        androidId = args.getString("androidId");
        userName = args.getString("userName");
        userToken = args.getString("userToken");
        userPhone = args.getString("userPhone");
    }

    public JoinGroup() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_joingroup, container, false);

        etJoinGroupID = rootView.findViewById(R.id.etJoinGroupId);
        joinGroupbtn = rootView.findViewById(R.id.joinGroupbtn);

        Log.d("MESSAGECHECKJOINGROUP", androidId + userName + userToken);

        joinGroupbtn.setOnClickListener(this);

        return rootView;
    }

    public void onClick(View v) {
        int viewId = v.getId();

        if(viewId == R.id.joinGroupbtn) {
            groupid = etJoinGroupID.getText().toString();

            Log.d(TAG, "test");

            final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            final DatabaseReference databaseReference1 = firebaseDatabase.getReference();

            databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("Groups").child(groupid).exists()){
                        Log.d(TAG, "Group Exists, joining");
                        if (dataSnapshot.child(groupid).child("Members").child(androidId).exists()){
                            Toast.makeText(getActivity(), "You are already a member of this group", Toast.LENGTH_SHORT).show();
                        }else{
                            User user = new User(userName, userToken, userPhone);
                            databaseReference1.child("Groups").child(groupid).child("Members").child(androidId).setValue(user);
                            Toast.makeText(getActivity(), "Successfully joined the group!", Toast.LENGTH_SHORT).show();
                            for(DataSnapshot snapshot : dataSnapshot.child("Users").child(androidId).child("Tasks").getChildren()){
                                String userTask = snapshot.getKey();
                                DatabaseReference databaseReference2 = firebaseDatabase.getReference().child("Groups").child(groupid).child("Members").child(androidId).child("Tasks").child(userTask);
                                databaseReference2.setValue("Not finished");
                            }
                            for(DataSnapshot snapshot : dataSnapshot.child("Groups").child(groupid).child("Alarms").getChildren()){
                                String alarmName = snapshot.getKey();
                                DatabaseReference databaseReference2 = firebaseDatabase.getReference().child("Groups").child(groupid).child("Alarms").child(alarmName).child("MemberState").child(androidId);
                                databaseReference2.setValue("NOT AWAKE!");
                            }
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            FragmentManagement fragmentManagement = new FragmentManagement();
                            TextView title = getActivity().findViewById(R.id.title_activity);
                            fragmentManagement.replaceMainFragment(title, transaction, getFragmentManager().findFragmentByTag("TEAMS"), "TEAMS");
                        }
                    }else{
                        Toast.makeText(getActivity(), groupid + " - This group does not exist. Please try again", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getActivity(), databaseError.getCode(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
