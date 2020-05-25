package com.example.ontimeapp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class CreateUser extends AppCompatActivity {

    private EditText etName;
    private Button confirmName;

    String name;
    private static final String TAG = "CreateUser";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createuser);
        etName = (EditText)findViewById(R.id.etName);
        confirmName = (Button)findViewById(R.id.confirmName);
        final String androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        confirmName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = etName.getText().toString();
                if (name.isEmpty()){
                    Toast.makeText(CreateUser.this, "Please enter a name!", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(CreateUser.this, token, Toast.LENGTH_SHORT).show();
                                    UserAdapter userAdapter = new UserAdapter(name, token);
                                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                    DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users").child(androidId);
                                    databaseReference.setValue(userAdapter);
                                    Toast.makeText(CreateUser.this, "Database Updated", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(CreateUser.this, MainMenu.class);
                                    intent.putExtra("androidId", androidId);
                                    intent.putExtra("userName", name);
                                    intent.putExtra("userToken", token);
                                    startActivity(intent);
                                }
                            });
                }
            }
        });
    }
}
