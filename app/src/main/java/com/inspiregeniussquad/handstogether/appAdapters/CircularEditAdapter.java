package com.inspiregeniussquad.handstogether.appAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appData.CircularDataItems;
import com.inspiregeniussquad.handstogether.appViews.NewsUpdateLayout;

import java.util.ArrayList;

public class CircularEditAdapter extends RecyclerView.Adapter<CircularEditAdapter.CircularItems> {

    private Context context;
    private ArrayList<CircularDataItems> circularDataItemsArrayList;
    private CircularEditAdapter.onViewClickedListener clickedListener;

    public CircularEditAdapter(Context context, ArrayList<CircularDataItems> circularDataItemsArrayList) {
        this.context = context;
        this.circularDataItemsArrayList = circularDataItemsArrayList;
    }

    public void setClickedListener(CircularEditAdapter.onViewClickedListener clickedListener) {
        this.clickedListener = clickedListener;
    }

    @NonNull
    @Override
    public CircularItems onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CircularEditAdapter.CircularItems(new NewsUpdateLayout(context));
    }

    @Override
    public void onBindViewHolder(@NonNull CircularItems holder, int position) {
        holder.setCircular(circularDataItemsArrayList.get(position));
        holder.setPosition(position, clickedListener);
    }

    @Override
    public int getItemCount() {
        return circularDataItemsArrayList.size();
    }

    public interface onViewClickedListener {
        void onViewClicked(int position);
    }

    class CircularItems extends RecyclerView.ViewHolder {

        private CardView cardView;
        private TextView nameTv, descTv, updatedDateTv;
        private View itemView;
        private CircularDataItems circularDataItems;
        private CircularEditAdapter.onViewClickedListener clickedListener;
        private int position;

        CircularItems(View view) {
            super(view);
            itemView = view;
        }

        void setCircular(CircularDataItems circularDataItems) {
            this.circularDataItems = circularDataItems;
            setUpView();
        }

        void setPosition(int position, CircularEditAdapter.onViewClickedListener clickedListener) {
            this.position = position;
            this.clickedListener = clickedListener;
        }

        private void setUpView() {
            nameTv = itemView.findViewById(R.id.name);
            descTv = itemView.findViewById(R.id.desc);
            cardView = itemView.findViewById(R.id.update_news_item);
            updatedDateTv = itemView.findViewById(R.id.updated_date);

            nameTv.setText(circularDataItems.getcTitle());
            descTv.setText(circularDataItems.getcDesc());
            updatedDateTv.setText(String.format("%s %s", context.getString(R.string.updated_on), circularDataItems.getpDate()));

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickedListener.onViewClicked(position);
                }
            });
        }

    }
}
