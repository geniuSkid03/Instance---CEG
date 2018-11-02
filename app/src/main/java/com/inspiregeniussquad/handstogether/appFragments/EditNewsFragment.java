package com.inspiregeniussquad.handstogether.appFragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inspiregeniussquad.handstogether.R;

public class EditNewsFragment extends SuperFragment {

    private SwipeRefreshLayout refreshUpdatedNewsLayout;
    private RecyclerView updatedNewsRv;

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater.inflate(R.layout.edit_news_fragment, container, false));
    }

    private View initView(View view) {

        //show all news updated by this mobile number
        //load all updated news by this member in recycler view
        //allow to edit and delete news
        //if swiped to right then edit, open new activity and update news
        //if swiped to left then delete, make the visibility of the news to 0

        return view;
    }
}
