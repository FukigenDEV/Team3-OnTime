package com.example.ontimeapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.MyViewHolder> {

    ArrayList<String> Name, Code;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder{
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
    public void onBindViewHolder(@NonNull GroupsAdapter.MyViewHolder holder, final int position) {
        holder.groupName.setText(Name.get(position));
        holder.groupCode.setText(Code.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selectedgroup = new Intent(v.getContext(), SelectedGroup.class);
                selectedgroup.putExtra("groupName", Name.get(position));
                selectedgroup.putExtra("groupCode", Code.get(position));
                context.startActivity(selectedgroup);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Name.size();
    }
}
