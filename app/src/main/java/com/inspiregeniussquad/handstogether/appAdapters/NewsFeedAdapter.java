package com.inspiregeniussquad.handstogether.appAdapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appData.NewsFeedItems;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;
import com.inspiregeniussquad.handstogether.appViews.NewsFeedItemsLayout;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.NewsFeedView> {

    private ArrayList<NewsFeedItems> newsFeedItemsArrayList;
    private Context context;
    private onViewClickedListener viewClickedListener;

    public NewsFeedAdapter(Context context, ArrayList<NewsFeedItems> newsFeedItemsArrayList) {
        this.context = context;
        this.newsFeedItemsArrayList = newsFeedItemsArrayList;
    }

    public void setClickListener(onViewClickedListener viewClickedListener) {
        this.viewClickedListener = viewClickedListener;
    }

    @NonNull
    @Override
    public NewsFeedView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsFeedView(new NewsFeedItemsLayout(context));
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

    class NewsFeedView extends RecyclerView.ViewHolder {

        private CircularImageView logoCiv;
        private TextView nameTv, descTv, timeTv, dateTv, readMoreTv;
        private ImageView posterImgIv;
        private View itemView;
        private int position;
        private onViewClickedListener viewClickedListener;
        private NewsFeedItems newsFeedItems;

        NewsFeedView(View view) {
            super(view);
            itemView = view;
        }

        void setNewsFeed(NewsFeedItems newsFeedItems) {
            this.newsFeedItems = newsFeedItems;
            setUpView();
        }

        void setPosition(int position, onViewClickedListener viewClickedListener) {
            this.position = position;
            this.viewClickedListener = viewClickedListener;
        }

        private void setUpView() {
            logoCiv = itemView.findViewById(R.id.logo);
            posterImgIv = itemView.findViewById(R.id.event_poster);
            nameTv = itemView.findViewById(R.id.name);
            descTv = itemView.findViewById(R.id.desc);
            timeTv = itemView.findViewById(R.id.time);
            dateTv = itemView.findViewById(R.id.date);
            readMoreTv = itemView.findViewById(R.id.read_more);

            nameTv.setText(newsFeedItems.geteName());
            descTv.setText(newsFeedItems.geteDesc());
            timeTv.setText(newsFeedItems.getpTime());
            dateTv.setText(newsFeedItems.geteDate());
            readMoreTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewClickedListener.onViewClicked(position);
                }
            });

//            logoCiv.setImageBitmap(AppHelper.getBitMapFromByteArray(newsFeedItems.getLogoDrawable()));
//            posterImgIv.setImageBitmap(AppHelper.getBitMapFromByteArray(newsFeedItems.getPosterDrawable()));

//            Picasso.get().load(newsFeedItems.getT()).into(logoCiv);
            Picasso.get().load(newsFeedItems.getPstrUrl()).into(posterImgIv);
        }
    }

    public interface onViewClickedListener {
        void onViewClicked(int position);
    }
}
