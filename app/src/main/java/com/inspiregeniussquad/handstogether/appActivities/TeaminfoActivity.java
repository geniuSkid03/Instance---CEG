package com.inspiregeniussquad.handstogether.appActivities;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appAdapters.TeamMembersAdapter;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appData.Team;
import com.inspiregeniussquad.handstogether.appData.TeamMembers;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

import butterknife.BindView;

public class TeaminfoActivity extends SuperCompatActivity {

    @BindView(R.id.team_scroll_view)
    NestedScrollView teamNestedScrollView;

    @BindView(R.id.team_content_view)
    RelativeLayout teamContentView;

    @BindView(R.id.team_loading_view)
    ShimmerFrameLayout teamLoadingView;

    @BindView(R.id.team_name)
    TextView teamNameTv;

    @BindView(R.id.team_motto)
    TextView teamMottoTv;

    @BindView(R.id.team_log_iv)
    CircularImageView teamLogoIv;

    @BindView(R.id.dashboard)
    CardView dashboardCv;

    @BindView(R.id.team_founded)
    TextView teamFoundedTv;

    @BindView(R.id.team_members_count)
    TextView teamMembersCountTv;

    @BindView(R.id.team_posts_count)
    TextView teamPostsTv;

    @BindView(R.id.desc)
    CardView descCv;

    @BindView(R.id.team_desc)
    TextView teamDescTv;

    @BindView(R.id.members_card)
    CardView membersCv;

    @BindView(R.id.team_posts_rv)
    RecyclerView teamMembersRv;

    @BindView(R.id.team_posts)
    CardView teamPostsCv;

    @BindView(R.id.team_members_rv)
    RecyclerView teamPostsRv;

    private Team team;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teaminfo);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (getIntent().getExtras() != null) {
            team = gson.fromJson(getIntent().getStringExtra(Keys.TEAM), Team.class);
            updateUi(team);
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

    @Override
    public void onBackPressed() {
        finish();
    }

    private void updateUi(Team team) {
        if(team != null) {
            teamNameTv.setText(team.gettName());
            teamMottoTv.setText(team.gettMotto());
            Glide.with(this).load(team.gettLogo()).into(teamLogoIv);
            teamFoundedTv.setText(team.gettFounded());
            teamMembersCountTv.setText(team.gettMemCount());
            teamDescTv.setText(team.gettDesc());

            String teamMembersId = team.gettMemId();


            ArrayList<TeamMembers> teamMembers = team.getTeamMembers();

            if(teamMembers != null) {
                AppHelper.print(teamMembers.size()+" ");

                teamMembersRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                TeamMembersAdapter teamMembersAdapter = new TeamMembersAdapter(this, teamMembers);
                teamMembersRv.setAdapter(teamMembersAdapter);
            } else {
                AppHelper.print("Team members null");
            }
        }
    }
}
