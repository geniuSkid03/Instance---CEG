package com.inspiregeniussquad.handstogether.appFragments;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appAdapters.NewsUpdatedAdapter;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appData.NewsFeedItems;
import com.inspiregeniussquad.handstogether.appInterfaces.FragmentInterfaceListener;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;

import java.util.ArrayList;
import java.util.Map;

public class EditNewsFragment extends SuperFragment {

    private SwipeRefreshLayout refreshUpdatedNewsLayout;
    private RecyclerView updatedNewsRv;
    private TextView noNewsTv;

    private ArrayList<NewsFeedItems> newsFeedItemsArrayList;
    private NewsUpdatedAdapter newsUpdatedAdapter;

    private FragmentInterfaceListener fragmentInterfaceListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        newsFeedItemsArrayList = new ArrayList<>();
        newsUpdatedAdapter = new NewsUpdatedAdapter(getContext(), newsFeedItemsArrayList);
        newsUpdatedAdapter.setClickListener(new NewsUpdatedAdapter.onViewClickedListener() {
            @Override
            public void onViewClicked(int position) {
                showOptionsFor(position);
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updatedNewsRv.setAdapter(newsUpdatedAdapter);
    }

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater.inflate(R.layout.edit_news_fragment, container, false));
    }

    private View initView(View view) {
        refreshUpdatedNewsLayout = view.findViewById(R.id.swipe_refresh_newsfeed);
        updatedNewsRv = view.findViewById(R.id.updated_news_rv);
        noNewsTv = view.findViewById(R.id.no_news_updated);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        updatedNewsRv.setLayoutManager(linearLayoutManager);

        refreshUpdatedNewsLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPostedNews();
            }
        });

        return view;
    }

    private void showOptionsFor(final int position) {
        View optionsView = LayoutInflater.from(getContext()).inflate(R.layout.news_edit_options, null);
        TextView editTv = optionsView.findViewById(R.id.edit);
        TextView deleteTv = optionsView.findViewById(R.id.delete);

        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setView(optionsView);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.delete:
                        alertDialog.dismiss();
                        proceedToDelete(position);
                        break;
                    case R.id.edit:
                        alertDialog.dismiss();
                        AppHelper.print("Edit Clicked");
                        break;
                }
            }
        };

        editTv.setOnClickListener(clickListener);
        deleteTv.setOnClickListener(clickListener);

        alertDialog.setCancelable(true);
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    private void proceedToDelete(int position) {
        AppHelper.print("Trying to delete: " + position);
        showProgress(getString(R.string.deleting_news));

        String eventName = newsFeedItemsArrayList.get(position).geteName();

        Query deleteQuery = newsDbReference.orderByChild("eName").equalTo(eventName);
        deleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot deleteSnapshot : dataSnapshot.getChildren()) {
                    deleteSnapshot.getRef().removeValue();
                }
                refreshPostedNews();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                cancelProgress();
                AppHelper.print("Db error while trying to delete: " + databaseError.getMessage());
                showToast(getString(R.string.db_error));
            }
        });
    }

    public void refreshPostedNews() {
        if (refreshUpdatedNewsLayout != null && refreshUpdatedNewsLayout.isRefreshing()) {
            refreshUpdatedNewsLayout.setRefreshing(false);
        }

        showProgress(getString(R.string.loading_your_news));

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
        if (!dataStorage.isDataAvailable(Keys.MOBILE)) {
            cancelProgress();
            AppHelper.print("Mobile number of user unavailable");
            showToast("Mobile number not found, cannot load data!");
            return;
        }

        String mobileNumber = dataStorage.getString(Keys.MOBILE);
        AppHelper.print("Mobile: " + mobileNumber);

        Map<String, NewsFeedItems> newsFeedItemsMap = (Map<String, NewsFeedItems>) dataSnapshot.getValue();

        newsFeedItemsArrayList.clear();

        for (Map.Entry<String, NewsFeedItems> teamEntry : newsFeedItemsMap.entrySet()) {
            Map map = (Map) teamEntry.getValue();

            if (mobileNumber.equals(map.get("postedBy"))) {
                NewsFeedItems newsFeedItems = new NewsFeedItems();
                newsFeedItems.settName((String) map.get("tName"));
                newsFeedItems.seteName((String) map.get("eName"));
                newsFeedItems.seteDate((String) map.get("eDate"));
                newsFeedItems.seteTime((String) map.get("eTime"));
                newsFeedItems.setpTime((String) map.get("pTime"));
                newsFeedItems.setpDate((String) map.get("pDate"));
                newsFeedItems.seteVenue((String) map.get("eVenue"));
                newsFeedItems.setVidUrl((String) map.get("vidUrl"));
                newsFeedItems.setPstrUrl((String) map.get("pstrUrl"));

                newsFeedItemsArrayList.add(newsFeedItems);
            }

            updateUi();
        }
    }

    private void updateUi() {
        cancelProgress();

        if (newsFeedItemsArrayList.size() != 0) {
            newsUpdatedAdapter.notifyDataSetChanged();
            updatedNewsRv.setAdapter(newsUpdatedAdapter);
            AppHelper.print("News feed items size: " + newsFeedItemsArrayList.size());
        }

        updatedNewsRv.setVisibility(newsFeedItemsArrayList.size() == 0 ? View.INVISIBLE : View.VISIBLE);
        noNewsTv.setVisibility(newsFeedItemsArrayList.size() == 0 ? View.VISIBLE : View.INVISIBLE);
    }

    public void setFragmentInterfaceListener(FragmentInterfaceListener fragmentInterfaceListener) {
        this.fragmentInterfaceListener = fragmentInterfaceListener;
    }

    public FragmentInterfaceListener getFragmentInterfaceListener() {
        return fragmentInterfaceListener;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            if (getFragmentInterfaceListener() != null) {
                getFragmentInterfaceListener().refreshFragments();
            }
        }
    }
}
