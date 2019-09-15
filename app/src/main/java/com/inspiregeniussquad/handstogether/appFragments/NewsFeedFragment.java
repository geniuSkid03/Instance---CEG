package com.inspiregeniussquad.handstogether.appFragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appActivities.CommentsViewActivity;
import com.inspiregeniussquad.handstogether.appActivities.NewsItemViewActivity;
import com.inspiregeniussquad.handstogether.appActivities.PosterViewActivity;
import com.inspiregeniussquad.handstogether.appAdapters.NewsFeedAdapter;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appData.NewsFeedItems;
import com.inspiregeniussquad.handstogether.appData.Users;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;

public class NewsFeedFragment extends SuperFragment implements SearchView.OnQueryTextListener {

    private RecyclerView newsFeedRv;
    private LinearLayout noNewsTv;

    private boolean isRefreshing = false;

    private NewsFeedAdapter newsFeedAdapter, filteredFeedAdapter;
    private ArrayList<NewsFeedItems> newsFeedItemsArrayList;
    private ShimmerFrameLayout newsLoadingView;

    private Users currentUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        //news items initializations
        newsFeedItemsArrayList = new ArrayList<>();

        currentUser = gson.fromJson(dataStorage.getString(Keys.USER_DATA), Users.class);

        newsFeedAdapter = new NewsFeedAdapter(getContext(), newsFeedItemsArrayList, currentUser);
        newsFeedAdapter.setClickListener(new NewsFeedAdapter.onViewClickedListener() {
            @Override
            public void onViewClicked(int position, ImageView viewOne, ImageView viewTwo) {
                onNewsItemClicked(newsFeedItemsArrayList.get(position), viewOne, viewTwo);
            }

            @Override
            public void onCommentsClicked(int position) {
                onCommentItemClicked(newsFeedItemsArrayList.get(position));
            }

            @Override
            public void onImageClicked(int position, ImageView posterImgIv) {
                onImageItemClicked(newsFeedItemsArrayList.get(position), posterImgIv);
            }

            @Override
            public void onBookmarkClicked(int position, boolean isBookmarked) {
                newsFeedAdapter.setPostAsBookmarked(position);

                if (isBookmarked) {
                    updateAsBookmarked(position);
                } else {
                    removeBookmarked(position);
                }

                updateBookmarkToFirebase(newsFeedItemsArrayList.get(position).isBookmarked());
            }

            @Override
            public void onLikeClicked(int position, boolean isLiked, ImageView likeIv) {

                animateLike(likeIv, isLiked);

//                if (!isLiked) {
//                    updateAsLiked(position);
//                } else {
//                    removeLiked(position);
//                }
//
//                animateLike(likeIv, isLiked);
//
//                ArrayList<String> likedUsersArrayList = newsFeedItemsArrayList.get(position).getLikedUsers();
//
//                if (!isLiked) {
//                    likedUsersArrayList.add(getUserId());
//                    newsFeedItemsArrayList.get(position).setLikedUsers(likedUsersArrayList);
//                } else {
//                    if (likedUsersArrayList != null && likedUsersArrayList.size() > 0) {
//                        for (String likedUser : likedUsersArrayList) {
//                            if (likedUser.equals(getUserId())) {
//                                likedUsersArrayList.remove(likedUser);
//                            }
//                        }
//                        newsFeedItemsArrayList.get(position).setLikedUsers(likedUsersArrayList);
//                    }
//                }
//
//                newsFeedItemsArrayList.get(position).setLiked(!isLiked);
//
//                newsFeedAdapter.notifyItemChanged(position);
            }

            @Override
            public void onShareClicked(int position, ImageView imageView) {
                showSharingView(newsFeedItemsArrayList.get(position), imageView);
            }
        });
    }

    private String getUserId() {
        return dataStorage.getString(Keys.USER_ID);
    }

    private void updateAsBookmarked(int position) {
        String postId = newsFeedItemsArrayList.get(position).getNfId();
    }

    private void removeBookmarked(int position) {

    }

    private void updateAsLiked(int position) {
        AppHelper.print("Updating as liked");
        final String postId = newsFeedItemsArrayList.get(position).getNfId();

        //updating likes count in news item
        DatabaseReference likesCountReference = newsDbReference
                .child(newsFeedItemsArrayList.get(position).getNfId())
                .child("likesCount");
        likesCountReference.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Integer currentValue = mutableData.getValue(Integer.class);
                if (currentValue == null) {
                    mutableData.setValue(1);
                } else {
                    mutableData.setValue(currentValue + 1);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                if (databaseError == null) {
                    //updating liked post to user profile
                    DatabaseReference userDbRef = usersDbReference.child(dataStorage.getString(Keys.USER_ID))
                            .child("likedPosts");
                    userDbRef.child(postId).setValue(postId, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                AppHelper.print("liked post added to user info");
                            } else {
                                AppHelper.print("liked post to user info : ERROR");
                            }
                        }
                    });

                    //updating news liked users
                    DatabaseReference postRef = newsDbReference.child(postId).child("likedUsers");
                    String userId = dataStorage.getString(Keys.USER_ID);
                    postRef.child(userId).setValue(userId, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                AppHelper.print("liked users added to post");
                            } else {
                                AppHelper.print("liked user to post info : ERROR");
                            }
                        }
                    });
                } else {
                    AppHelper.print("ERROR in updateAsLiked");
                }
            }
        });
    }

    private void animateLike(final ImageView likeIv, final boolean isLiked) {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(likeIv, "scaleX", 0.2f, 1f);
        bounceAnimX.setDuration(Keys.ANIMATION_DURATION);
        bounceAnimX.setInterpolator(new BounceInterpolator());
        ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(likeIv, "scaleY", 0.2f, 1f);
        bounceAnimY.setDuration(Keys.ANIMATION_DURATION);
        bounceAnimY.setInterpolator(new BounceInterpolator());
        bounceAnimY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {

            }
        });

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                likeIv.setImageResource(isLiked ? R.drawable.ic_heart_selected : R.drawable.ic_heart_icon);
            }
        });

        animatorSet.play(bounceAnimX).with(bounceAnimY);
        animatorSet.start();
    }

    private void removeLiked(int position) {
        AppHelper.print("Removing liked");

        final String postId = newsFeedItemsArrayList.get(position).getNfId();

        //removing like count in news item
        DatabaseReference likesCountReference = newsDbReference
                .child(postId).child("likesCount");
        likesCountReference.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Long currentValue = mutableData.getValue(Long.class);
                if (currentValue == null) {
                    mutableData.setValue(0);
                } else {
                    if (currentValue != 0) {
                        mutableData.setValue(currentValue - 1);
                    }
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                if (databaseError == null) {
                    String userId = dataStorage.getString(Keys.USER_ID);

                    //removing like from user profile
                    usersDbReference.child(userId).child("likedPosts").child(postId).removeValue(new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            AppHelper.print("Like removed in user profile");
                        }
                    });

                    //removing liked user in news item
                    DatabaseReference postRef = newsDbReference.child(postId).child("likedUsers").child(userId);
                    postRef.removeValue(new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            AppHelper.print("Like removed in news item");
                        }
                    });
                }
            }
        });
    }

    private void updateBookmarkToFirebase(boolean bookmarked) {
        showToast("" + bookmarked);


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_search, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.action_filter_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.search_event));

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                resetSearch();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initNewsFeedView(inflater.inflate(R.layout.fragment_newsfeed, container, false));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        newsFeedRv.setAdapter(newsFeedAdapter);

