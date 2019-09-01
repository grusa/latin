package ru.ai.latin;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    ArrayList<String> myAdapter;
    public static ClickListener clickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements  View.OnClickListener,View.OnLongClickListener {
        private CardView cardView;
        public ViewHolder(CardView cv) {
            super(cv);
            cardView = cv;
            cv.setOnClickListener(this);
            cv.setOnLongClickListener(this);

        }
        @Override
        public void onClick (View view) {
            clickListener.onItemClick(getAdapterPosition(),view);
        }
        @Override
        public boolean onLongClick (View view) {
            clickListener.onItemLongClick(getAdapterPosition(),view);
            return false;
        }
    }
    public BookAdapter(ArrayList<String> mAdapter) {
        myAdapter = mAdapter;
    }

    @Override
    public BookAdapter.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_books,parent,false);
        return new ViewHolder(cv);

    }
    @Override
    public void onBindViewHolder (ViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        TextView textView = cardView.findViewById(R.id.book);
        textView.setText(myAdapter.get(position));
    }
    @Override
    public int getItemCount() {
        return myAdapter.size();
    }
    public void setOnItemClickListener(ClickListener clickListener) {
        BookAdapter.clickListener = clickListener;
    }
    public interface ClickListener {
        void onItemClick(int position, View view);
        void onItemLongClick(int position, View view);
    }

}
