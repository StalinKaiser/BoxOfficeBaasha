package com.omglabs.boxofficebaasha;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by anusha on 24/05/16.
 */
public class RowViewAdapter extends RecyclerView.Adapter<RowViewHolder> {
    Context context;
    ArrayList<RowItem> itemsList;

    public RowViewAdapter(Context context, ArrayList<RowItem> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }
    @Override
    public RowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_item, null);
        RowViewHolder viewHolder = new RowViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RowViewHolder holder, int position) {
        RowItem items = itemsList.get(position);
        RowViewHolder.titleTextView.setText(String.valueOf(items.getTitle()));
        RowViewHolder.theatreTextView.setText(String.valueOf(items.getTheatre()));
        RowViewHolder.dateTextView.setText(String.valueOf(items.getDate()));
        RowViewHolder.timeTextView.setText(String.valueOf(items.getTime()));
        RowViewHolder.countTextView.setText(String.valueOf(items.getCount()));
    //    RowViewHolder.imageView.setBackgroundResource(items.getImgIcon());
    }

    @Override
    public int getItemCount() {
        if (itemsList == null) {
            return 0;
        } else {
            return itemsList.size();
        }
    }
}
