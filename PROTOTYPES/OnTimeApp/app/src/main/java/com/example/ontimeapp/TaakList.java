package com.example.ontimeapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class TaakList extends AppCompatActivity  {

    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasklist);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_tasks);
        new FirebaseDatabaseHelper().readTaken(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Taken> taken, List<String> keys) {
                new RecyclerView_Config().setConfig(mRecyclerView, TaakList.this, taken, keys);
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });
        }

}
