package com.instance.ceg.appActivities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.instance.ceg.R;
import com.instance.ceg.appAdapters.TeamMembersAdapter;
import com.instance.ceg.appData.Keys;
import com.instance.ceg.appData.Team;
import com.instance.ceg.appData.TeamMembers;
import com.instance.ceg.appUtils.AppHelper;
import com.instance.ceg.appViews.CircleImageView;
import com.instance.ceg.appViews.CircularImageView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class TeamInfoActivity extends SuperCompatActivity {

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

    @BindView(R.id.team_members_rv)
    RecyclerView teamMembersRv;

    @BindView(R.id.team_posts)
    CardView teamPostsCv;

    @BindView(R.id.team_posts_rv)
    RecyclerView teamPostsRv;

    @BindView(R.id.team_member_loading_view)
    ShimmerFrameLayout teamMemberLoadingView;

    @BindView(R.id.no_team_mem_view)
    LinearLayout noTeamMemberAvailLayout;

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
        if (team != null) {
            teamNameTv.setText(team.gettName());
            teamMottoTv.setText(team.gettMotto());
            Glide.with(this).load(team.gettLogo()).into(teamLogoIv);
            teamFoundedTv.setText(team.gettFounded());
            teamMembersCountTv.setText(team.gettMemCount());
            teamDescTv.setText(team.gettDesc());

            String teamMembersId = team.gettMemId();

            if (!TextUtils.isEmpty(teamMembersId)) {
                loadTeamMembers(teamMembersId);
            }
        }
    }

    private void loadTeamMembers(String teamMemId) {
        showTeamMemLoading();

        membersDbReference.child(teamMemId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    AppHelper.print("Team member datasnapshot exists");
                    retriveTeamMembers(dataSnapshot);
                } else {
                    AppHelper.print("Team member datasnapshot null");
                    hideTeamMemLoading();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideTeamMemLoading();
                AppHelper.print("Error in loading team members");
            }
        });
    }

    private ArrayList<TeamMembers> teamMembersArrayList = new ArrayList<>();

    private void retriveTeamMembers(DataSnapshot dataSnapshot) {
        teamMembersArrayList.clear();

        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
            teamMembersArrayList.add(dataSnapshot1.getValue(TeamMembers.class));
            AppHelper.print("Team members added!");
        }

        teamMembersRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        TeamMembersAdapter teamMembersAdapter = new TeamMembersAdapter(this, teamMembersArrayList);
        teamMembersRv.setAdapter(teamMembersAdapter);

        hideTeamMemLoading();
    }

    private void showTeamMemLoading() {
        teamMembersRv.setVisibility(View.GONE);
        teamMemberLoadingView.setVisibility(View.VISIBLE);
    }

    private void hideTeamMemLoading() {
        teamMemberLoadingView.setVisibility(View.GONE);

        teamMembersRv.setVisibility(teamMembersArrayList.size() > 0 ? View.VISIBLE : View.GONE);
        noTeamMemberAvailLayout.setVisibility(teamMembersArrayList.size() > 0 ? View.GONE : View.VISIBLE);
    }
}
