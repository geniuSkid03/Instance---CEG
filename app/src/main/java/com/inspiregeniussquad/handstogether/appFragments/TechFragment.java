package com.inspiregeniussquad.handstogether.appFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appActivities.PermissionsHelperActivity;
import com.inspiregeniussquad.handstogether.appAdapters.TeamsListAdapter;
import com.inspiregeniussquad.handstogether.appData.Team;
import com.inspiregeniussquad.handstogether.appInterfaces.FragmentInterfaceListener;

import java.util.ArrayList;

public class TechFragment extends SuperFragment{


    private RecyclerView teamRv;
    private LinearLayout noTeamLtyt;

    private FragmentInterfaceListener fragmentInterfaceListener;

    private ArrayList<Team> teamArrayList;
    private TeamsListAdapter teamsListAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        teamArrayList = new ArrayList<>();
        teamsListAdapter = new TeamsListAdapter(getContext(), teamArrayList, new TeamsListAdapter.TeamClickListener() {
            @Override
            public void onTeamClicked(Team team) {
                //todo open activity and show team details completely
            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem searchMenuItem = menu.findItem(R.id.action_filter_search);
        searchMenuItem.setVisible(false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        teamRv.setAdapter(teamsListAdapter);
    }

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater.inflate(R.layout.fragment_tech, container, false));
    }

    private View initView(View view) {

        teamRv = view.findViewById(R.id.team_recycler);
        noTeamLtyt = view.findViewById(R.id.no_team_view);

        return view;
    }


    public void refreshTechTeams() {
        //todo get all team data under technical teams
        //todo show recycler view if only teams data is available
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

        if(isVisibleToUser) {
            if(getFragmentInterfaceListener() != null) {
                getFragmentInterfaceListener().refreshFragments();
            }
        }
    }
}
