package com.inspiregeniussquad.handstogether.appAdapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appData.DataStorage;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appData.NewsFeedItems;
import com.inspiregeniussquad.handstogether.appData.Users;
import com.inspiregeniussquad.handstogether.appStorage.AppDbs;
import com.inspiregeniussquad.handstogether.appStorage.TeamData;
import com.inspiregeniussquad.handstogether.appViews.NewsFeedItemsLayout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Locale;

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.NewsFeedView> {

    private ArrayList<NewsFeedItems> newsFeedItemsArrayList;
    private Context context;
    private onViewClickedListener viewClickedListener;
    private AppDbs appDbs;
    private TeamData[] teamData;
    private ImageLoader imageLoader;

    private DataStorage dataStorage;

    private Users users;

    private DatabaseReference userDatabaseReference, likePostsReference;
    private ArrayList<String> likedPostsArrayList;

    public NewsFeedAdapter(Context context, ArrayList<NewsFeedItems> newsFeedItemsArrayList, Users users) {
        this.context = context;
        this.newsFeedItemsArrayList = newsFeedItemsArrayList;
        this.users = users;

        appDbs = AppDbs.getTeamDao(context);
        teamData = appDbs.teamDao().loadAll();

        imageLoader = ImageLoader.getInstance();

        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child(Keys.TABLE_USER);
        likePostsReference = userDatabaseReference.child(users.getMobile()).child(Keys.LIKED_POSTS);

        likedPostsArrayList = new ArrayList<>();

        dataStorage = new DataStorage(context);
    }

    public void setClickListener(onViewClickedListener viewClickedListener) {
        this.viewClickedListener = viewClickedListener;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
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

        if (newsFeedItemsArrayList.get(position).isLiked()) {
            setLiked(true, newsFeedItem);
        } else {
            setLiked(false, newsFeedItem);
        }

        if (newsFeedItemsArrayList.get(position).isBookmarked()) {
            setBookmarked(true, newsFeedItem);
        } else {
            setBookmarked(false, newsFeedItem);
        }
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(newsFeedItemsArrayList.get(position).getNfId());
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

    private void setBookmarked(final boolean isBookmarked, final NewsFeedView newsFeedView) {
        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(newsFeedView.bookmarkIv, "scaleX", 0.2f, 1f);
        bounceAnimX.setDuration(Keys.ANIMATION_DURATION);
        bounceAnimX.setInterpolator(new BounceInterpolator());

        ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(newsFeedView.bookmarkIv, "scaleY", 0.2f, 1f);
        bounceAnimY.setDuration(Keys.ANIMATION_DURATION);
        bounceAnimY.setInterpolator(new BounceInterpolator());
        bounceAnimY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                newsFeedView.bookmarkIv.setImageResource(!isBookmarked ? R.drawable.ic_bookmark_icon
                        : R.drawable.ic_bookmark_done);
            }
        });

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
            }
        });

        animatorSet.play(bounceAnimX).with(bounceAnimY);
        animatorSet.start();
    }


    private void setLiked(final boolean isLiked, final NewsFeedView newsFeedItem) {
        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(newsFeedItem.likeIv, "scaleX", 0.2f, 1f);
        bounceAnimX.setDuration(Keys.ANIMATION_DURATION);
        bounceAnimX.setInterpolator(new BounceInterpolator());

        ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(newsFeedItem.likeIv, "scaleY", 0.2f, 1f);
        bounceAnimY.setDuration(Keys.ANIMATION_DURATION);
        bounceAnimY.setInterpolator(new BounceInterpolator());
        bounceAnimY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                newsFeedItem.likeIv.setImageResource(!isLiked ? R.drawable.ic_heart_icon
                        : R.drawable.ic_heart_selected);
            }
        });

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
            }
        });

        animatorSet.play(bounceAnimX).with(bounceAnimY);
        animatorSet.start();
    }

    public void setPostAsLiked(int position) {
        if (newsFeedItemsArrayList.get(position).isLiked()) {
            newsFeedItemsArrayList.get(position).setLiked(false);
            newsFeedItemsArrayList.get(position).setLikesCount(newsFeedItemsArrayList.get(position).getLikesCount() - 1);
        } else {
            newsFeedItemsArrayList.get(position).setLiked(true);
            newsFeedItemsArrayList.get(position).setLikesCount(newsFeedItemsArrayList.get(position).getLikesCount() + 1);
        }

        notifyItemChanged(position);
    }

    public void setPostAsBookmarked(int position) {
        if (newsFeedItemsArrayList.get(position).isBookmarked()) {
            newsFeedItemsArrayList.get(position).setBookmarked(false);
        } else {
            newsFeedItemsArrayList.get(position).setBookmarked(true);
        }
        notifyItemChanged(position);
    }

    class NewsFeedView extends RecyclerView.ViewHolder {

        private com.inspiregeniussquad.handstogether.appViews.CircularImageView logoCiv;
        private TextView nameTv, descTv, likeTv, cmntTv, readMoreTv;
        private ImageView posterImgIv, bookmarkIv, shareIv, likeIv, commentIv;
        private LinearLayout likeLayout, commentLayout, shareLayout, bookmarkLayout;
        private AVLoadingIndicatorView imgloadingIv;

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
            likeTv.setText(String.format(Locale.getDefault(), "%d", newsFeedItems.getLikesCount()));
            cmntTv.setText(String.format(Locale.getDefault(), "%d", newsFeedItems.getCommentCount()));

            if(newsFeedItems.getLikedUsers() != null) {
                for (int i = 0; i < newsFeedItems.getLikedUsers().size(); i++) {
                    if (newsFeedItems.getLikedUsers().get(i).equals(getUserId())) {
                        likeIv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart_selected));
                    } else {
                        likeIv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart_icon));
                    }
                }
            }

