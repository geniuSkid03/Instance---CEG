package com.inspiregeniussquad.handstogether.appFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inspiregeniussquad.handstogether.R;

public class AboutFragment extends SuperFragment {

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater.inflate(R.layout.fragment_about, container, false));
    }

    private View initView(View view) {

        return view;
    }
}
