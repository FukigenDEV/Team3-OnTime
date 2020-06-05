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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.work.Constraints;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

public class EntryScreen extends Fragment {

    private static final String TAG = "EntryScreen";
    private Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        getActivity().findViewById(R.id.global_titlebar).setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.title_activity).setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.ic_nav).setVisibility(View.INVISIBLE);
    }

    public EntryScreen() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        @SuppressLint("HardwareIds") final String androidId = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FragmentManagement fragmentManagement = new FragmentManagement();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                TextView activityTitle = getActivity().findViewById(R.id.title_activity);

                if (dataSnapshot.child(androidId).exists()){
                    Toast.makeText(getActivity(), "Android ID exists in database", Toast.LENGTH_SHORT).show();
                    User user = dataSnapshot.child(androidId).getValue(User.class);

                    String userName = user.getName();
                    String userToken = user.getDeviceToken();
                    System.out.println(userName + userToken);

                    Fragment mainMenu = new MainMenu();
                    Bundle bundle = new Bundle();
                    bundle.putString("androidId", androidId);
                    bundle.putString("userName", userName);
                    bundle.putString("userToken", userToken);
                    mainMenu.setArguments(bundle);

                    fragmentManagement.setMainFragment(activityTitle, transaction, mainMenu, "TEAMS");
                }else{
                    Toast.makeText(getActivity(), androidId + "Android ID does not exist in database", Toast.LENGTH_SHORT).show();

                    Fragment createUser = new CreateUser();
                    Bundle bundle = new Bundle();
                    bundle.putString("androidId", androidId);
                    createUser.setArguments(bundle);

                    Log.d(TAG, "androidId: "+androidId);

                    fragmentManagement.replaceMainFragment(activityTitle, transaction, createUser, "CREATE USER");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getCode(), Toast.LENGTH_SHORT);
            }
        });

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_entryscreen, container, false);
    }
}
