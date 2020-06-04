package com.example.ontimeapp;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;

import com.example.ontimeapp.Book;

import java.util.List;


public class RecyclerView_Config {
    private Context mContext;
    private BooksAdapter mBooksAdapter;

    public void setConfig(RecyclerView recyclerView, Context context, List<Book> books, List<String> keys){
        mContext = context;
        mBooksAdapter = new BooksAdapter(books, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mBooksAdapter);
    }


    class BookItemView extends RecyclerView.ViewHolder{
        private TextView mTitle;
        //private TextView mIsFinished;

        private String key;

        public BookItemView(ViewGroup parent){
            super(LayoutInflater.from(mContext).inflate(R.layout.task_list_item, parent, false));


            mTitle = (TextView) itemView.findViewById(R.id.task_txtView);
            //mIsFinished = (TextView) itemView.findViewById(R.id.isfinished_txtView);

        }
        public void bind(Book books, String key) {
            mTitle.setText(books.getTitle());
            //mIsFinished.setText(book.getisFinished());
            this.key = key;
        }

    }
    class BooksAdapter extends RecyclerView.Adapter<BookItemView> {
        private List<Book> mBookList;
        private List<String> mKeys;

    public BooksAdapter(List<Book> mBookList, List<String> mKeys) {
        this.mBookList = mBookList;
        this.mKeys = mKeys;
    }

    @Override
    public BookItemView onCreateViewHolder( ViewGroup parent, int viewType) {
        return new BookItemView(parent);
    }

    @Override
    public void onBindViewHolder( BookItemView holder, int position) {
        holder.bind(mBookList.get(position), mKeys.get(position));
    }

    @Override
    public int getItemCount() {
        return mBookList.size();
    }
}


}
