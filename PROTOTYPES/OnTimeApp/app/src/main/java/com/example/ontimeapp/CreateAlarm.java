package com.example.ontimeapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class CreateAlarm extends Fragment implements View.OnClickListener {

    Button btnDatePicker, btnTimePicker, confirmAlarm;
    EditText alarmName, alarmDate, alarmTime;
    private int pickedYear, pickedMonth, pickedDay, pickedHour, pickedMinute;
    String alarmNamestring, alarmDatestring, alarmTimestring, groupCode, groupName;

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        Bundle args = getArguments();
        groupCode = args.getString("groupCode");
        groupName = args.getString("groupName");
    }

    public CreateAlarm() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.activity_createalarm, container, false);

        alarmName = rootView.findViewById(R.id.setAlarmName);
        alarmDate = rootView.findViewById(R.id.newAlarmDatetxt);
        alarmTime = rootView.findViewById(R.id.newAlarmTimetxt);

        btnDatePicker = rootView.findViewById(R.id.newAlarmDatebtn);
        btnTimePicker = rootView.findViewById(R.id.newAlarmTimebtn);
        confirmAlarm = rootView.findViewById(R.id.confirmNewAlarmbtn);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        confirmAlarm.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        final FragmentManagement fragmentManagement = new FragmentManagement();
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();

        int viewId = v.getId();

        if(viewId == R.id.newAlarmDatebtn){

            final Calendar c = Calendar.getInstance();
            pickedYear = c.get(Calendar.YEAR);
            pickedMonth = c.get(Calendar.MONTH);
            pickedDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    alarmDate.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                }
            }, pickedYear, pickedMonth, pickedDay);
            datePickerDialog.show();
        }
        else if(viewId == R.id.newAlarmTimebtn){

            final Calendar c = Calendar.getInstance();
            pickedHour = c.get(Calendar.HOUR_OF_DAY);
            pickedMinute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    alarmTime.setText(hourOfDay + ":" + minute);
                }
            }, pickedHour, pickedMinute, true);
            timePickerDialog.show();
        }
        else if(viewId == R.id.confirmNewAlarmbtn){
            alarmNamestring = alarmName.getText().toString();
            alarmDatestring = alarmDate.getText().toString();
            alarmTimestring = alarmTime.getText().toString();
            if (alarmNamestring.isEmpty() || alarmDatestring.isEmpty() || alarmTimestring.isEmpty()){
                Toast.makeText(getContext(), "Please fill in all of the fields", Toast.LENGTH_SHORT).show();
            }
            else{
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference().child("Groups").child(groupCode).child("Alarms").child(alarmNamestring);
                Alarm alarm = new Alarm(alarmNamestring, alarmDatestring, alarmTimestring);
                databaseReference.setValue(alarm);
                Toast.makeText(getContext(), "Alarm successfully added!", Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                bundle.putString("groupName", groupName);
                bundle.putString("groupCode", groupCode);

                Fragment selectedGroup = new SelectedGroup();
                selectedGroup.setArguments(bundle);

                fragmentManagement.replaceMainFragment((TextView)getActivity().findViewById(R.id.title_activity), transaction, selectedGroup, groupCode);
            }
        }
    }
}
