package com.example.alarmtest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class AlarmActive extends AppCompatActivity {

    Button dismissAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarmactive);

        dismissAlarm = findViewById(R.id.dismissAlarm);
        dismissAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

            }
        });

    }
}
