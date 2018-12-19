package com.inspiregeniussquad.handstogether.appAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appData.NewsFeedItems;
import com.inspiregeniussquad.handstogether.appData.Users;
import com.inspiregeniussquad.handstogether.appStorage.AppDbs;
import com.inspiregeniussquad.handstogether.appStorage.TeamData;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;
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

        if (newsFeedItemsArrayList.get(position).isLiked()) {
            newsFeedItem.likeIv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart_selected));
        } else {
            newsFeedItem.likeIv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart_icon));
        }

        if (newsFeedItemsArrayList.get(position).isBookmarked()) {
            newsFeedItem.bookmarkIv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_bookmark_done));
        } else {
            newsFeedItem.bookmarkIv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_bookmark_icon));
        }
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

    public void setPostAsLiked(int position) {
        if (newsFeedItemsArrayList.get(position).isLiked()) {
            newsFeedItemsArrayList.get(position).setLiked(false);
            updateToFirebaseAsDisLiked(newsFeedItemsArrayList.get(position));
        } else {
            newsFeedItemsArrayList.get(position).setLiked(true);
            updateToFirebaseAsLiked(newsFeedItemsArrayList.get(position));
        }
        notifyItemChanged(position);
    }

    public void setPostAsBookmarked(int position) {
        if (newsFeedItemsArrayList.get(position).isBookmarked()) {
            newsFeedItemsArrayList.get(position).setBookmarked(false);
            updateToFirebaseAsDisLiked(newsFeedItemsArrayList.get(position));
        } else {
            newsFeedItemsArrayList.get(position).setBookmarked(true);
            updateToFirebaseAsLiked(newsFeedItemsArrayList.get(position));
        }
        notifyItemChanged(position);
    }

    private void updateToFirebaseAsLiked(final NewsFeedItems newsFeedItems) {
        likePostsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                likedPostsArrayList.clear();

                if(!dataSnapshot.exists()) {
                    AppHelper.print("Like datasnapshot doesnt exists!");
                    return;
                }

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    AppHelper.print("Liked posts: "+ds.getValue(String.class));
                    likedPostsArrayList.add(ds.getValue(String.class));
                }

                likedPostsArrayList.add(newsFeedItems.getNfId());

                userDatabaseReference.child(users.getMobile()).child(Keys.LIKED_POSTS)
                        .setValue(likedPostsArrayList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        if(newsFeedItems.getNfId() != null) {
//            String key = likePostsReference.getKey();
//            Map<String, Object> map = new HashMap<>();
//            map.put(key, newsFeedItems.getNfId());
//            likePostsReference.updateChildren(map);
//            AppHelper.print("Added to liked posts");
//        } else {
//            AppHelper.print("Cannot add to liked posts, newsfeed items id empty");
//        }
    }

    private void updateToFirebaseAsDisLiked(NewsFeedItems newsFeedItems) {
        if (newsFeedItems.getNfId() != null) {
            likePostsReference.orderByKey().equalTo(newsFeedItems.getNfId())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String key = dataSnapshot.getKey();
                            dataSnapshot.getRef().removeValue();
                            AppHelper.print("Disliked success!");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            AppHelper.print("Database error while disliking: " + databaseError.getMessage());
                        }
                    });
        } else {
            AppHelper.print("Cannot dislike posts, news feed items id empty");
        }
    }

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

            if (users.getLikedPosts() != null) {
                if (users.getLikedPosts().size() > 0) {
                    for (int i = 0; i < users.getLikedPosts().size(); i++) {
                        String likedPostId = users.getLikedPosts().get(i);
                        if (likedPostId.equalsIgnoreCase(newsFeedItemsArrayList.get(i).getNfId())) {
                            likeIv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart_selected));
                        } else {
                            likeIv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart_icon));
                        }
                    }
                } else {
                    likeIv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart_icon));
                }
            }

            if (users.getBookmarkedPosts() != null) {
                if (users.getBookmarkedPosts().size() > 0) {
                    for (int i = 0; i < users.getBookmarkedPosts().size(); i++) {
                        if (users.getBookmarkedPosts().get(i)
                                .contains(newsFeedItemsArrayList.get(i).getNfId())) {
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
                        newsFeedItems.setPosterUri(Uri.parse(imageUri));
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

                                            newsFeedItems.setPstrUrl(imageUri);
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

        void onImageClicked(int position, ImageView posterImgIv);

        void onBookmarkClicked(int position);

        void onLikeClicked(int position, View itemView);

        void onShareClicked(int position, ImageView posterImgIv);
    }
}
