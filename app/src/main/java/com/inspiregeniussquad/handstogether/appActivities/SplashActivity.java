package com.inspiregeniussquad.handstogether.appActivities;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appData.Admin;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appStorage.TeamData;
import com.inspiregeniussquad.handstogether.appStorage.dbAsyncHelpers.TeamDbHelper;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;

public class SplashActivity extends SuperCompatActivity {

    @BindView(R.id.logo)
    ImageView splashIv;

   /* @BindView(R.id.progress_bar)
    ProgressBar progressBar;*/

    @BindView(R.id.app_motto)
    TextView appMottoTv;

    private int i = 0;

    private ArrayList<TeamData> teamDataArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        teamDataArrayList = new ArrayList<>();

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                slideToTop(appMottoTv);
//            }
//        }, 1000);


        retrieveUserInfo();

//        AnimatorSet set = (AnimatorSet) AnimatorInflater
//                .loadAnimator(this, R.animator.logo_animator);
//        set.setTarget(splashIv);
//        set.start();

//        appMottoTv.setVisibility(View.VISIBLE);
//        TranslateAnimation animate = new TranslateAnimation(
//                0,                 // fromXDelta
//                0,                 // toXDelta
//                appMottoTv.getHeight(),  // fromYDelta
//                0);                // toYDelta
//        animate.setDuration(500);
//        animate.setFillAfter(true);
//        appMottoTv.startAnimation(animate);

//        slideToBottom(splashIv);

//        slideToTop(appMottoTv);

    }

    private void animateView(final TextView textView) {
        textView.animate()
                .translationY(0)
                .alpha(0.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        textView.setVisibility(View.VISIBLE);
                    }
                });
    }

    public void animateViewFromBottomToTop(final View view){

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                final int TRANSLATION_Y = view.getHeight();
                view.setTranslationY(TRANSLATION_Y);
                view.setVisibility(View.GONE);
                view.animate()
                        .translationYBy(-TRANSLATION_Y)
                        .setDuration(500)
                        .setStartDelay(200)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(final Animator animation) {
                                view.setVisibility(View.VISIBLE);

                                Toast.makeText(SplashActivity.this, "Anomation started", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .start();
            }
        });
    }

    private void retrieveUserInfo() {
        adminDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    parseUserInfo(dataSnapshot);
                } else {
                    retriveTeamDatas();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                AppHelper.print("User info not loaded");
                AppHelper.print("Db Error: "+databaseError.getMessage());
            }
        });
    }

    private ArrayList<Admin> adminsArraylist;

    private void parseUserInfo(DataSnapshot dataSnapshot) {

        adminsArraylist = new ArrayList<>();

        Map<String, TeamData> teamDataMap = (Map<String, TeamData>) dataSnapshot.getValue();

        adminsArraylist.clear();

        AppHelper.print("Admins data\n----------------------------------------");

        for (Map.Entry<String, TeamData> teamDataEntry : teamDataMap.entrySet()) {
            Map map = (Map) teamDataEntry.getValue();

            Admin admin = new Admin((String) map.get("name"), (String) map.get("mobile"),
                    (String) map.get("position"));

            AppHelper.print("Name: "+admin.getName());

            adminsArraylist.add(admin);
        }

        dataStorage.saveString(Keys.ADMIN_INFO, gson.toJson(adminsArraylist));

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
        }, 10000);
    }

    public static void slideToBottom(View view){
        TranslateAnimation animate = new TranslateAnimation(0,0,0,view.getHeight());
        animate.setDuration(2000);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.VISIBLE);
    }

    public static void slideToTop(final View view){

//
//        view.animate()
//                .setDuration(3000)
//                .alpha(0)
//                .se
//
        TranslateAnimation animate = new TranslateAnimation(0,0,view.getHeight(),0);
//        animate.setDuration(3000);
//        animate.setFillAfter(true);
//        view.startAnimation(animate);
//        view.setVisibility(View.VISIBLE);

        view.setAnimation(animate);
        view.animate()
                .setDuration(3000)
                .translationY(view.getHeight())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);

                        view.setVisibility(View.VISIBLE);
                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
