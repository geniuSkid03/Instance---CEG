package com.inspiregeniussquad.handstogether.appFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inspiregeniussquad.handstogether.R;

public class UpdateTeamFragment extends SuperFragment {

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater.inflate(R.layout.update_team_fragment, container, false));
    }

    private View initView(View view) {
        return view;
    }
}
