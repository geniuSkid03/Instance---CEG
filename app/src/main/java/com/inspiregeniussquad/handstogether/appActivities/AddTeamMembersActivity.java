package com.inspiregeniussquad.handstogether.appActivities;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appData.Team;
import com.inspiregeniussquad.handstogether.appData.TeamMembers;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class AddTeamMembersActivity extends SuperCompatActivity {

    private Team team;

    @BindView(R.id.team_members_names_list)
    ListView teamMembersLv;

    @BindView(R.id.add_btn)
    AppCompatButton addMemberBtn;

    @BindView(R.id.team_member_name)
    EditText teamMemberNameEd;

    @BindView(R.id.member_position_spinner)
    AppCompatSpinner teamMemberPositionSpinner;

    @BindView(R.id.no_members_view)
    TextView noMembersTv;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private TeamMembers teamMembers;
    private ArrayList<TeamMembers> teamMembersArrayList;
    private ArrayAdapter<TeamMembers> teamMembersArrayAdapter;

    private String memberName;
    private int memberPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team_members);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        if (getIntent().getExtras() != null) {
            team = gson.fromJson(getIntent().getStringExtra(Keys.TEAM), Team.class);
        }

        String[] positionNames = {getString(R.string.empty), getString(R.string.president), getString(R.string.member), getString(R.string.vice_president)};
        loadSpinner(positionNames);

        teamMembersArrayList = new ArrayList<>();

        teamMembersArrayAdapter = new ArrayAdapter<TeamMembers>(this, R.layout.members_name_item, teamMembersArrayList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if(convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.members_name_item, parent, false);
                }

                TeamMembers teamMembers = teamMembersArrayList.get(position);

                createList(teamMembers, convertView);

                return convertView;
            }
        };
        teamMembersLv.setAdapter(teamMembersArrayAdapter);
    }

    private void createList(TeamMembers teamMembers, View convertView) {
        if(teamMembers == null || convertView == null) {
            return;
        }

        CircularImageView circularImageView = convertView.findViewById(R.id.member_image);
        Picasso.get().load(Uri.parse(team.getTeamLogoUri())).into(circularImageView);

        ((TextView) findViewById(R.id.member_name)).setText(teamMembers.getTeamMemberName());
        ((TextView) findViewById(R.id.member_position)).setText(teamMembers.getTeamMemberPosition());

    }

    private void updateUi() {
        teamMembersLv.setVisibility(teamMembersArrayList.size() != 0 ? View.VISIBLE : View.GONE);
        noMembersTv.setVisibility(teamMembersArrayList.size() == 0 ? View.VISIBLE : View.GONE );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ok_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                showSnack("Save");
                break;
            case android.R.id.home:
                showSnack("Go back");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.add_btn})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.add_btn:
                checkAndAddMember();
                break;
        }
    }

    private void loadSpinner(final String[] positionNames) {

        ArrayAdapter positionAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, positionNames);
        positionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamMemberPositionSpinner.setAdapter(positionAdapter);

        teamMemberPositionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                memberPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                showSnack(getString(R.string.choose_position));
            }
        });
    }

    private void checkAndAddMember() {
        memberName = teamMemberNameEd.getText().toString().trim();

        if (TextUtils.isEmpty(memberName)) {
            showSnack(getString(R.string.enter_name));
            return;
        }


        if (memberPosition == 0) {
            showSnack(getString(R.string.choose_position));
            return;
        }

        if (teamMembersArrayList.size() == 0) {
            addToList(memberName, memberPosition);
        } else {
            for (TeamMembers teamMembers1 : teamMembersArrayList) {
                if (!teamMembers1.getTeamMemberName().equalsIgnoreCase(memberName)) {
                    addToList(memberName, memberPosition);
                }
            }
        }
    }

    private void addToList(String memberName, int memberPosition) {
        TeamMembers teamMembers = new TeamMembers();
        teamMembers.setTeamMemberName(memberName);
        teamMembers.setTeamMemberPosition(String.valueOf(memberPosition));

        teamMembersArrayList.add(teamMembers);

        //todo notify adapter and update UI

        teamMembersArrayAdapter.notifyDataSetChanged();

        updateUi();

        teamMemberNameEd.setText("");
        AddTeamMembersActivity.this.memberName = null;
        AddTeamMembersActivity.this.memberPosition = 0;
        teamMemberPositionSpinner.setSelection(0);
    }
}
