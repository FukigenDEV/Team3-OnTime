package com.example.ontimeapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChecklistAdapter extends RecyclerView.Adapter<ChecklistAdapter.MyViewHolder> {

    ArrayList<String> Name, Code;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView taskName;
        TextView taskValue;
        public MyViewHolder(View groupView){
            super(groupView);
            taskName = groupView.findViewById(R.id.taskname);
            taskValue = groupView.findViewById(R.id.taskvalue);
        }
    }

    public ChecklistAdapter(Context context, ArrayList<String> name, ArrayList<String> code){
        this.context = context;
        this.Name = name;
        this.Code = code;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View groupView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_tasklist, parent, false);
        return new MyViewHolder(groupView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChecklistAdapter.MyViewHolder holder, final int position) {
        holder.taskName.setText(Name.get(position));
        holder.taskValue.setText(Code.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("taskName", Name.get(position));
                bundle.putString("taskValue", Code.get(position));

                FragmentManagement fragmentManagement = new FragmentManagement();
                FragmentTransaction transaction = ((MainUI)context).getSupportFragmentManager().beginTransaction();

                Fragment selectedGroup = new SelectedGroup();
                selectedGroup.setArguments(bundle);

                fragmentManagement.replaceMainFragment(holder.taskName, transaction, selectedGroup, Name.get(position));

            }
        });
    }

    @Override
    public int getItemCount() {
        return Name.size();
    }
}