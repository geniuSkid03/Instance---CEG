package com.inspiregeniussquad.handstogether.appAdapters;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.inspiregeniussquad.handstogether.appViews.NewsFeedItemsLayout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.NewsFeedView> {

    private ArrayList<NewsFeedItems> newsFeedItemsArrayList;
    private Context context;
    private onViewClickedListener viewClickedListener;
    private AppDbs appDbs;
    private TeamData[] teamData;
    private ImageLoader imageLoader;


    public NewsFeedAdapter(Context context, ArrayList<NewsFeedItems> newsFeedItemsArrayList) {
        this.context = context;
        this.newsFeedItemsArrayList = newsFeedItemsArrayList;

        appDbs = AppDbs.getTeamDao(context);
        teamData = appDbs.teamDao().loadAll();

        imageLoader = ImageLoader.getInstance();
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

//    public void setPostAsUnliked(int position) {
//
//    }

    class NewsFeedView extends RecyclerView.ViewHolder {

        private com.inspiregeniussquad.handstogether.appViews.CircularImageView logoCiv;
        private TextView nameTv, descTv, likeTv, cmntTv, readMoreTv;
        private ImageView posterImgIv, bookmarkIv, shareIv, likeIv, commentIv, imgloadingIv;
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
            imgloadingIv = itemView.findViewById(R.id.img_loading_view);

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

            File posterImage = DiskCacheUtils.findInCache(newsFeedItems.getPstrUrl(), imageLoader.getDiskCache());
            if (posterImage != null && posterImage.exists()) {
                Picasso.get().load(posterImage).fit().into(posterImgIv, new Callback() {
                    @Override
                    public void onSuccess() {
                        posterImgIv.setVisibility(View.VISIBLE);
                        imgloadingIv.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        posterImgIv.setVisibility(View.GONE);
                        imgloadingIv.setVisibility(View.VISIBLE);
                    }
                });
            } else {
                imageLoader.loadImage(newsFeedItems.getPstrUrl(), new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        posterImgIv.setVisibility(View.GONE);
                        imgloadingIv.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        posterImgIv.setVisibility(View.GONE);
                        imgloadingIv.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        posterImgIv.setVisibility(View.VISIBLE);
                        imgloadingIv.setVisibility(View.GONE);
                        Picasso.get().load(imageUri).fit().into(posterImgIv);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });
            }

            if (appDbs != null) {
                if (teamData != null) {
                    for (TeamData team : teamData) {
                        if (team.getTeamName().equalsIgnoreCase(newsFeedItems.gettName())) {
                            final String logoUrl = team.getTeamLogoUrl();
                            if (logoUrl != null) {
                                File logoImage = DiskCacheUtils.findInCache(logoUrl, imageLoader.getDiskCache());

                                if (logoImage != null && logoImage.exists()) {
                                    Picasso.get().load(logoImage).fit().into(logoCiv, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            logoCiv.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onError(Exception e) {
                                            logoCiv.setVisibility(View.GONE);
                                        }
                                    });
                                } else {
                                    imageLoader.loadImage(logoUrl, new ImageLoadingListener() {
                                        @Override
                                        public void onLoadingStarted(String imageUri, View view) {
                                            logoCiv.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                                        }

                                        @Override
                                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                            Picasso.get().load(imageUri).fit().into(logoCiv);
                                            logoCiv.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onLoadingCancelled(String imageUri, View view) {

                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            }
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
