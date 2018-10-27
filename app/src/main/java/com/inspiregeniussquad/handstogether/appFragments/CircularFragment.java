package com.inspiregeniussquad.handstogether.appFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appData.CircularDataItems;

import java.util.ArrayList;

public class CircularFragment extends SuperFragment implements SearchView.OnQueryTextListener {

    private RecyclerView circularRv;
    private LinearLayout circularLoadingView;

    private ArrayList<CircularDataItems> circularDataItems;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
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
        return inflater.inflate(R.layout.fragment_circular, container, false);
    }


    public void refreshCirculars() {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}

