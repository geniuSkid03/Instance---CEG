package com.inspiregeniussquad.handstogether.appActivities;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appStorage.TeamData;
import com.inspiregeniussquad.handstogether.appStorage.dbAsyncHelpers.TeamDbHelper;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;

public class SplashActivity extends SuperCompatActivity {

    @BindView(R.id.logo)
    ImageView splashIv;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private int i = 0;

    private ArrayList<TeamData> teamDataArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        teamDataArrayList = new ArrayList<>();

        retriveTeamDatas();
    }

    public void retriveTeamDatas() {
        teamDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    AppHelper.print("Team data exists and trying to retrive!");
                    retriveDataFromDb(dataSnapshot);
                } else {
                    AppHelper.print("No team data found!");
                    onDataRetrived();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                AppHelper.print("Error in retriving team data!");
                onDataRetrived();
            }
        });
    }

    private void retriveDataFromDb(DataSnapshot dataSnapshot) {
        Map<String, TeamData> teamDataMap = (Map<String, TeamData>) dataSnapshot.getValue();

//        teamDatabase.teamDao.deleteAllTeams();
        teamDataArrayList.clear();

        for (Map.Entry<String, TeamData> teamDataEntry : teamDataMap.entrySet()) {
            Map map = (Map) teamDataEntry.getValue();

            TeamData teamData = new TeamData();
//            teamData.setTeamId(i);

            teamData.setTeamName((String) map.get("teamName"));
            teamData.setTeamMotto((String) map.get("teamMotto"));
            teamData.setTeamLogoUrl((String) map.get("teamLogoUri"));

            teamDataArrayList.add(teamData);

            AppHelper.print("Retrived team info: " + teamData.getTeamName() + "\t" + teamData.getTeamMotto() + "\t" + teamData.getTeamLogoUrl());

//            i++;
        }

        insertIntoDb(teamDataArrayList);
    }

    private void insertIntoDb(final ArrayList<TeamData> teamDataArrayList) {

        new TeamDbHelper(SplashActivity.this, teamDataArrayList, new TeamDbHelper.Action() {

            @Override
            public void onStart() {
                AppHelper.print("Starting to insert team datas");
//                progressBar.setIndeterminate(false);
            }

            @Override
            public void onUpdate(int progress) {
                AppHelper.print("team datas update in progress: " + progress);
//                progressBar.setProgress(progress);
            }

            @Override
            public void onEnd() {
                AppHelper.print("team datas update completed");
                onDataRetrived();
            }
        }).run();
    }

    public void onDataRetrived() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkDataAndOpen();
            }
        }, 500);
    }
}
