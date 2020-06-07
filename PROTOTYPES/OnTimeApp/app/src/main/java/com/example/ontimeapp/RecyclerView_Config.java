package com.example.ontimeapp;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;

import com.example.ontimeapp.Taken;

import java.util.List;


public class RecyclerView_Config {
    private Context mContext;
    private TakenAdapter mTakenAdapter;
    public void setConfig(RecyclerView recyclerView, Context context, List<Taken> taken, List<String> keys){
        mContext = context;
        mTakenAdapter = new TakenAdapter(taken, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mTakenAdapter);
    }


    class TaakItemView extends RecyclerView.ViewHolder{
        private TextView mTaak;
        private TextView mIsFinished;

        private String key;

        public TaakItemView(ViewGroup parent){
            super(LayoutInflater.from(mContext).inflate(R.layout.task_list_item, parent, false));

            mTaak = (TextView) itemView.findViewById(R.id.task_txtView);
            mIsFinished = (TextView) itemView.findViewById(R.id.isfinished_txtView);

        }
        public void bind(Taken taak, String key) {
            mTaak.setText(taak.getTaak());
            mIsFinished.setText(taak.getisFinished());
            this.key = key;
        }

    }
class TakenAdapter extends RecyclerView.Adapter<TaakItemView> {
        private List<Taken> mTaakList;
        private List<String> mKeys;

    public TakenAdapter(List<Taken> mTaakList, List<String> mKeys) {
        this.mTaakList = mTaakList;
        this.mKeys = mKeys;
    }

    @NonNull
    @Override
    public TaakItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaakItemView(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull TaakItemView holder, int position) {
        holder.bind(mTaakList.get (position), mKeys.get (position));

    }

    @Override
    public int getItemCount() {
        return mTaakList.size();
    }
}


}
