package com.example.ontimeapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserChecklistAdapter extends RecyclerView.Adapter<UserChecklistAdapter.MyViewHolder> {

    ArrayList<String> Tasks, Status;
    Context context;

    String androidId;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView userTask;
        ImageView checkBox;
        public MyViewHolder(View taskchecklist){
            super(taskchecklist);
            userTask = taskchecklist.findViewById(R.id.userTask);
            checkBox = taskchecklist.findViewById(R.id.checkBoximg);
        }
    }

    @SuppressLint("HardwareIds")
    public UserChecklistAdapter (Context context, ArrayList<String> tasks, ArrayList<String> status){
        this.context = context;
        this.Tasks = tasks;
        this.Status = status;
        this.androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View taskchecklist = LayoutInflater.from(parent.getContext()).inflate(R.layout.checklistitem, parent, false);
        return new MyViewHolder(taskchecklist);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserChecklistAdapter.MyViewHolder holder, final int position){
        holder.userTask.setText(Tasks.get(position));
        if(Status.get(position).equals("Not finished")){
            holder.checkBox.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.iconmonstr_empty_checkbox_11_240));
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.checkBox.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.iconmonstr_checked_checkbox_9_240));

                    FirebaseDatabase.getInstance().getReference().child("Users").child(androidId).child("Tasks").child(Tasks.get(position)).setValue("Finished");

                    Status.set(position, "Finished");
                    notifyDataSetChanged();
                }
            });
        } else if (Status.get(position).equals("Finished")){
            holder.checkBox.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.iconmonstr_checked_checkbox_9_240));
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.checkBox.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.iconmonstr_empty_checkbox_11_240));

                    FirebaseDatabase.getInstance().getReference().child("Users").child(androidId).child("Tasks").child(Tasks.get(position)).setValue("Not finished");

                    Status.set(position, "Not finished");
                    notifyDataSetChanged();
                }
            });
        }
    }
    @Override
    public int getItemCount(){
        return Tasks.size();
    }
}
