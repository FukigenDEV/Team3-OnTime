package com.example.ontimeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;

public class MainMenu extends AppCompatActivity {

    private static final String TAG = "MainMenu";
    private FirebaseFunctions mFunctions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        Button showinstanceid = findViewById(R.id.InstanceIdTest);
        Button sendtodb = findViewById(R.id.sendInstanceId);
        mFunctions = FirebaseFunctions.getInstance();

        showinstanceid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                            Toast.makeText(MainMenu.this, token, Toast.LENGTH_SHORT).show();
                        }
                    });
            }
        });

        sendtodb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (!task.isSuccessful()){
                                    Log.w(TAG, "getInstanceId failed", task.getException());
                                    return;
                                }
                                String token = task.getResult().getToken();
                                sendDeviceToken(token)
                                        .addOnCompleteListener(new OnCompleteListener<String>() {
                                            @Override
                                            public void onComplete(@NonNull Task<String> task) {
                                                if (!task.isSuccessful()){
                                                    Exception e = task.getException();
                                                    if (e instanceof FirebaseFunctionsException){
                                                        FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                                        FirebaseFunctionsException.Code code = ffe.getCode();
                                                        Object details = ffe.getDetails();
                                                    }
                                                }
                                            }
                                        });
                            }
                        });
            }
        });
    }

    private Task<String> sendDeviceToken (String token) {
        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("push", true);

        return mFunctions
            .getHttpsCallable("sendDeviceToken")
            .call(data)
            .continueWith(new Continuation<HttpsCallableResult, String>() {
                @Override
                public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                    String result = (String) task.getResult().getData();
                    return result;
                }
            });
    }
}


