package com.inspiregeniussquad.handstogether.appFragments;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appData.CircularDataItems;

import java.util.ArrayList;

public class CircularFragment extends SuperFragment {

    private RecyclerView circularRv;
    private LinearLayout circularLoadingView;

    private ArrayList<CircularDataItems> circularDataItems;

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_circular, container, false);
    }


    public void refreshCirculars() {

    }
}

