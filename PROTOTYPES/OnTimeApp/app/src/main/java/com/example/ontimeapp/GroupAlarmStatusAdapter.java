package com.example.ontimeapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GroupAlarmStatusAdapter extends RecyclerView.Adapter<GroupAlarmStatusAdapter.MyViewHolder> {

    ArrayList<String> Name, Status, Number, userAndroidIds;
    Context context;
    String currentAndroidId;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView memberAlarmStatus;
        TextView memberName;
        ImageView statusImage;
        public MyViewHolder(View alarmStatusView){
            super(alarmStatusView);
            memberAlarmStatus = alarmStatusView.findViewById(R.id.memberAlarmStatus);
            memberName = alarmStatusView.findViewById(R.id.memberName);
            statusImage = alarmStatusView.findViewById(R.id.statusImage);

        }
    }

    public GroupAlarmStatusAdapter(Context context, ArrayList<String> name, ArrayList<String> status, ArrayList<String> number, ArrayList<String> userAndroidIds, String currentAndroidId){
        this.context = context;
        this.Name = name;
        this.Status = status;
        this.Number = number;
        this.userAndroidIds = userAndroidIds;
        this.currentAndroidId = currentAndroidId;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View alarmStatusView = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarmstatusrow, parent, false);
        return new MyViewHolder(alarmStatusView);
    }

    @Override
    public void onBindViewHolder(@NonNull final GroupAlarmStatusAdapter.MyViewHolder holder, final int position){
        holder.memberName.setText(Name.get(position));
        holder.memberAlarmStatus.setText(Status.get(position));
        if (userAndroidIds.get(position).equals(currentAndroidId)){
            holder.statusImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.iconmonstr_smiley_13_240));
            holder.statusImage.setVisibility(View.VISIBLE);
        }else if(Status.get(position).equals("NOT AWAKE!")){
            holder.statusImage.setVisibility(View.VISIBLE);
            holder.statusImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callGroupMember(Number.get(position));
                }
            });
        }else if (Status.get(position).equals("AWAKE")){
            holder.statusImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.iconmonstr_smiley_13_240));
            holder.statusImage.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount(){
        return Name.size();
    }

    private void callGroupMember(final String phoneNumber){
        context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }

}
