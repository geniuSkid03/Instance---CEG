package com.inspiregeniussquad.handstogether.appAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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


    @NonNull
    @Override
    public NewsFeedView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsFeedView(new NewsFeedItemsLayout(context));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsFeedView newsFeedItem, int position) {

        newsFeedItem.bind(position, viewClickedListener);
    }

    @Override
    public long getItemId(int position) {
        return /*Long.parseLong(newsFeedItemsArrayList.get(position).getNfId())*/position;
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
        private TextView nameTv, descTv/*, likeTv, cmntTv, readMoreTv*/;
        private ImageView posterImgIv/*, bookmarkIv, shareIv, likeIv, commentIv*/;
//        private LinearLayout likeLayout, commentLayout, shareLayout, bookmarkLayout;
        private AVLoadingIndicatorView imgloadingIv;
        private CardView cardView;

        private View itemView;

        private int position;

        private onViewClickedListener viewClickedListener;

        NewsFeedView(View view) {
            super(view);
            itemView = view;
        }

        void bind(int position, onViewClickedListener viewClickedListener) {
            this.position = position;
            this.viewClickedListener = viewClickedListener;
            setUpView(newsFeedItemsArrayList.get(position));
        }

        private void setUpView(final NewsFeedItems newsFeedItems) {
            logoCiv = itemView.findViewById(R.id.logo);
            posterImgIv = itemView.findViewById(R.id.event_poster);
            imgloadingIv = itemView.findViewById(R.id.img_loading_view);
            cardView = itemView.findViewById(R.id.post_card);

            nameTv = itemView.findViewById(R.id.name);
            descTv = itemView.findViewById(R.id.desc);
//            readMoreTv = itemView.findViewById(R.id.read_more);
//
//            likeTv = itemView.findViewById(R.id.likes);
//            cmntTv = itemView.findViewById(R.id.comments);
//
//            likeIv = itemView.findViewById(R.id.like);
//            commentIv = itemView.findViewById(R.id.comment);
//            shareIv = itemView.findViewById(R.id.share);
//            bookmarkIv = itemView.findViewById(R.id.bookmark);
//
//            likeLayout = itemView.findViewById(R.id.like_container);
//            commentLayout = itemView.findViewById(R.id.comment_container);
//            shareLayout = itemView.findViewById(R.id.share_container);
//            bookmarkLayout = itemView.findViewById(R.id.bookmark_container);

            nameTv.setText(newsFeedItems.geteName());
            descTv.setText(newsFeedItems.geteDesc());

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewClickedListener.onViewClicked(position, posterImgIv, logoCiv);
                }
            });

//            likeTv.setText(String.format(Locale.getDefault(), "%d", newsFeedItems.getLikesCount()));
//            cmntTv.setText(String.format(Locale.getDefault(), "%d", newsFeedItems.getCommentCount()));

//            ArrayList<String> likedUsers = newsFeedItems.getLikedUsers();
//            if (likedUsers != null && likedUsers.size() > 0) {
//                AppHelper.print("Liked users exists");
//                AppHelper.print("User id: " + getUserId());
//
//                for (int i = 0; i < likedUsers.size(); i++) {
//                    if (likedUsers.get(i).equals(getUserId())) {
//                        AppHelper.print("liked User id: " + likedUsers.get(i));
//                        newsFeedItems.setLiked(true);
//                        likeIv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart_selected));
//                        return;
//                    } else {
//                        likeIv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart_icon));
//                        newsFeedItems.setLiked(false);
//                    }
//                }
//                AppHelper.print("isLiked: " + newsFeedItems.isLiked());
//            } else {
//                AppHelper.print("Liked users doesnt exists");
//                newsFeedItems.setLiked(false);
//                likeIv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart_icon));
//            }

//            likeLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    viewClickedListener.onLikeClicked(position, newsFeedItems.isLiked(), likeIv);
//                }
//            });
//            commentLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    viewClickedListener.onCommentsClicked(position);
//                }
//            });
//            shareLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    viewClickedListener.onShareClicked(position, posterImgIv);
//                }
//            });
//            bookmarkLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    viewClickedListener.onBookmarkClicked(position, newsFeedItems.isBookmarked());
//                }
//            });
//            readMoreTv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    viewClickedListener.onViewClicked(position, posterImgIv, logoCiv);
//                }
//            });
//            posterImgIv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    viewClickedListener.onImageClicked(position, posterImgIv);
//                }
//            });

            Glide.with(context).load(newsFeedItems.getPstrUrl()).into(posterImgIv);
            Glide.with(context).load(newsFeedItems.gettLogo()).into(logoCiv);
        }
    }

    private String getUserId() {
        return dataStorage.getString(Keys.USER_ID);
    }

    public interface onViewClickedListener {
        void onViewClicked(int position, ImageView oneIv, ImageView twoIv);

        void onCommentsClicked(int position);

        void onImageClicked(int position, ImageView posterImgIv);

        void onBookmarkClicked(int position, boolean isBookmarked);

        void onLikeClicked(int position, boolean isLiked, ImageView likeIv);

        void onShareClicked(int position, ImageView posterImgIv);
    }

//    private void setBookmarked(final boolean isBookmarked, final NewsFeedView newsFeedView) {
//        AnimatorSet animatorSet = new AnimatorSet();
//
//        ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(newsFeedView.bookmarkIv, "scaleX", 0.2f, 1f);
//        bounceAnimX.setDuration(Keys.ANIMATION_DURATION);
//        bounceAnimX.setInterpolator(new BounceInterpolator());
//
//        ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(newsFeedView.bookmarkIv, "scaleY", 0.2f, 1f);
//        bounceAnimY.setDuration(Keys.ANIMATION_DURATION);
//        bounceAnimY.setInterpolator(new BounceInterpolator());
//        bounceAnimY.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//                newsFeedView.bookmarkIv.setImageResource(!isBookmarked ? R.drawable.ic_bookmark_icon
//                        : R.drawable.ic_bookmark_done);
//            }
//        });
//
//        animatorSet.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//            }
//        });
//
//        animatorSet.play(bounceAnimX).with(bounceAnimY);
//        animatorSet.start();
//    }
//
//    public void setPostAsLiked(final int position, final boolean isLiked, final ImageView likeIv) {
//        AnimatorSet animatorSet = new AnimatorSet();
//        ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(likeIv, "scaleX", 0.2f, 1f);
//        bounceAnimX.setDuration(Keys.ANIMATION_DURATION);
//        bounceAnimX.setInterpolator(new BounceInterpolator());
//        ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(likeIv, "scaleY", 0.2f, 1f);
//        bounceAnimY.setDuration(Keys.ANIMATION_DURATION);
//        bounceAnimY.setInterpolator(new BounceInterpolator());
//        bounceAnimY.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//                likeIv.setImageResource(!isLiked ? R.drawable.ic_heart_icon
//                        : R.drawable.ic_heart_selected);
//            }
//        });
//
//        animatorSet.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//            }
//        });
//
//        animatorSet.play(bounceAnimX).with(bounceAnimY);
//        animatorSet.start();
//
//        if (isLiked) {
//            newsFeedItemsArrayList.get(position).setLikesCount(newsFeedItemsArrayList.get(position).getLikesCount() - 1);
//        } else {
//            newsFeedItemsArrayList.get(position).setLikesCount(newsFeedItemsArrayList.get(position).getLikesCount() + 1);
//        }
////        notifyItemChanged(position);
//    }

}
