package com.instance.ceg.appAdapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.instance.ceg.R;
import com.instance.ceg.appData.NewsFeedItems;
import com.instance.ceg.appViews.NewsUpdateLayout;

import java.util.ArrayList;

public class NewsUpdatedAdapter extends RecyclerView.Adapter<NewsUpdatedAdapter.NewsFeedView> {

    private ArrayList<NewsFeedItems> newsFeedItemsArrayList;
    private Context context;
    private NewsUpdatedAdapter.onViewClickedListener viewClickedListener;

    public NewsUpdatedAdapter(Context context, ArrayList<NewsFeedItems> newsFeedItemsArrayList) {
        this.context = context;
        this.newsFeedItemsArrayList = newsFeedItemsArrayList;
    }

    public void setClickListener(NewsUpdatedAdapter.onViewClickedListener viewClickedListener) {
        this.viewClickedListener = viewClickedListener;
    }

    @NonNull
    @Override
    public NewsUpdatedAdapter.NewsFeedView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsUpdatedAdapter.NewsFeedView(new NewsUpdateLayout(context));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsFeedView newsFeedItem, int position) {
        newsFeedItem.setNewsFeed(newsFeedItemsArrayList.get(position));
        newsFeedItem.setPosition(position, viewClickedListener);
    }

    @Override
    public int getItemCount() {
        return newsFeedItemsArrayList.size();
    }

    public interface onViewClickedListener {
        void onViewClicked(int position);
    }

    class NewsFeedView extends RecyclerView.ViewHolder {

        private CardView cardView;
        private TextView nameTv, descTv, updatedDateTv;
        private View itemView;
        private int position;
        private NewsUpdatedAdapter.onViewClickedListener viewClickedListener;
        private NewsFeedItems newsFeedItems;

        NewsFeedView(View view) {
            super(view);
            itemView = view;
        }

        void setNewsFeed(NewsFeedItems newsFeedItems) {
            this.newsFeedItems = newsFeedItems;
            setUpView();
        }

        void setPosition(int position, NewsUpdatedAdapter.onViewClickedListener viewClickedListener) {
            this.position = position;
            this.viewClickedListener = viewClickedListener;
        }

        private void setUpView() {
            nameTv = itemView.findViewById(R.id.name);
            descTv = itemView.findViewById(R.id.desc);
            cardView = itemView.findViewById(R.id.update_news_item);
            updatedDateTv = itemView.findViewById(R.id.updated_date);

            nameTv.setText(newsFeedItems.geteName());
            descTv.setText(newsFeedItems.geteDesc());
            updatedDateTv.setText(String.format("%s %s", context.getString(R.string.updated_on), newsFeedItems.getpDate()));

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewClickedListener.onViewClicked(position);
                }
            });
        }
    }
}
