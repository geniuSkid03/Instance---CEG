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
import com.inspiregeniussquad.handstogether.appAdapters.CircularEditAdapter;
import com.inspiregeniussquad.handstogether.appData.CircularDataItems;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appData.NewsFeedItems;
import com.inspiregeniussquad.handstogether.appInterfaces.FragmentInterfaceListener;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;

import java.util.ArrayList;
import java.util.Map;

public class ManageCircularFragment extends SuperFragment {

    private SwipeRefreshLayout refreshCircularLayout;
    private RecyclerView updatedCircularRv;
    private TextView noUpdatedTv;

    private ArrayList<CircularDataItems> circularDataItemsArrayList;
    private CircularEditAdapter circularDataAdapter;

    private FragmentInterfaceListener fragmentInterfaceListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        circularDataItemsArrayList = new ArrayList<>();

        circularDataAdapter = new CircularEditAdapter(getContext(), circularDataItemsArrayList);
        circularDataAdapter.setClickedListener(new CircularEditAdapter.onViewClickedListener() {
            @Override
            public void onViewClicked(int position) {
                onItemClicked(position);
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updatedCircularRv.setAdapter(circularDataAdapter);
    }

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater.inflate(R.layout.manage_circular_fragment, container, false));
    }

    private View initView(View view) {

        refreshCircularLayout = view.findViewById(R.id.refresh_updated_circular);
        updatedCircularRv = view.findViewById(R.id.updated_news_rv);
        noUpdatedTv = view.findViewById(R.id.no_updated_circular);

        refreshCircularLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshCirculars();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        updatedCircularRv.setLayoutManager(linearLayoutManager);

        return view;
    }

    private void onItemClicked(final int position) {
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

        String title = circularDataItemsArrayList.get(position).getcTitle();

        Query deleteQuery = circularDbReference.orderByChild("cTitle").equalTo(title);
        deleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot deleteSnapshot : dataSnapshot.getChildren()) {
                    deleteSnapshot.getRef().removeValue();
                }
                refreshCirculars();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                cancelProgress();
                AppHelper.print("Db error while trying to delete: " + databaseError.getMessage());
                showToast(getString(R.string.db_error));
            }
        });
    }

    public void refreshCirculars() {
        if(refreshCircularLayout != null && refreshCircularLayout.isRefreshing()){
            refreshCircularLayout.setRefreshing(false);
        }

        showProgress(getString(R.string.loading_circular_updated_by_you));

        circularDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    AppHelper.print("Circular exists and trying to retrive!");
                    retriveDataFromDb(dataSnapshot);
                } else {
                    AppHelper.print("No circulars found!");
                    cancelProgress();
                    updateUi();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                cancelProgress();
                updateUi();
                AppHelper.print("Db Error while loading Circulars: " + databaseError);
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

        circularDataItemsArrayList.clear();

        for (Map.Entry<String, NewsFeedItems> teamEntry : newsFeedItemsMap.entrySet()) {
            Map map = (Map) teamEntry.getValue();

            if (mobileNumber.equals(map.get("postedBy"))) {
                CircularDataItems circularDataItems = new CircularDataItems();

                circularDataItems.setcTitle((String) map.get("cTitle"));
                circularDataItems.setcDesc((String) map.get("cDesc"));
                circularDataItems.setCircularImgPath((String) map.get("circularImgPath"));
                circularDataItems.setpDate((String) map.get("pDate"));

                circularDataItemsArrayList.add(circularDataItems);
            }

            updateUi();
        }
    }

    private void updateUi(){
        cancelProgress();

        if (circularDataItemsArrayList.size() != 0) {
            circularDataAdapter.notifyDataSetChanged();
            updatedCircularRv.setAdapter(circularDataAdapter);
            AppHelper.print("Circular items size: " + circularDataItemsArrayList.size());
        }

        updatedCircularRv.setVisibility(circularDataItemsArrayList.size() == 0 ? View.INVISIBLE : View.VISIBLE);
        noUpdatedTv.setVisibility(circularDataItemsArrayList.size() == 0 ? View.VISIBLE : View.INVISIBLE);
    }

    public FragmentInterfaceListener getFragmentInterfaceListener() {
        return fragmentInterfaceListener;
    }

    public void setFragmentInterfaceListener(FragmentInterfaceListener fragmentInterfaceListener) {
        this.fragmentInterfaceListener = fragmentInterfaceListener;
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
