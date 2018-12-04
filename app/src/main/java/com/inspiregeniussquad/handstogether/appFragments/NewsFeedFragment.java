package com.inspiregeniussquad.handstogether.appFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appActivities.CommentsViewActivity;
import com.inspiregeniussquad.handstogether.appActivities.NewsItemViewActivity;
import com.inspiregeniussquad.handstogether.appActivities.PosterViewActivity;
import com.inspiregeniussquad.handstogether.appAdapters.NewsFeedAdapter;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appData.NewsFeedItems;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;

import java.util.ArrayList;
import java.util.Map;

public class NewsFeedFragment extends SuperFragment implements SearchView.OnQueryTextListener {

    private RecyclerView newsFeedRv;
    private TextView noNewsTv;

    private boolean isRefreshing;

    private NewsFeedAdapter newsFeedAdapter, filteredFeedAdapter;
    private ArrayList<NewsFeedItems> newsFeedItemsArrayList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        //news items initializations
        newsFeedItemsArrayList = new ArrayList<>();

        newsFeedAdapter = new NewsFeedAdapter(getContext(), newsFeedItemsArrayList);
        newsFeedAdapter.setClickListener(new NewsFeedAdapter.onViewClickedListener() {
            @Override
            public void onViewClicked(int position) {
                onNewsItemClicked(newsFeedItemsArrayList.get(position));
            }

            @Override
            public void onCommentsClicked(int position) {
                onCommentItemClicked(newsFeedItemsArrayList.get(position));
            }

            @Override
            public void onImageClicked(int position) {
                onImageItemClicked(newsFeedItemsArrayList.get(position));
            }

            @Override
            public void onBookmarkClicked(int position) {
                AppHelper.showToast(getContext(), "Bookmark clicked");
            }

            @Override
            public void onLikeClicked(int position, View itemView) {
                newsFeedAdapter.setPostAsLiked(position, itemView);
                AppHelper.showToast(getContext(), "Like clicked");
            }

            @Override
            public void onShareClicked(int position) {
                AppHelper.showToast(getContext(), "Share clicked");
            }
        });
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
    }

    private View initNewsFeedView(View view) {

        newsFeedRv = view.findViewById(R.id.news_feed_recycler_view);
        noNewsTv = view.findViewById(R.id.no_news_view);

        newsFeedRv.setHasFixedSize(true);
        newsFeedRv.setItemViewCacheSize(20);
        newsFeedRv.setDrawingCacheEnabled(true);
        newsFeedRv.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        newsFeedRv.setLayoutManager(linearLayoutManager);

        return view;
    }

    private void onNewsItemClicked(NewsFeedItems newsFeedItem) {
        goTo(getActivity(), NewsItemViewActivity.class, false, Keys.NEWS_ITEM, gson.toJson(newsFeedItem));
    }

    private void onImageItemClicked(NewsFeedItems newsFeedItems) {
        goTo(getContext(), PosterViewActivity.class, false, Keys.NEWS_ITEM, gson.toJson(newsFeedItems));
    }

    private void onCommentItemClicked(NewsFeedItems newsFeedItems) {
        goTo(getContext(), CommentsViewActivity.class, false, Keys.NEWS_ITEM, gson.toJson(newsFeedItems));
    }

    public void refreshNewsFeed() {
        showProgress(getString(R.string.loading));

        newsDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    AppHelper.print("News exists and trying to retrive!");
                    retriveDataFromDb(dataSnapshot);
                } else {
                    AppHelper.print("No news found!");
                    cancelProgress();
                    updateUi();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                cancelProgress();
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
            newsFeedItems.setLikes((String) map.get("likes"));

            newsFeedItemsArrayList.add(newsFeedItems);
        }

        updateUi();
    }

    private void updateUi() {
        if (newsFeedItemsArrayList.size() != 0) {
            newsFeedAdapter.notifyDataSetChanged();
            newsFeedRv.setAdapter(newsFeedAdapter);
        }

        AppHelper.print("Items in list: " + newsFeedItemsArrayList.size());

        newsFeedRv.setVisibility(newsFeedItemsArrayList.size() == 0 ? View.INVISIBLE : View.VISIBLE);
        noNewsTv.setVisibility(newsFeedItemsArrayList.size() == 0 ? View.VISIBLE : View.INVISIBLE);

        cancelProgress();

        animateWithData(newsFeedRv);
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

        filteredFeedAdapter = new NewsFeedAdapter(getActivity(), filteredNewsItem);
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
}
