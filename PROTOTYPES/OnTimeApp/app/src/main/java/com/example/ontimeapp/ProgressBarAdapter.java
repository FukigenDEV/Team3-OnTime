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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProgressBarAdapter extends RecyclerView.Adapter<ProgressBarAdapter.MyViewHolder> {

    ArrayList<String> progressName, progressAndroidId, progressPhone;
    String groupId;
    Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView userNamePill;
        ImageView callPillTask;
        ImageView taskProgressLine;

        ImageView startTaskcircle;
        ImageView task1circle;
        ImageView task2circle;
        ImageView task3circle;
        ImageView task4circle;
        ImageView endTaskcircle;

        ConstraintLayout holderTasks;
        public MyViewHolder(View prograssBarView){
            super(prograssBarView);
            userNamePill = prograssBarView.findViewById(R.id.userNamePill);
            callPillTask = prograssBarView.findViewById(R.id.callpillTask);
            taskProgressLine = prograssBarView.findViewById(R.id.taskProgressLine);

            startTaskcircle = prograssBarView.findViewById(R.id.starttaskCircle1_3);
            task1circle = prograssBarView.findViewById(R.id.taskCircle1_3);
            task2circle = prograssBarView.findViewById(R.id.taskCircle2_3);
            task3circle = prograssBarView.findViewById(R.id.taskCircle3_3);
            task4circle = prograssBarView.findViewById(R.id.taskCircle4_3);
            endTaskcircle = prograssBarView.findViewById(R.id.endTask1_3);

            holderTasks = prograssBarView.findViewById(R.id.holderTasks);
        }
    }

    public ProgressBarAdapter(Context context, ArrayList<String> progressName, ArrayList<String> progressPhone, ArrayList<String> progressAndroidId, String groupId){
        this.context = context;
        this.progressName = progressName;
        this.progressPhone = progressPhone;
        this.groupId = groupId;
        this.progressAndroidId = progressAndroidId;
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
        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Integer amtTotalTasks, amtCompletedTasks;
                ArrayList<String> totalTasks = new ArrayList<>();
                final ArrayList<String> completedTasks = new ArrayList<>();
                for (DataSnapshot tasksSnapshot : dataSnapshot.child("Groups").child(groupId).child("Members").child(progressAndroidId.get(position)).child("Tasks").getChildren()){
                    totalTasks.add(tasksSnapshot.getKey());
                    String taskState = tasksSnapshot.getValue().toString();
                    if(taskState.equals("Finished")){
                        completedTasks.add(tasksSnapshot.getKey());
                    }
                }
                amtTotalTasks = totalTasks.size();
                amtCompletedTasks = completedTasks.size();
                holder.userNamePill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String message = progressName.get(position) + " has completed " + amtCompletedTasks + " out of " + amtTotalTasks + " tasks!";
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                });
                float percentComplete = (amtCompletedTasks * 100.0f) / amtTotalTasks;
                System.out.println(percentComplete);
                if(percentComplete > 0){
                    holder.startTaskcircle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.task_progress_circle3));
                    if (percentComplete < 20) {
                        ConstraintSet constraintSet = new ConstraintSet();
                        constraintSet.clone(holder.holderTasks);
                        constraintSet.connect(R.id.taskProgressLine, ConstraintSet.END, R.id.task1, ConstraintSet.END);
                        constraintSet.applyTo(holder.holderTasks);
                    } else if (percentComplete == 20) {
                        ConstraintSet constraintSet = new ConstraintSet();
                        constraintSet.clone(holder.holderTasks);
                        constraintSet.connect(R.id.taskProgressLine, ConstraintSet.END, R.id.task1, ConstraintSet.END);
                        constraintSet.applyTo(holder.holderTasks);

                        holder.task1circle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.task_progress_circle3));
                    } else if (percentComplete < 40) {
                        ConstraintSet constraintSet = new ConstraintSet();
                        constraintSet.clone(holder.holderTasks);
                        constraintSet.connect(R.id.taskProgressLine, ConstraintSet.END, R.id.task2, ConstraintSet.END);
                        constraintSet.applyTo(holder.holderTasks);

                        holder.task1circle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.task_progress_circle3));
                    } else if (percentComplete == 40){
                        ConstraintSet constraintSet = new ConstraintSet();
                        constraintSet.clone(holder.holderTasks);
                        constraintSet.connect(R.id.taskProgressLine, ConstraintSet.END, R.id.task2, ConstraintSet.END);
                        constraintSet.applyTo(holder.holderTasks);

                        holder.task1circle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.task_progress_circle3));
                        holder.task2circle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.task_progress_circle3));
                    } else if (percentComplete < 60) {
                        ConstraintSet constraintSet = new ConstraintSet();
                        constraintSet.clone(holder.holderTasks);
                        constraintSet.connect(R.id.taskProgressLine, ConstraintSet.END, R.id.task3, ConstraintSet.END);
                        constraintSet.applyTo(holder.holderTasks);

                        holder.task1circle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.task_progress_circle3));
                        holder.task2circle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.task_progress_circle3));
                    } else if (percentComplete == 60){
                        ConstraintSet constraintSet = new ConstraintSet();
                        constraintSet.clone(holder.holderTasks);
                        constraintSet.connect(R.id.taskProgressLine, ConstraintSet.END, R.id.task3, ConstraintSet.END);
                        constraintSet.applyTo(holder.holderTasks);

                        holder.task1circle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.task_progress_circle3));
                        holder.task2circle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.task_progress_circle3));
                        holder.task3circle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.task_progress_circle3));
                    } else if (percentComplete < 80) {
                        ConstraintSet constraintSet = new ConstraintSet();
                        constraintSet.clone(holder.holderTasks);
                        constraintSet.connect(R.id.taskProgressLine, ConstraintSet.END, R.id.task4, ConstraintSet.END);
                        constraintSet.applyTo(holder.holderTasks);

                        holder.task1circle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.task_progress_circle3));
                        holder.task2circle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.task_progress_circle3));
                        holder.task3circle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.task_progress_circle3));
                    } else if (percentComplete == 80){
                        ConstraintSet constraintSet = new ConstraintSet();
                        constraintSet.clone(holder.holderTasks);
                        constraintSet.connect(R.id.taskProgressLine, ConstraintSet.END, R.id.task4, ConstraintSet.END);
                        constraintSet.applyTo(holder.holderTasks);

                        holder.task1circle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.task_progress_circle3));
                        holder.task2circle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.task_progress_circle3));
                        holder.task3circle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.task_progress_circle3));
                        holder.task4circle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.task_progress_circle3));
                    } else if (percentComplete > 80 && percentComplete < 100){
                        ConstraintSet constraintSet = new ConstraintSet();
                        constraintSet.clone(holder.holderTasks);
                        constraintSet.connect(R.id.taskProgressLine, ConstraintSet.END, R.id.endTask, ConstraintSet.END);
                        constraintSet.applyTo(holder.holderTasks);

                        holder.task1circle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.task_progress_circle3));
                        holder.task2circle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.task_progress_circle3));
                        holder.task3circle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.task_progress_circle3));
                        holder.task4circle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.task_progress_circle3));
                    }
                    else if (percentComplete == 100){
                        holder.task1circle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.task_progress_circle3));
                        holder.task2circle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.task_progress_circle3));
                        holder.task3circle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.task_progress_circle3));
                        holder.task4circle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.task_progress_circle3));
                        holder.endTaskcircle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.task_progress_circle3));

                        ConstraintSet constraintSet = new ConstraintSet();
                        constraintSet.clone(holder.holderTasks);
                        constraintSet.connect(R.id.taskProgressLine, ConstraintSet.END, R.id.endTask, ConstraintSet.END);
                        constraintSet.applyTo(holder.holderTasks);

                        holder.task1circle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.task_progress_circle3));
                        holder.task2circle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.task_progress_circle3));
                        holder.task3circle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.task_progress_circle3));
                        holder.task4circle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.task_progress_circle3));
                        holder.endTaskcircle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.task_progress_circle3));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, databaseError.getCode(), Toast.LENGTH_SHORT).show();
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
