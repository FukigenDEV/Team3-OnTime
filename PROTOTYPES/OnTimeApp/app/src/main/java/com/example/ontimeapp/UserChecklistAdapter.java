package com.example.ontimeapp;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserChecklistAdapter extends RecyclerView.Adapter<UserChecklistAdapter.MyViewHolder> {

    ArrayList<String> Tasks, Status;
    Context;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView userTask;
        ImageView checkBox;
        public MyViewHolder(View taskchecklist){
            super(taskchecklist);
            userTask = taskchecklist.findViewById(R.id.userTask);
            checkBox = taskchecklist.findViewById(R.id.checkBoximg);
        }
    }
}