//            if (users != null && users.getLikedPosts() != null) {
//                if (users.getLikedPosts().size() > 0) {
//                    for (int i = 0; i < users.getLikedPosts().size(); i++) {
//                        String likedPostId = users.getLikedPosts().get(i);
//                        if (likedPostId.equalsIgnoreCase(newsFeedItemsArrayList.get(i).getNfId())) {
//                            likeIv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart_selected));
//                        } else {
//                            likeIv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart_icon));
//                        }
//                    }
//                } else {
//                    likeIv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart_icon));
//                }
//            }

            if (users != null && users.getBookmarkedPosts() != null) {
                if (users.getBookmarkedPosts().size() > 0) {
                    for (int i = 0; i < users.getBookmarkedPosts().size(); i++) {
                        if (users.getBookmarkedPosts().get(i).contains(newsFeedItemsArrayList.get(i).getNfId())) {
                            bookmarkIv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_bookmark_done));
                        } else {
                            bookmarkIv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_bookmark_icon));
                        }
                    }
                } else {
                    bookmarkIv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_bookmark_icon));
                }
            }

            likeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewClickedListener.onLikeClicked(position, newsFeedItems.isLiked());
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
                    viewClickedListener.onShareClicked(position, posterImgIv);
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
                    viewClickedListener.onImageClicked(position, posterImgIv);
                }
            });

            Glide.with(context).load(newsFeedItems.getPstrUrl()).into(posterImgIv);

//            File posterImage = DiskCacheUtils.findInCache(, imageLoader.getDiskCache());
//            if (posterImage != null && posterImage.exists()) {
//                Picasso.get().load(posterImage).fit().into(posterImgIv, new Callback() {
//                    @Override
//                    public void onSuccess() {
//                        posterImgIv.setVisibility(View.VISIBLE);
//                        imgloadingIv.hide();
//                        imgloadingIv.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//                        posterImgIv.setVisibility(View.GONE);
//                        imgloadingIv.setVisibility(View.VISIBLE);
//                        imgloadingIv.show();
//                    }
//                });
//            } else {
//                imageLoader.loadImage(newsFeedItems.getPstrUrl(), new ImageLoadingListener() {
//                    @Override
//                    public void onLoadingStarted(String imageUri, View view) {
//                        posterImgIv.setVisibility(View.GONE);
//                        imgloadingIv.setVisibility(View.VISIBLE);
//                        imgloadingIv.show();
//                    }
//
//                    @Override
//                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                        posterImgIv.setVisibility(View.GONE);
//                        imgloadingIv.show();
//                        imgloadingIv.setVisibility(View.VISIBLE);
//                    }
//
//                    @Override
//                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                        posterImgIv.setVisibility(View.VISIBLE);
//                        imgloadingIv.setVisibility(View.GONE);
//                        imgloadingIv.hide();
//                        Picasso.get().load(imageUri).fit().into(posterImgIv);
//                        newsFeedItems.setPosterUri(imageUri);
//                    }
//
//                    @Override
//                    public void onLoadingCancelled(String imageUri, View view) {
//
//                    }
//                });
//            }

            if (appDbs != null) {
                if (teamData != null) {
                    for (TeamData team : teamData) {
                        if (team.getTeamName().equalsIgnoreCase(newsFeedItems.gettName())) {
                            final String logoUrl = team.getTeamLogoUrl();
                            if (logoUrl != null) {

                                Glide.with(context).load(logoUrl).into(logoCiv);

//                                File logoImage = DiskCacheUtils.findInCache(logoUrl, imageLoader.getDiskCache());
//
//                                if (logoImage != null && logoImage.exists()) {
//                                    Picasso.get().load(logoImage).fit().into(logoCiv, new Callback() {
//                                        @Override
//                                        public void onSuccess() {
//                                            logoCiv.setVisibility(View.VISIBLE);
//                                        }
//
//                                        @Override
//                                        public void onError(Exception e) {
//                                            logoCiv.setVisibility(View.GONE);
//                                        }
//                                    });
//                                } else {
//                                    imageLoader.loadImage(logoUrl, new ImageLoadingListener() {
//                                        @Override
//                                        public void onLoadingStarted(String imageUri, View view) {
//                                            logoCiv.setVisibility(View.GONE);
//                                        }
//
//                                        @Override
//                                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//
//                                        }
//
//                                        @Override
//                                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                                            Picasso.get().load(imageUri).fit().into(logoCiv);
//                                            logoCiv.setVisibility(View.VISIBLE);
//
//                                            newsFeedItems.setPstrUrl(imageUri);
//                                        }
//
//                                        @Override
//                                        public void onLoadingCancelled(String imageUri, View view) {
//
//                                        }
//                                    });
//                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public String getUserId() {
        return dataStorage.getString(Keys.USER_ID);
    }

    public interface onViewClickedListener {
        void onViewClicked(int position);

        void onCommentsClicked(int position);

        void onImageClicked(int position, ImageView posterImgIv);

        void onBookmarkClicked(int position);

        void onLikeClicked(int position, boolean isLiked);

        void onShareClicked(int position, ImageView posterImgIv);
    }

}
