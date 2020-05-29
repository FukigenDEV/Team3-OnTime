package com.example.ontimeapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SelectedGroup extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectedgroup);

        Intent selectedgroup = getIntent();
        String groupName = selectedgroup.getStringExtra("groupName");
        String groupCode = selectedgroup.getStringExtra("groupCode");

        TextView setGroupName = findViewById(R.id.groupname);
        TextView setGroupCode = findViewById(R.id.groupcode);

        setGroupName.setText(groupName);
        setGroupCode.setText(groupCode + " (others can use this code to join your group)");

    }
}
