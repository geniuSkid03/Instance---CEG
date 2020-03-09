package com.instance.ceg.appFragments;

import android.app.AlertDialog;
import android.content.Intent;
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
import com.google.gson.Gson;
import com.instance.ceg.R;
import com.instance.ceg.appActivities.EditCircularActivity;
import com.instance.ceg.appActivities.EditNewsActivity;
import com.instance.ceg.appAdapters.CircularEditAdapter;
import com.instance.ceg.appData.CircularDataItems;
import com.instance.ceg.appData.Keys;
import com.instance.ceg.appData.NewsFeedItems;
import com.instance.ceg.appInterfaces.FragmentInterfaceListener;
import com.instance.ceg.appUtils.AppHelper;

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
                        proceedToEdit(position);
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

    private void proceedToEdit(int position) {
        CircularDataItems circularDataItems = circularDataItemsArrayList.get(position);
        Intent intent = new Intent(getContext(), EditCircularActivity.class);
        intent.putExtra("circular_items", new Gson().toJson(circularDataItems));
        startActivity(intent);
    }

    private void proceedToDelete(int position) {
        AppHelper.print("Trying to delete: " + position);
        showProgress(getString(R.string.deleting_circular));

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

        circularDataItemsArrayList.clear();

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

        for (Map.Entry<String, NewsFeedItems> teamEntry : newsFeedItemsMap.entrySet()) {
            Map map = (Map) teamEntry.getValue();

            CircularDataItems circularDataItems = new CircularDataItems();

            circularDataItems.setcTitle((String) map.get("cTitle"));
            circularDataItems.setcDesc((String) map.get("cDesc"));
            circularDataItems.setCircularImgPath((String) map.get("circularImgPath"));
            circularDataItems.setpDate((String) map.get("pDate"));
            circularDataItems.setcId((String) map.get("cId"));

            if(isCircularManager()) {
                if (mobileNumber.equals(map.get("postedBy"))) {
                    circularDataItemsArrayList.add(circularDataItems);
                }
            } else {
                circularDataItemsArrayList.add(circularDataItems);
            }

            updateUi();
        }
    }

    private boolean isCircularManager(){
        return /*dataStorage.getString(Keys.ADMIN_VALUE).equals("4") ||*/
                dataStorage.getString(Keys.ADMIN_VALUE).equals("5") ;
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
