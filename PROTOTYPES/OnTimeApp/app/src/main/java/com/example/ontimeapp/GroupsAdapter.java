package com.example.ontimeapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.MyViewHolder> {

    ArrayList<String> Name, Code;
    String androidId;
    Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView groupName;
        TextView groupCode;
        ImageView exitButton;
        public MyViewHolder(View groupView){
            super(groupView);
            groupName = groupView.findViewById(R.id.groupname);
            groupCode = groupView.findViewById(R.id.groupcode);
            exitButton = groupView.findViewById(R.id.exitGroupBtn);
        }
    }

    public GroupsAdapter(Context context, ArrayList<String> name, ArrayList<String> code, String androidId){
        this.context = context;
        this.Name = name;
        this.Code = code;
        this.androidId = androidId;
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
        holder.exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Groups").child(Code.get(position)).child("Members").child(androidId).removeValue();
                FirebaseDatabase.getInstance().getReference("Groups").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(Code.get(position)).hasChild("Alarms")){
                            for(DataSnapshot snapshot : dataSnapshot.child(Code.get(position)).child("Alarms").getChildren()){
                                snapshot.child("MemberState").child(androidId).getRef().removeValue();
                            }
                        }
                        if(!dataSnapshot.child(Code.get(position)).hasChild("Members")){
                            dataSnapshot.child(Code.get(position)).getRef().removeValue();
                        }
                        if(!dataSnapshot.child(Code.get(position)).child("Members").hasChildren()){
                            dataSnapshot.child(Code.get(position)).getRef().removeValue();
                        }
                        Name.remove(position);
                        Code.remove(position);
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
    public int getItemCount() {
        return Name.size();
    }
}

