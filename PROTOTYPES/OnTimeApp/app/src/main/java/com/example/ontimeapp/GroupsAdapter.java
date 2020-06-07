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

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.MyViewHolder> {

    ArrayList<String> Name, Code;
    Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView groupName;
        TextView groupCode;
        public MyViewHolder(View groupView){
            super(groupView);
            groupName = groupView.findViewById(R.id.groupname);
            groupCode = groupView.findViewById(R.id.groupcode);
        }
    }

    public GroupsAdapter(Context context, ArrayList<String> name, ArrayList<String> code){
        this.context = context;
        this.Name = name;
        this.Code = code;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View groupView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.groupsrow, parent, false);
        return new MyViewHolder(groupView);
    }

    @Override
    public void onBindViewHolder(@NonNull final GroupsAdapter.MyViewHolder holder, final int position) {
        holder.groupName.setText(Name.get(position));
        holder.groupCode.setText(Code.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("groupName", Name.get(position));
                bundle.putString("groupCode", Code.get(position));

                FragmentManagement fragmentManagement = new FragmentManagement();
                FragmentTransaction transaction = ((MainUI)context).getSupportFragmentManager().beginTransaction();

                Fragment selectedGroup = new SelectedGroup();
                selectedGroup.setArguments(bundle);

                fragmentManagement.replaceMainFragment(holder.groupName, transaction, selectedGroup, Name.get(position));

            }
        });
    }

    @Override
    public int getItemCount() {
        return Name.size();
    }
}

