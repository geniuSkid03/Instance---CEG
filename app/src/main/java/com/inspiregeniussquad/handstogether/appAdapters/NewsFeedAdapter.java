package com.inspiregeniussquad.handstogether.appAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appData.NewsFeedItems;
import com.inspiregeniussquad.handstogether.appStorage.AppDbs;
import com.inspiregeniussquad.handstogether.appStorage.TeamData;
import com.inspiregeniussquad.handstogether.appUtils.TeamDataHelper;
import com.inspiregeniussquad.handstogether.appViews.NewsFeedItemsLayout;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.NewsFeedView> {

    private ArrayList<NewsFeedItems> newsFeedItemsArrayList;
    private Context context;
    private onViewClickedListener viewClickedListener;
    private AppDbs appDbs;

    public NewsFeedAdapter(Context context, ArrayList<NewsFeedItems> newsFeedItemsArrayList) {
        this.context = context;
        this.newsFeedItemsArrayList = newsFeedItemsArrayList;
        appDbs = AppDbs.getTeamDao(context);
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

    public void clear() {
        final int size = newsFeedItemsArrayList.size();
        newsFeedItemsArrayList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void setPostAsLiked(int position, View itemView) {
        NewsFeedItems newsFeedItems = newsFeedItemsArrayList.get(position);
        newsFeedItems.setLiked(true);

//        onBindViewHolder((View) itemView, position);
    }

    public void setPostAsUnliked(int position) {

    }

    class NewsFeedView extends RecyclerView.ViewHolder {

        private com.inspiregeniussquad.handstogether.appViews.CircularImageView logoCiv;
        private TextView nameTv, descTv, likeTv, cmntTv, readMoreTv;
        private ImageView posterImgIv, bookmarkIv, shareIv, likeIv, commentIv;
        private LinearLayout likeLayout, commentLayout, shareLayout, bookmarkLayout;

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
            readMoreTv = itemView.findViewById(R.id.read_more);

            likeTv = itemView.findViewById(R.id.likes);
            cmntTv = itemView.findViewById(R.id.comments);

            likeIv = itemView.findViewById(R.id.like);
            commentIv = itemView.findViewById(R.id.comment);
            shareIv = itemView.findViewById(R.id.share);
            bookmarkIv = itemView.findViewById(R.id.bookmark);

            likeLayout = itemView.findViewById(R.id.like_container);
            commentLayout = itemView.findViewById(R.id.comment_container);
            shareLayout = itemView.findViewById(R.id.share_container);
            bookmarkLayout = itemView.findViewById(R.id.bookmark_container);

            nameTv.setText(newsFeedItems.geteName());
            descTv.setText(newsFeedItems.geteDesc());
            likeTv.setText(newsFeedItems.getLikes());
            cmntTv.setText(newsFeedItems.getCommentCount());

            likeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewClickedListener.onLikeClicked(position, itemView);
                }
            });
            commentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewClickedListener.onCommentsClicked(position);
                }
            });
            shareLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewClickedListener.onShareClicked(position);
                }
            });
            bookmarkLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewClickedListener.onBookmarkClicked(position);
                }
            });
            readMoreTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewClickedListener.onViewClicked(position);
                }
            });
            posterImgIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewClickedListener.onImageClicked(position);
                }
            });

            TeamData teamData = appDbs.teamDao().getTeamInfo(newsFeedItems.gettName());
            if(teamData != null) {
                Picasso.get().load(teamData.getTeamLogoUrl()).into(logoCiv);
            }

            Picasso.get().load(newsFeedItems.getPstrUrl()).into(posterImgIv);
        }
    }

    public interface onViewClickedListener {
        void onViewClicked(int position);

        void onCommentsClicked(int position);

        void onImageClicked(int position);

        void onBookmarkClicked(int position);

        void onLikeClicked(int position, View itemView);

        void onShareClicked(int position);
    }
}
