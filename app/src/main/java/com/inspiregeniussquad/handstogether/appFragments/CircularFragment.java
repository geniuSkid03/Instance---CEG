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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appAdapters.CircularFeedAdapter;
import com.inspiregeniussquad.handstogether.appAdapters.NewsFeedAdapter;
import com.inspiregeniussquad.handstogether.appData.CircularDataItems;
import com.inspiregeniussquad.handstogether.appData.NewsFeedItems;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;

import java.util.ArrayList;
import java.util.Map;

public class CircularFragment extends SuperFragment implements SearchView.OnQueryTextListener {

    private RecyclerView circularRv;
    private TextView noCircularTv;

    private ArrayList<CircularDataItems> circularDataItemsArrayList;
    private CircularFeedAdapter circularFeedAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        circularDataItemsArrayList = new ArrayList<>();

        circularFeedAdapter = new CircularFeedAdapter(getContext(), circularDataItemsArrayList);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        circularRv.setAdapter(circularFeedAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_search, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.action_filter_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.search_ciruclar));

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater.inflate(R.layout.fragment_circular, container, false));
    }

    private View initView(View view) {

        circularRv = view.findViewById(R.id.circular_recycler_view);
        noCircularTv = view.findViewById(R.id.no_circular_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        circularRv.setLayoutManager(linearLayoutManager);

        return view;
    }

    public void refreshCirculars() {
        showProgress(getString(R.string.loading));

        circularDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
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

        circularDataItemsArrayList.clear();

        for (Map.Entry<String, NewsFeedItems> teamEntry : newsFeedItemsMap.entrySet()) {
            Map map = (Map) teamEntry.getValue();

            CircularDataItems circularDataItems = new CircularDataItems();

            circularDataItems.setcTitle((String) map.get("cTitle"));
            circularDataItems.setcDesc((String) map.get("cDesc"));
            circularDataItems.setCircularImgPath((String) map.get("circularImgPath"));
            circularDataItems.setpDate((String) map.get("pDate"));
            circularDataItems.setpTime((String) map.get("pTime"));

            circularDataItemsArrayList.add(circularDataItems);
        }

        updateUi();
    }

    private void updateUi() {
        if (circularDataItemsArrayList.size() != 0) {
            circularFeedAdapter.notifyDataSetChanged();
            circularRv.setAdapter(circularFeedAdapter);
        }

        circularRv.setVisibility(circularDataItemsArrayList.size() == 0 ? View.INVISIBLE : View.VISIBLE);
        noCircularTv.setVisibility(circularDataItemsArrayList.size() == 0 ? View.VISIBLE : View.INVISIBLE);

        cancelProgress();

        animateWithData(circularRv);
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

        circularDataItemsArrayList.clear();
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

        ArrayList<CircularDataItems> filteredNewsItem = new ArrayList<>(circularDataItemsArrayList);
        for (CircularDataItems circularDataItems : circularDataItemsArrayList) {
            if (!circularDataItems.getcTitle().toLowerCase().contains(newText.toLowerCase())) {
                filteredNewsItem.remove(circularDataItems);
            }
        }

        circularFeedAdapter = new CircularFeedAdapter(getActivity(), filteredNewsItem);
        circularFeedAdapter.notifyDataSetChanged();
        circularRv.setAdapter(circularFeedAdapter);

        animateWithData(circularRv);

        return false;
    }

    private void resetSearch() {
        refreshCirculars();
    }
}

