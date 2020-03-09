package com.instance.ceg.appFragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.instance.ceg.R;
import com.instance.ceg.appAdapters.TeamsListAdapter;
import com.instance.ceg.appData.Team;
import com.instance.ceg.appInterfaces.FragmentInterfaceListener;

import java.util.ArrayList;

public class NonTechFragment  extends SuperFragment{

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
    protected View inflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return intiView(inflater.inflate(R.layout.fragment_non_tech, container, false));
    }

    private View intiView(View view) {

        teamRv = view.findViewById(R.id.team_recycler);
        noTeamLtyt = view.findViewById(R.id.no_team_view);

        return view;
    }

    public void refreshNonTechTeams() {
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
