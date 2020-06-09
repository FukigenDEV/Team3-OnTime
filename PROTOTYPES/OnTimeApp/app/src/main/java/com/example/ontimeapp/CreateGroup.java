package com.example.ontimeapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
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

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class CreateGroup extends Fragment implements View.OnClickListener {

    private EditText etgroupName;
    private Button confirmGroupNamebtn;

    private static final String ALLOWED_CHARACTERS ="0123456789QWERTYUIOPASDFGHJKLZXCVBNM";

    String name, groupId, androidId, userName, userToken, userPhone;

    private static final String TAG = "CreateGroup";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Bundle args = getArguments();
        androidId = args.getString("androidId");
        userName = args.getString("userName");
        userToken = args.getString("userToken");
        userPhone = args.getString("userPhone");
    }

    public CreateGroup() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_creategroup, container, false);

        etgroupName = (EditText) rootView.findViewById(R.id.etgroupName);
        confirmGroupNamebtn = (Button) rootView.findViewById(R.id.confirmGroupNamebtn);

        confirmGroupNamebtn.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        final FragmentManagement fragmentManagement = new FragmentManagement();
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();

        int viewId = v.getId();

        if(viewId == R.id.confirmGroupNamebtn) {
            name = etgroupName.getText().toString();
            groupId = generateId(5);

            Group group = new Group(groupId, name);
            final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference1 = firebaseDatabase.getReference().child("Groups").child(groupId);
            databaseReference1.setValue(group);

            final User user = new User(userName, userToken, userPhone);
            DatabaseReference databaseReference2 = firebaseDatabase.getReference().child("Groups").child(groupId).child("Members").child(androidId);
            databaseReference2.setValue(user);

            FirebaseDatabase.getInstance().getReference().child("Users").child(androidId).child("Tasks").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        String userTask = snapshot.getKey();
                        DatabaseReference databaseReference2 = firebaseDatabase.getReference().child("Groups").child(groupId).child("Members").child(androidId).child("Tasks").child(userTask);
                        databaseReference2.setValue("Not finished");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getActivity(), databaseError.getCode(), Toast.LENGTH_SHORT).show();
                }
            });

            Toast.makeText(getContext(), "Group Successfully created!", Toast.LENGTH_SHORT).show();

            fragmentManagement.replaceMainFragment((TextView)getActivity().findViewById(R.id.title_activity), transaction, getFragmentManager().findFragmentByTag("TEAMS"), "TEAMS");
        }
    }

    public static String generateId(int sizeofid){
        Random generator = new Random();
        StringBuilder stringBuilder = new StringBuilder(sizeofid);
        for(int i = 0; i < sizeofid; i++){
            stringBuilder.append(ALLOWED_CHARACTERS.charAt(generator.nextInt(ALLOWED_CHARACTERS.length())));
        }
        return stringBuilder.toString();
    }
}
