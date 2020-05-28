package com.example.ontimeapp;

import android.util.Log;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

class FragmentManagement {

        void setMainFragment(TextView activityTitle, FragmentTransaction transaction, Fragment fragment, String title) {
            transaction.replace(R.id.global_framelayout, fragment);
            transaction.addToBackStack(null);
            transaction.commit();

            Log.d("TAG", title);
            activityTitle.setText(title);
        }

}
