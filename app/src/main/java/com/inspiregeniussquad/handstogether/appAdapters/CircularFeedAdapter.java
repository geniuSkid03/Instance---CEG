package com.inspiregeniussquad.handstogether.appAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appData.CircularDataItems;
import com.inspiregeniussquad.handstogether.appViews.CircularFeedLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CircularFeedAdapter extends RecyclerView.Adapter<CircularFeedAdapter.CircularFeedView> {

    private ArrayList<CircularDataItems> circularDataItemsArrayList;
    private Context context;

    public CircularFeedAdapter(Context context, ArrayList<CircularDataItems> circularDataItemsArrayList) {
        this.context = context;
        this.circularDataItemsArrayList = circularDataItemsArrayList;
    }

    @NonNull
    @Override
    public CircularFeedView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CircularFeedView(new CircularFeedLayout(context));
    }

    @Override
    public void onBindViewHolder(@NonNull CircularFeedView holder, int position) {
        holder.setCicularFeedView(circularDataItemsArrayList.get(position));
        holder.setPosition(position);
    }

    @Override
    public int getItemCount() {
        return circularDataItemsArrayList.size();
    }

    class CircularFeedView extends RecyclerView.ViewHolder {

        private View itemView;
        private int position;

        private TextView titleTv, descTv, dateTimeTv;
        private ImageView circularImgIv;

        CircularFeedView(View view) {
            super(view);
            itemView = view;
        }

        void setCicularFeedView(CircularDataItems circularDataItems) {
            setView(circularDataItems);
        }

        void setPosition(int position) {
            this.position = position;
        }

        private void setView(CircularDataItems circularDataItems) {
            titleTv = itemView.findViewById(R.id.title);
            dateTimeTv = itemView.findViewById(R.id.posted_date_time);
            descTv = itemView.findViewById(R.id.circular_desc);
            circularImgIv = itemView.findViewById(R.id.circular_image);

            titleTv.setText(circularDataItems.getcTitle());
            dateTimeTv.setText(String.format("%s  %s", circularDataItems.getpDate(), circularDataItems.getpTime()));
            descTv.setText(circularDataItems.getcDesc());

            Picasso.get().load(circularDataItems.getCircularImgPath()).into(circularImgIv);
        }
    }
}