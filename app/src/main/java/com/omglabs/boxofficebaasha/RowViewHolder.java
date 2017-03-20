package com.omglabs.boxofficebaasha;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by anusha on 24/05/16.
 */
public class RowViewHolder extends RecyclerView.ViewHolder{

    //static ImageView imageView;
    static TextView titleTextView;
    static TextView theatreTextView;
    static TextView dateTextView;
    static TextView timeTextView;
    static TextView countTextView;

    public RowViewHolder(View view) {
        super(view);
        this.titleTextView = (TextView) view.findViewById(R.id.title);
        this.theatreTextView = (TextView) view.findViewById(R.id.theatre);
        this.dateTextView = (TextView) view.findViewById(R.id.date);
        this.timeTextView = (TextView) view.findViewById(R.id.time);
        this.countTextView = (TextView) view.findViewById(R.id.count);
        //this.imageView = (ImageView) view.findViewById(R.id.image);
    }
}
