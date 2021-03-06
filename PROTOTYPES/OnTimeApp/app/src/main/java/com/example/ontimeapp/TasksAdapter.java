package com.example.ontimeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.MyViewHolder> {

    ArrayList<String> Tasks;
    String androidId;
    Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView userTask;
        ImageView deleteTask;
        TextView tasksHeader;
        public MyViewHolder(View tasksView){
            super(tasksView);
            userTask = tasksView.findViewById(R.id.userTask);
            deleteTask = tasksView.findViewById(R.id.deleteBtn);
            tasksHeader = tasksView.findViewById(R.id.taskHeader);
        }
    }

    public TasksAdapter(Context context, ArrayList<String> tasks, String androidId){
        this.context = context;
        this.Tasks = tasks;
        this.androidId = androidId;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View tasksView = LayoutInflater.from(parent.getContext()).inflate(R.layout.taskeditrow, parent, false);
        return new MyViewHolder(tasksView);
    }

    @Override
    public void onBindViewHolder(@NonNull final TasksAdapter.MyViewHolder holder, final int position){
        holder.userTask.setText(Tasks.get(position));
        holder.deleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Users").child(androidId).child("Tasks").child(Tasks.get(position)).removeValue();
                FirebaseDatabase.getInstance().getReference().child("Groups").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            FirebaseDatabase.getInstance().getReference().child("Groups").child(snapshot.getKey()).child("Members").child(androidId).child("Tasks").child(Tasks.get(position)).removeValue();
                        }
                        Tasks.remove(position);
                        notifyDataSetChanged();
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
    public int getItemCount(){
        return Tasks.size();
    }

}
