package com.example.ontimeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.work.Constraints;
import androidx.work.Data;
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

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainUI extends AppCompatActivity {

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
        fragmentManagement.replaceMainFragment(activityTitle, transaction, new EntryScreen(), "ENTRYSCREEN");

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

        Constraints constraints = new Constraints.Builder()
                .build();

        PeriodicWorkRequest syncAlarmRequest =
                new PeriodicWorkRequest.Builder(AlarmSyncWorker.class, 15, TimeUnit.MINUTES)
                        .setConstraints(constraints)
                        .build();

        WorkManager.getInstance(context).enqueue(syncAlarmRequest);
    }

    public static class NavItem {
        String title;
        int iconResource;

        NavItem(String title_, int iconResource_) {
            title = title_;
            iconResource = iconResource_;
        }
    }

//    public void onBackPressed() {
//        FragmentManagement fragmentManagement = new FragmentManagement();
//        fragmentManagement.logFragments(getSupportFragmentManager());
//
//        int fragmentAmount = getSupportFragmentManager().getBackStackEntryCount();
//        FragmentManager.BackStackEntry fragmentPrev = getSupportFragmentManager().getBackStackEntryAt(fragmentAmount-2);
//        FragmentManager.BackStackEntry fragmentCurr = getSupportFragmentManager().getBackStackEntryAt(fragmentAmount-1);
//        String tag = fragmentPrev.getName();
//        String tag2 = fragmentCurr.getName();
//
//        Log.d("BackPress", ""+tag+" Fragments"+getSupportFragmentManager().getFragments());
//
//        if((!tag.equals("Home")) && (!tag.equals("Create User"))) {
//            final TextView activityTitle = findViewById(R.id.title_activity);
//
//            Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
//            Fragment fragment2 = getSupportFragmentManager().findFragmentByTag(tag2);
//
//            this.getSupportFragmentManager().popBackStackImmediate();
//
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.remove(fragment2);
//            transaction.replace(R.id.global_framelayout, fragment, tag);
//            transaction.commit();
//
//            activityTitle.setText(tag);
//        } else {
//            finishAffinity();
//        }
//    }


    @Override
    public void onBackPressed() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        FragmentManagement fragmentManagement = new FragmentManagement();
        fragmentManagement.logFragments(getSupportFragmentManager());

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.global_framelayout);
        if(!(f instanceof MainMenu)) {
            fragmentManagement.replaceMainFragment((TextView) findViewById(R.id.title_activity), transaction, getSupportFragmentManager().findFragmentByTag("TEAMS"), "TEAMS");
        } else {
            finishAffinity();
        }
    }
}
