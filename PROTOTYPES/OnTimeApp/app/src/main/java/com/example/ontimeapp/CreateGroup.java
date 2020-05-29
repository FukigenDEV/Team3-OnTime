package com.example.ontimeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class CreateGroup extends AppCompatActivity {

    private EditText etgroupName;
    private Button confirmGroupNamebtn;

    private static final String ALLOWED_CHARACTERS ="0123456789QWERTYUIOPASDFGHJKLZXCVBNM";

    String name, groupId, androidId, userName, userToken;

    private static final String TAG = "CreateGroup";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creategroup);
        etgroupName = (EditText)findViewById(R.id.etgroupName);
        confirmGroupNamebtn = (Button)findViewById(R.id.confirmGroupNamebtn);

        Intent intent = getIntent();
        androidId = intent.getStringExtra("androidId");
        userName = intent.getStringExtra("userName");
        userToken = intent.getStringExtra("userToken");

        confirmGroupNamebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = etgroupName.getText().toString();
                groupId = generateId(10);

                Group group = new Group(groupId, name);
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference1 = firebaseDatabase.getReference().child("Groups").child(groupId);
                databaseReference1.setValue(group);

                User user = new User(userName, userToken);
                DatabaseReference databaseReference2 = firebaseDatabase.getReference().child("Groups").child(groupId).child("Members").child(androidId);
                databaseReference2.setValue(user);

                Toast.makeText(CreateGroup.this, "Group Successfully created!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CreateGroup.this, MainMenu.class));
            }
        });
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
