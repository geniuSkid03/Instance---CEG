package com.inspiregeniussquad.handstogether.appActivities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appAdapters.TeamsItemAdapter;
import com.inspiregeniussquad.handstogether.appData.Clubs;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appData.Team;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;

public class TeamsActivity extends SuperCompatActivity {

    @BindView(R.id.teams_list_rv)
    RecyclerView teamsRv;

    @BindView(R.id.no_clubs_view)
    LinearLayout noClubsLayout;

    @BindView(R.id.team_loading_view)
    ShimmerFrameLayout loadingView;

    @BindView(R.id.club_name_text)
    TextView clubNameTv;

    private ArrayList<Team> teamArrayList;
    private TeamsItemAdapter teamsItemAdapter;

    private Clubs clubs;
    private Team team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_view);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        if (getIntent() != null) {
            clubs = gson.fromJson(getIntent().getStringExtra(Keys.CLUBS_ID), Clubs.class);
        }

        teamArrayList = new ArrayList<>();

        teamsRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        if (clubs != null) {
            showLoading();
            getTeamsInClub(clubs.getClubsId());

            AppHelper.print("ClubInfo: " + clubs.getClubsId() + "\t" + clubs.getClubsName());

            clubNameTv.setText(clubs.getClubsName());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getTeamsInClub(final String clubId) {
        teamDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    AppHelper.print("Team data exists, trying to retrive!");
                    getTeamData(dataSnapshot, clubId);
                } else {
                    hideLoading();
                    AppHelper.print("No data exists");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideLoading();
            }
        });
    }

    private void getTeamData(DataSnapshot dataSnapshot, String clubId) {
        Map<String, Team> teamMap = (Map<String, Team>) dataSnapshot.getValue();

        if (teamMap != null) {
            for (Map.Entry<String, Team> teamEntry : teamMap.entrySet()) {
                Map map = (Map) teamEntry.getValue();

                Team team = new Team((String) map.get("tName"), (String) map.get("tMotto"),
                        (String) map.get("tLogo"), (String) map.get("tMemCount"),
                        (String) map.get("tDesc"), (String) map.get("tId"),
                        (String) map.get("tClubId"), (String) map.get("tClubName"));
                team.settFounded((String) map.get("tFounded"));
                team.settMemId((String) map.get("tMemId"));

                if (team.gettClubId().equals(clubId)) {
                    teamArrayList.add(team);
                }
            }
        } else {
            AppHelper.print("Team data not found!");
        }

        if (teamArrayList != null && teamArrayList.size() > 0) {
            teamsItemAdapter = new TeamsItemAdapter(TeamsActivity.this, teamArrayList, new TeamsItemAdapter.TeamClickListener() {
                @Override
                public void onClicked(Team team) {

                    goTo(TeamsActivity.this, TeamInfoActivity.class, false, Keys.TEAM, gson.toJson(team));
                }
            });
            teamsRv.setAdapter(teamsItemAdapter);
        }

        hideLoading();
    }

    private void updateUi() {
        teamsRv.setVisibility(teamArrayList.size() > 0 ? View.VISIBLE : View.GONE);
        noClubsLayout.setVisibility(teamArrayList.size() > 0 ? View.GONE : View.VISIBLE);
    }

    private void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
        loadingView.startShimmer();
    }

    private void hideLoading() {
        loadingView.setVisibility(View.GONE);
        loadingView.stopShimmer();

        updateUi();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
