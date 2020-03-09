package com.instance.ceg.appFragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.instance.ceg.R;
import com.instance.ceg.appActivities.PosterViewActivity;
import com.instance.ceg.appAdapters.CircularFeedAdapter;
import com.instance.ceg.appData.CircularDataItems;
import com.instance.ceg.appData.Keys;
import com.instance.ceg.appData.NewsFeedItems;
import com.instance.ceg.appDialogs.ReadMoreDialog;
import com.instance.ceg.appUtils.AppHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CircularFragment extends SuperFragment implements SearchView.OnQueryTextListener {

    private RecyclerView circularRv;
    private LinearLayout noCircularTv;

    private ArrayList<CircularDataItems> circularDataItemsArrayList;
    private CircularFeedAdapter circularFeedAdapter;
    private ShimmerFrameLayout newsLoadingView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        circularDataItemsArrayList = new ArrayList<>();

        circularFeedAdapter = new CircularFeedAdapter(getContext(), circularDataItemsArrayList,
                new CircularFeedAdapter.CircularCallBack() {
                    @Override
                    public void onItemClicked(int position, ImageView imageView) {
                        onCircularClicked(position, imageView);
                    }

                    @Override
                    public void onReadMoreClick(String description) {
                        showDialog(description);
                    }
                });

        readMoreDialog = new ReadMoreDialog(Objects.requireNonNull(getContext()));
    }

    private ReadMoreDialog readMoreDialog;

    private void showDialog(String description) {
        if (readMoreDialog != null && !readMoreDialog.isShowing())
            readMoreDialog.setAndShow(description);
    }

    private void onCircularClicked(int position, ImageView imageView) {
        if (circularDataItemsArrayList.get(position).getCircularImgPath() != null) {
            openWithImageTransition(getContext(), PosterViewActivity.class, false,
                    imageView, Keys.CIRCULAR_ITEM, gson.toJson(circularDataItemsArrayList.get(position)));
        } else {
            showDownloadOptions(position);
        }
    }

    private void showDownloadOptions(int position) {
        Intent intent = new Intent();
        intent.setDataAndType(Uri.parse(circularDataItemsArrayList.get(position).getPdfPath()), Intent.ACTION_VIEW);
        startActivity(intent);
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
        newsLoadingView = view.findViewById(R.id.shimmer_view_container);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        circularRv.setLayoutManager(linearLayoutManager);

        return view;
    }

    public void refreshCirculars() {
//        showProgress(getString(R.string.loading));
        showLoadingView();

        circularDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    AppHelper.print("News exists and trying to retrive!");
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

        circularDataItemsArrayList.clear();

        for (Map.Entry<String, NewsFeedItems> teamEntry : newsFeedItemsMap.entrySet()) {
            Map map = (Map) teamEntry.getValue();

            CircularDataItems circularDataItems = new CircularDataItems();

            circularDataItems.setcTitle((String) map.get("cTitle"));
            circularDataItems.setcDesc((String) map.get("cDesc"));
            circularDataItems.setCircularImgPath((String) map.get("circularImgPath"));
            circularDataItems.setpDate((String) map.get("pDate"));
            circularDataItems.setpTime((String) map.get("pTime"));
            circularDataItems.setPdfPath((String) map.get("pdfPath"));

            circularDataItemsArrayList.add(circularDataItems);
        }

        for (CircularDataItems items : circularDataItemsArrayList) {
            String[] dateSplit = items.getpDate().split("-");
            String[] timeSplit = items.getpTime().split(":");

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, Integer.valueOf(dateSplit[2]));
            calendar.set(Calendar.MONTH, Integer.valueOf(dateSplit[1]));
            calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(dateSplit[0]));
            calendar.set(Calendar.HOUR, Integer.valueOf(timeSplit[0]));
            calendar.set(Calendar.MINUTE, Integer.valueOf(timeSplit[1]));
            long timestamp = (calendar.getTimeInMillis() / 1000L);

            items.setTimeStamp(timestamp);
        }

        Collections.sort(circularDataItemsArrayList, (t0, t1) ->
                Long.compare(t0.getTimeStamp(), t1.getTimeStamp()));

        updateUi();
    }

    private void updateUi() {
        hideLoading();

        if (circularDataItemsArrayList.size() != 0) {
            circularFeedAdapter.notifyDataSetChanged();
            circularRv.setAdapter(circularFeedAdapter);
        }

        circularRv.setVisibility(circularDataItemsArrayList.size() == 0 ? View.INVISIBLE : View.VISIBLE);
        noCircularTv.setVisibility(circularDataItemsArrayList.size() == 0 ? View.VISIBLE : View.INVISIBLE);

        animateWithData(circularRv);
    }

    private void showLoadingView() {
        circularRv.setVisibility(View.GONE);
        noCircularTv.setVisibility(View.GONE);

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

        circularFeedAdapter = new CircularFeedAdapter(getActivity(), filteredNewsItem, new CircularFeedAdapter.CircularCallBack() {
            @Override
            public void onItemClicked(int position, ImageView imageView) {
                onCircularClicked(position, imageView);
            }

            @Override
            public void onReadMoreClick(String description) {
                showDialog(description);
            }
        });
        circularFeedAdapter.notifyDataSetChanged();
        circularRv.setAdapter(circularFeedAdapter);

        animateWithData(circularRv);

        return false;
    }

    private void resetSearch() {
        refreshCirculars();
    }
}

