package com.example.ontimeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainUI extends AppCompatActivity {

    String TAG = "MainUI";

    private Context context;

    ImageView icNav, subnavButton1, subnavButton2, subnavButton3;
    LinearLayout navParent, global_nav;
    FrameLayout flGlobal;
    LayoutInflater layoutInflater;
    String androidId;
    Bundle bundle, bundle1;

    NavItem[] navItemArray;
    List<LinearLayout> navLayoutList;

    @SuppressLint("HardwareIds")
    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        findViewById(R.id.global_titlebar).setVisibility(View.INVISIBLE);
        findViewById(R.id.title_activity).setVisibility(View.INVISIBLE);
        findViewById(R.id.ic_nav).setVisibility(View.INVISIBLE);
        findViewById(R.id.subNav).setVisibility(View.INVISIBLE);

        icNav = findViewById(R.id.ic_nav);

        subnavButton1 = findViewById(R.id.nav_bg_1);
        subnavButton2 = findViewById(R.id.nav_bg_2);
        subnavButton3 = findViewById(R.id.nav_bg_3);

        global_nav = findViewById(R.id.global_nav);
        navParent = findViewById(R.id.layout_sidenav);
        navLayoutList = Arrays.asList(global_nav, navParent);
        flGlobal = findViewById(R.id.global_framelayout);

        androidId = Settings.Secure.getString(MainUI.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users");

        final FragmentManagement fragmentManagement = new FragmentManagement();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TextView activityTitle = findViewById(R.id.title_activity);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                if (dataSnapshot.child(androidId).exists()) {
                    User user = dataSnapshot.child(androidId).getValue(User.class);

                    String userName = user.getName();
                    String userToken = user.getDeviceToken();
                    String userPhone = user.getPhone();

                    Fragment mainMenu = new MainMenu();
                    bundle = new Bundle();
                    bundle.putString("androidId", androidId);
                    bundle.putString("userName", userName);
                    bundle.putString("userToken", userToken);
                    bundle.putString("userPhone", userPhone);
                    mainMenu.setArguments(bundle);

                    fragmentManagement.setMainFragment(activityTitle, transaction, mainMenu, "TEAMS");
                } else {

                    Fragment createUser = new CreateUser();
                    Bundle bundle = new Bundle();
                    bundle.putString("androidId", androidId);
                    createUser.setArguments(bundle);

                    Log.d(TAG, "androidId: " + androidId);

                    fragmentManagement.replaceMainFragment(activityTitle, transaction, createUser, "CREATE USER");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, databaseError.getCode(), Toast.LENGTH_SHORT);
            }
        });

        final TextView activityTitle = findViewById(R.id.title_activity);
        layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        navItemArray = new NavItem[1];
        navItemArray[0] = new NavItem("Teams", R.drawable.ic_iconmonstr_user_29);

        int pos = 0;
        for (NavItem Item : navItemArray) {
            View sidenavItem = layoutInflater.inflate(R.layout.sidenav_item, navParent, false);
            LinearLayout holder = sidenavItem.findViewById(R.id.test);
            ImageView icon = sidenavItem.findViewById(R.id.navitem_icon);
            TextView text = sidenavItem.findViewById(R.id.navitem_text);

            final int index = pos;
            holder.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    FragmentManagement fragmentManagement = new FragmentManagement();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                    UIAnimation uiAnimation = new UIAnimation();
                    String title = navItemArray[index].title;

                    switch(navItemArray[index].title) {
                        case "Teams":
                            uiAnimation.hideMainNav(MainUI.this, navLayoutList);
                            Fragment fragment = getSupportFragmentManager().findFragmentByTag("TEAMS");
                            fragmentManagement.replaceMainFragment((TextView)findViewById(R.id.title_activity), transaction, fragment, "TEAMS");
                            break;
//                        case "Profile":
//                            uiAnimation.hideMainNav(MainUI.this, navLayoutList);
//                            fragmentManagement.setMainFragment(activityTitle, transaction, new ProfileFragment(), title);
//                            break;
                    }
                }
            });
            holder.setId(1+pos);
            icon.setImageResource(Item.iconResource);
            text.setText(Item.title);

            navParent.addView(sidenavItem);
            pos++;
        }

        icNav.setOnClickListener(new View.OnClickListener() {
            int state = 0;
            UIAnimation uiAnimation = new UIAnimation();
            public void onClick(View v) {
                if (state == 0) {
                    uiAnimation.showMainNav(MainUI.this, navLayoutList);
                    state = 1;
                } else {
                    uiAnimation.hideMainNav(MainUI.this, navLayoutList);
                    state = 0;
                }
            }
        });

        flGlobal.setOnClickListener(new View.OnClickListener() {
            UIAnimation uiAnimation = new UIAnimation();
            public void onClick(View v) {
                if (navParent.getTranslationX() == 0.0) {
                    uiAnimation.hideMainNav(MainUI.this, navLayoutList);
                }
                Log.d("navParent XPos", ""+navParent.getTranslationX());
            }
        });

        subnavButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.child("Users").child(androidId).getValue(User.class);

                        String userName = user.getName();
                        String userToken = user.getDeviceToken();
                        String userPhone = user.getPhone();

                        bundle1 = new Bundle();
                        bundle1.putString("androidId", androidId);
                        bundle1.putString("userName", userName);
                        bundle1.putString("userToken", userToken);
                        bundle1.putString("userPhone", userPhone);

                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        Fragment setTaskActivity = new SetTasksActivity();
                        setTaskActivity.setArguments(bundle1);
                        fragmentManagement.replaceMainFragment(activityTitle, transaction, setTaskActivity, "SET TASKS");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(context, databaseError.getCode(), Toast.LENGTH_SHORT);
                    }
                });
            }
        });

        subnavButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.child("Users").child(androidId).getValue(User.class);

                        String userName = user.getName();
                        String userToken = user.getDeviceToken();
                        String userPhone = user.getPhone();

                        bundle1 = new Bundle();
                        bundle1.putString("androidId", androidId);
                        bundle1.putString("userName", userName);
                        bundle1.putString("userToken", userToken);
                        bundle1.putString("userPhone", userPhone);

                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        Fragment joinGroup = new JoinGroup();
                        joinGroup.setArguments(bundle1);
                        fragmentManagement.replaceMainFragment(activityTitle, transaction, joinGroup, "JOIN TEAM");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(context, databaseError.getCode(), Toast.LENGTH_SHORT);
                    }
                });
            }
        });

        subnavButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.child("Users").child(androidId).getValue(User.class);

                        String userName = user.getName();
                        String userToken = user.getDeviceToken();
                        String userPhone = user.getPhone();

                        bundle1 = new Bundle();
                        bundle1.putString("androidId", androidId);
                        bundle1.putString("userName", userName);
                        bundle1.putString("userToken", userToken);
                        bundle1.putString("userPhone", userPhone);

                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        Fragment createGroup = new CreateGroup();
                        createGroup.setArguments(bundle1);
                        fragmentManagement.replaceMainFragment(activityTitle, transaction, createGroup, "CREATE TEAM");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(context, databaseError.getCode(), Toast.LENGTH_SHORT);
                    }
                });
            }
        });

        Constraints constraints = new Constraints.Builder()
                .build();

        PeriodicWorkRequest syncAlarmRequest =
                new PeriodicWorkRequest
                        .Builder(AlarmSyncWorker.class, 15, TimeUnit.MINUTES)
                        .setConstraints(constraints)
                        .build();

        WorkManager.getInstance(context).enqueueUniquePeriodicWork("alarmSync", ExistingPeriodicWorkPolicy.REPLACE, syncAlarmRequest);
    }

    public static class NavItem {
        String title;
        int iconResource;

        NavItem(String title_, int iconResource_) {
            title = title_;
            iconResource = iconResource_;
        }
    }

    @Override
    public void onBackPressed() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        FragmentManagement fragmentManagement = new FragmentManagement();
        fragmentManagement.logFragments(getSupportFragmentManager());

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.global_framelayout);
        if(!(f instanceof MainMenu) && !(f instanceof CreateUser)) {
            fragmentManagement.replaceMainFragment((TextView) findViewById(R.id.title_activity), transaction, getSupportFragmentManager().findFragmentByTag("TEAMS"), "TEAMS");
        } else {
            finishAffinity();
        }
    }
}
