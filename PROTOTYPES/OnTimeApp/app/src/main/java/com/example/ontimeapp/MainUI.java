package com.example.ontimeapp;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainUI extends FragmentActivity {

    private Context context;

    ImageView icNav;
    LinearLayout navParent, global_nav;
    FrameLayout flGlobal;
    LayoutInflater layoutInflater;

    NavItem[] navItemArray;
    List<LinearLayout> navLayoutList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        icNav = findViewById(R.id.ic_nav);

        global_nav = findViewById(R.id.global_nav);
        navParent = findViewById(R.id.layout_sidenav);
        navLayoutList = Arrays.asList(global_nav, navParent);
        flGlobal = findViewById(R.id.global_framelayout);



        @SuppressLint("HardwareIds") final String androidId = Settings.Secure.getString(MainUI.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        final TextView activityTitle = findViewById(R.id.title_activity);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        FragmentManagement fragmentManagement = new FragmentManagement();
        fragmentManagement.setMainFragment(activityTitle, transaction, new EntryScreen(), "Home");

        layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        navItemArray = new NavItem[4];
        navItemArray[0] = new NavItem("Home", R.drawable.ic_iconmonstr_home_6);
        navItemArray[1] = new NavItem("Profile", R.drawable.ic_iconmonstr_user_1);
        navItemArray[2] = new NavItem("Team", R.drawable.ic_iconmonstr_user_29);
        navItemArray[3] = new NavItem("Settings", R.drawable.ic_iconmonstr_gear_11);

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
                        case "Home":
                            uiAnimation.hideMainNav(MainUI.this, navLayoutList);
                            fragmentManagement.setMainFragment(activityTitle, transaction, new HomeFragment(), title);
                            break;
                        case "Profile":
                            uiAnimation.hideMainNav(MainUI.this, navLayoutList);
                            fragmentManagement.setMainFragment(activityTitle, transaction, new ProfileFragment(), title);
                            break;
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

}
