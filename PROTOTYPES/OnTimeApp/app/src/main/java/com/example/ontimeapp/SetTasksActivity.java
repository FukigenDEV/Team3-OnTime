package com.example.ontimeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class SetTasksActivity extends AppCompatActivity {

    private EditText task;
    private Button confirm;

    String tasktext;

    private static final String TAG = "SetTasks";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_tasks);
        task = (EditText)findViewById(R.id.etName);
        confirm = (Button)findViewById(R.id.confirmbtn);
        final String androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tasktext = task.getText().toString();
                if (tasktext.isEmpty()){
                    Toast.makeText(SetTasksActivity.this, "Please enter a name!", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(SetTasksActivity.this, token, Toast.LENGTH_SHORT).show();
                                    UserAdapter userAdapter = new UserAdapter(tasktext, token, "");
                                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                    DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users").child(androidId).child("Tasks").child(tasktext);
                                    databaseReference.setValue("Not finished");
                                    //databaseReference.setValue(userAdapter);
                                    Toast.makeText(SetTasksActivity.this, "Database Updated", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SetTasksActivity.this, MainMenu.class);
                                    intent.putExtra("androidId", androidId);
                                    intent.putExtra("userName", tasktext);
                                    intent.putExtra("userToken", token);
                                    startActivity(intent);
                                }
                            });
                }
            }
        });
    }
}

