package com.inspiregeniussquad.handstogether.appFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;

import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appAdapters.ClubsRecyclerAdapter;
import com.inspiregeniussquad.handstogether.appData.Clubs;
import com.inspiregeniussquad.handstogether.appInterfaces.FragmentInterfaceListener;

import java.util.ArrayList;

public class ClubsFragment extends SuperFragment {

    private RecyclerView clubsRv;

    private FragmentInterfaceListener fragmentRefreshListener;

    private ArrayList<Clubs> clubsArrayList;
    private ClubsRecyclerAdapter clubsRecyclerAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
//        super.onPrepareOptionsMenu(menu);

        MenuItem searchMenuItem = menu.findItem(R.id.action_filter_search);
        searchMenuItem.setVisible(false);
    }


    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater.inflate(R.layout.fragment_clubs, container, false));
    }

    private View initView(View view) {

        clubsRv = view.findViewById(R.id.clubs_recycler);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        clubsRv.setLayoutManager(staggeredGridLayoutManager);

        clubsArrayList = new ArrayList<>();
        clubsRecyclerAdapter = new ClubsRecyclerAdapter(getContext(), clubsArrayList, new ClubsRecyclerAdapter.ClubClickListener() {
            @Override
            public void onClicked(int position) {
                showToast("Clicked: "+position);
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void refreshClubsFragment() {
        if(clubsArrayList.isEmpty()) {
            clubsArrayList.add(new Clubs("Club one", "https://firebasestorage.googleapis.com/v0/b/wefive-50838.appspot.com/o/TeamLogo%2FCar%20Mech_Logo?alt=media&token=b9736aab-2902-4230-acfa-20cb74a192ca"));
            clubsArrayList.add(new Clubs("Club Two", "https://firebasestorage.googleapis.com/v0/b/wefive-50838.appspot.com/o/TeamLogo%2FCar%20Mech_Logo?alt=media&token=b9736aab-2902-4230-acfa-20cb74a192ca"));
            clubsArrayList.add(new Clubs("Club Three", "https://firebasestorage.googleapis.com/v0/b/wefive-50838.appspot.com/o/TeamLogo%2FCar%20Mech_Logo?alt=media&token=b9736aab-2902-4230-acfa-20cb74a192ca"));
            clubsArrayList.add(new Clubs("Club Four", "https://firebasestorage.googleapis.com/v0/b/wefive-50838.appspot.com/o/TeamLogo%2FCar%20Mech_Logo?alt=media&token=b9736aab-2902-4230-acfa-20cb74a192ca"));
            clubsArrayList.add(new Clubs("Club Five", "https://firebasestorage.googleapis.com/v0/b/wefive-50838.appspot.com/o/TeamLogo%2FCar%20Mech_Logo?alt=media&token=b9736aab-2902-4230-acfa-20cb74a192ca"));
        }

        clubsRv.setAdapter(clubsRecyclerAdapter);
    }


    public void setFragmentRefreshListener(FragmentInterfaceListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

    public FragmentInterfaceListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            if (getFragmentRefreshListener() != null) {
                getFragmentRefreshListener().refreshFragments();
            }
        }
    }
}
