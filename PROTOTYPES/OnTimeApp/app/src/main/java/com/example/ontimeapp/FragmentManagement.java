package com.example.ontimeapp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

class FragmentManagement {

    void setMainFragment(TextView activityTitle, FragmentTransaction transaction, Fragment fragment, String title) {
        transaction.replace(R.id.global_framelayout, fragment, title);
        transaction.addToBackStack(title);
        transaction.commit();

        Log.d("TAG", title);
        activityTitle.setText(title);
    }

//    void clearFragments(FragmentManager fragmentManager) {
//        final FragmentManager fm = fragmentManager;
//        Fragment fragment = fm.findFragmentByTag("Home");
//        if(fragment != null)
//            fm.beginTransaction().remove(fragment).commit();
//            Log.d("FM", ""+fm.getBackStackEntryAt(i).getName());
//    }

    void logFragments(FragmentManager fragmentManager) {
        final FragmentManager fm = fragmentManager;
        for (int i = fragmentManager.getBackStackEntryCount() - 1; i >= 0; i--) {
            Log.d("Fragments", "" + fragmentManager.getBackStackEntryAt(i).getName());
        }
    }

}