//        if (!newsFeedAdapter.hasObservers()) {
//            newsFeedAdapter.setHasStableIds(true);
//        }
    }

    private View initNewsFeedView(View view) {
        newsFeedRv = view.findViewById(R.id.news_feed_recycler_view);
        noNewsTv = view.findViewById(R.id.no_news_view);

        newsLoadingView = view.findViewById(R.id.shimmer_view_container);

//        newsFeedRv.setHasFixedSize(true);
//        newsFeedRv.setItemViewCacheSize(20);
//        newsFeedRv.setDrawingCacheEnabled(true);
//        newsFeedRv.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        newsFeedRv.setLayoutManager(linearLayoutManager);

        return view;
    }

    private void onNewsItemClicked(NewsFeedItems newsFeedItem, ImageView imageOne, ImageView imageTwo) {
//        openWithMultipleImageTransition(getContext(), NewsItemViewActivity.class, false,
//                imageOne, imageTwo,  Keys.NEWS_ITEM, gson.toJson(newsFeedItem));

//        openWithImageTransition(getContext(), NewsItemViewActivity.class, false, imageOne,
//                Keys.NEWS_ITEM, gson.toJson(newsFeedItem));

        goTo(getActivity(), NewsItemViewActivity.class, false, Keys.NEWS_ITEM, gson.toJson(newsFeedItem));
    }

    private void onImageItemClicked(NewsFeedItems newsFeedItems, ImageView posterImgIv) {
//        goTo(getContext(), PosterViewActivity.class, false,  Keys.NEWS_ITEM, gson.toJson(newsFeedItems));
        openWithImageTransition(getContext(), PosterViewActivity.class, false, posterImgIv, Keys.NEWS_ITEM, gson.toJson(newsFeedItems));
    }

    private void onCommentItemClicked(NewsFeedItems newsFeedItems) {
        goTo(getContext(), CommentsViewActivity.class, false, Keys.NEWS_ITEM, gson.toJson(newsFeedItems));
    }

    public void refreshNewsFeed() {
        AppHelper.print("Refresh need: " + dataStorage.getBoolean(Keys.HOME_REFRESH_NEED));

        if (!dataStorage.getBoolean(Keys.HOME_REFRESH_NEED)) {
            AppHelper.print("Home refresh not needed");
            return;
        } else {
            dataStorage.saveBoolean(Keys.HOME_REFRESH_NEED, false);
        }

        if (isRefreshing) {
            AppHelper.print("Already refreshing");
            return;
        }

        isRefreshing = true;

        showLoading();
//        showProgress(getString(R.string.loading));

        newsDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    AppHelper.print("News exists and trying to retrieve!");
                    retriveDataFromDb(dataSnapshot);
                } else {
                    AppHelper.print("No news found!");
                    updateUi();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                updateUi();
                AppHelper.print("Db Error while loading news feed: " + databaseError);
                showToast(getString(R.string.db_error));
            }
        });
    }

    private void retriveDataFromDb(DataSnapshot dataSnapshot) {
        Map<String, NewsFeedItems> newsFeedItemsMap = (Map<String, NewsFeedItems>) dataSnapshot.getValue();

        newsFeedItemsArrayList.clear();

        for (Map.Entry<String, NewsFeedItems> teamEntry : newsFeedItemsMap.entrySet()) {

            Map map = (Map) teamEntry.getValue();

            NewsFeedItems newsFeedItems = new NewsFeedItems();
            newsFeedItems.setNfId((String) map.get("nfId"));
            newsFeedItems.settName((String) map.get("tName"));
            newsFeedItems.seteName((String) map.get("eName"));
            newsFeedItems.seteDesc((String) map.get("eDesc"));
            newsFeedItems.seteDate((String) map.get("eDate"));
            newsFeedItems.seteTime((String) map.get("eTime"));
            newsFeedItems.setpTime((String) map.get("pTime"));
            newsFeedItems.setpDate((String) map.get("pDate"));
            newsFeedItems.seteVenue((String) map.get("eVenue"));
            newsFeedItems.setVidUrl((String) map.get("vidUrl"));
            newsFeedItems.setPstrUrl((String) map.get("pstrUrl"));
            newsFeedItems.setLikesCount((long) map.get("likesCount"));
//            newsFeedItems.setCommentCount((long) map.get("commentCount"));
//            newsFeedItems.settLogo((String) map.get("tLogo"));
//
//            ArrayList<String> likedUsers = new ArrayList<>();
//
//            if (teamEntry.getKey() != null) {
//                likedUsers.clear();
//                DataSnapshot dataSnapshot1 = dataSnapshot.child(teamEntry.getKey()).child("likedUsers");
//                if (dataSnapshot1.exists()) {
//                    Map<String, String> stringMap = (Map<String, String>) dataSnapshot1.getValue();
//                    for (Map.Entry<String, String> entry : stringMap.entrySet()) {
//                        likedUsers.add(entry.getValue());
//                    }
//                }
//            }
//
//            newsFeedItems.setLikedUsers(likedUsers);

            newsFeedItemsArrayList.add(newsFeedItems);
        }

        updateUi();
    }

    private void updateUi() {
        hideLoading();

        isRefreshing = false;

        if (newsFeedItemsArrayList.size() != 0) {
            newsFeedAdapter.notifyDataSetChanged();
            newsFeedRv.setAdapter(newsFeedAdapter);
        }

        AppHelper.print("Items in list: " + newsFeedItemsArrayList.size());

        newsFeedRv.setVisibility(newsFeedItemsArrayList.size() == 0 ? View.INVISIBLE : View.VISIBLE);
        noNewsTv.setVisibility(newsFeedItemsArrayList.size() == 0 ? View.VISIBLE : View.INVISIBLE);

        animateWithData(newsFeedRv);
    }

    private void showLoading() {
        newsFeedRv.setVisibility(View.GONE);
        noNewsTv.setVisibility(View.GONE);

        newsLoadingView.setVisibility(View.VISIBLE);
        newsLoadingView.startShimmer();
    }

    private void hideLoading() {

        newsLoadingView.setVisibility(View.GONE);
        newsLoadingView.stopShimmer();
    }

    private void animateWithData(RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.fall_down_layout_anim);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        newsFeedItemsArrayList.clear();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            resetSearch();
            return false;
        }

        AppHelper.print("Search Text: " + newText);

        ArrayList<NewsFeedItems> filteredNewsItem = new ArrayList<>(newsFeedItemsArrayList);
        for (NewsFeedItems newsFeedItems : newsFeedItemsArrayList) {
            if (!newsFeedItems.geteName().toLowerCase().contains(newText.toLowerCase())) {
                filteredNewsItem.remove(newsFeedItems);
            }
        }

        filteredFeedAdapter = new NewsFeedAdapter(getActivity(), filteredNewsItem, currentUser);
        filteredFeedAdapter.notifyDataSetChanged();
        newsFeedRv.setAdapter(filteredFeedAdapter);

        animateWithData(newsFeedRv);

        showFilters();

        return false;
    }

    private void showFilters() {

    }

    private void resetSearch() {
        if (filteredFeedAdapter != null && filteredFeedAdapter.getItemCount() != 0) {
            filteredFeedAdapter.clear();
        }
        refreshNewsFeed();
    }

    private void showSharingView(NewsFeedItems newsFeedItems, ImageView imageView) {

//        imageView.buildDrawingCache();
//        imageView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        imageView.layout(0, 0, imageView.getMeasuredWidth(), imageView.getMeasuredHeight());
//        Bitmap bitmap = imageView.getDrawingCache();

        Bitmap bitmap = null;

        if (bitmap != null) {
            Intent shareIntent;
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + newsFeedItems.geteName() + ".png";
            OutputStream out = null;
            File file = new File(path);
            try {
                out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            path = file.getPath();
            Uri bmpUri = Uri.parse("file://" + path);
            shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check this event\n" + newsFeedItems.geteName());
            shareIntent.setType("image/png");
            startActivity(Intent.createChooser(shareIntent, "Share with"));
        } else {
            showToast(getString(R.string.share_failed));
        }
    }
}
