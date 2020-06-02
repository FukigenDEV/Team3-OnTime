package com.example.ontimeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AlarmsAdapter extends RecyclerView.Adapter<AlarmsAdapter.MyViewHolder> {
    ArrayList<String> AlarmName, AlarmDate, AlarmTime;
    String teamcode;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView alarmName;
        TextView alarmDate;
        TextView alarmTime;
        Button removeAlarm;
        public MyViewHolder(View alarmView){
            super(alarmView);
            alarmName = alarmView.findViewById(R.id.alarmname);
            alarmDate = alarmView.findViewById(R.id.alarmdate);
            alarmTime = alarmView.findViewById(R.id.alarmtime);
            removeAlarm = alarmView.findViewById(R.id.removeAlarm);
        }
    }

    public AlarmsAdapter(Context context, ArrayList<String> alarmName, ArrayList<String> alarmDate, ArrayList<String> alarmTime, String teamCode){
        this.context = context;
        this.AlarmName = alarmName;
        this.AlarmDate = alarmDate;
        this.AlarmTime = alarmTime;
        this.teamcode = teamCode;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View alarmView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alarmsrow, parent, false);
        return new MyViewHolder(alarmView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.alarmName.setText(AlarmName.get(position));
        holder.alarmDate.setText(AlarmDate.get(position));
        holder.alarmTime.setText(AlarmTime.get(position));
        holder.removeAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Groups")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                    if (dataSnapshot1.getKey().equals(teamcode)){
                                        dataSnapshot1.getRef().child("Alarms").child(AlarmName.get(position)).removeValue();
                                        AlarmName.remove(AlarmName.get(position));
                                        AlarmDate.remove(AlarmDate.get(position));
                                        AlarmTime.remove(AlarmTime.get(position));
                                        notifyDataSetChanged();
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(context, databaseError.getCode(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return AlarmName.size();
    }

}
