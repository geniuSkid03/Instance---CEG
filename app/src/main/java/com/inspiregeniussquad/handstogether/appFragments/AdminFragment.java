package com.inspiregeniussquad.handstogether.appFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appActivities.ManageAdminsActivity;
import com.inspiregeniussquad.handstogether.appActivities.ManageCircularActivity;
import com.inspiregeniussquad.handstogether.appActivities.ManageClubsActivity;
import com.inspiregeniussquad.handstogether.appActivities.ManageNewsActivity;
import com.inspiregeniussquad.handstogether.appActivities.ManageTeamsActivity;
import com.inspiregeniussquad.handstogether.appInterfaces.FragmentInterfaceListener;

public class AdminFragment extends SuperFragment {

    private CardView newsCv, circularCv, manageTeamsCv, manageAdminsCv, manageClubsCv;

    private FragmentInterfaceListener fragmentRefreshListener;



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
        return initView(inflater.inflate(R.layout.fragment_admin, container, false));
    }

    private View initView(View view) {

        newsCv = view.findViewById(R.id.add_news);
        circularCv = view.findViewById(R.id.add_circular);
        manageTeamsCv = view.findViewById(R.id.manage_teams);
        manageAdminsCv = view.findViewById(R.id.admin_manage_card);
        manageClubsCv = view.findViewById(R.id.manage_clubs);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doActionForClick(v);
            }
        };

        newsCv.setOnClickListener(clickListener);
        circularCv.setOnClickListener(clickListener);
        manageAdminsCv.setOnClickListener(clickListener);
        manageTeamsCv.setOnClickListener(clickListener);
        manageClubsCv.setOnClickListener(clickListener);
    }

    private void doActionForClick(View view) {
        switch (view.getId()) {
            case R.id.add_circular:
                openActivity(0);
                break;
            case R.id.add_news:
                openActivity(1);
                break;
            case R.id.manage_teams:
                openActivity(2);
                break;
            case R.id.admin_manage_card:
                openActivity(3);
                break;
            case R.id.manage_clubs:
                openActivity(4);
                break;
        }
    }

    private void openActivity(int key) {
        switch (key) {
            case 0:
                goTo(getContext(), ManageCircularActivity.class, false);
                break;
            case 1:
                goTo(getContext(), ManageNewsActivity.class, false);
                break;
            case 2:
                goTo(getContext(), ManageTeamsActivity.class, false);
                break;
            case 3:
                goTo(getContext(), ManageAdminsActivity.class, false);
                break;
            case 4:
                goTo(getContext(), ManageClubsActivity.class, false);
                break;
        }
    }


    public void refreshAdminsFragment() {

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
            if (getFragmentRefreshListener() != null && getView() != null) {
                getFragmentRefreshListener().refreshFragments();
            }
        }
    }

}
