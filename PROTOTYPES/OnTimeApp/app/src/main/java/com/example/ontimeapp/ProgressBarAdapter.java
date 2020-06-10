package com.example.ontimeapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProgressBarAdapter extends RecyclerView.Adapter<ProgressBarAdapter.MyViewHolder> {

    ArrayList<String> progressName, progressPhone;
    String groupId, androidId;
    Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView userNamePill;
        ImageView callPillTask;
        ImageView taskProgressLine;
        public MyViewHolder(View prograssBarView){
            super(prograssBarView);
            userNamePill = prograssBarView.findViewById(R.id.userNamePill);
            callPillTask = prograssBarView.findViewById(R.id.callpillTask);
            taskProgressLine = prograssBarView.findViewById(R.id.taskProgressLine);
        }
    }

    public ProgressBarAdapter(Context context, ArrayList<String> progressName, ArrayList<String> progressPhone, String groupId, String androidId){
        this.context = context;
        this.progressName = progressName;
        this.progressPhone = progressPhone;
        this.groupId = groupId;
        this.androidId = androidId;
    }

    @NonNull
    @Override
    public ProgressBarAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View progressBarView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_progress_item, parent, false);
        return new MyViewHolder(progressBarView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProgressBarAdapter.MyViewHolder holder, final int position){
        holder.userNamePill.setText(progressName.get(position));
        holder.callPillTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callGroupMember(progressPhone.get(position));
            }
        });
    }

    @Override
    public int getItemCount(){
        return progressName.size();
    }

    private void callGroupMember(final String phoneNumber){
        context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }
}
