package com.inspiregeniussquad.handstogether.appActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appAdapters.ClubsAdapter;
import com.inspiregeniussquad.handstogether.appData.Admin;
import com.inspiregeniussquad.handstogether.appData.Clubs;
import com.inspiregeniussquad.handstogether.appData.NewsFeedItems;
import com.inspiregeniussquad.handstogether.appDialogs.AddClubDialog;
import com.inspiregeniussquad.handstogether.appFragments.SuperFragment;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;
import com.inspiregeniussquad.handstogether.appUtils.ImageHelper;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class ManageClubsActivity extends SuperCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.clubs_list)
    ListView clubsLv;

    @BindView(R.id.no_clubs)
    TextView noClubsTv;

    @BindView(R.id.add_clubs_fab)
    FloatingActionButton addClubFab;

    private ClubsAdapter clubsAdapter;
    private ArrayList<Clubs> clubsArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_clubs);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        clubsArrayList = new ArrayList<>();
        clubsAdapter = new ClubsAdapter(this, clubsArrayList, new ClubsAdapter.OnOptionsSelected() {
            @Override
            public void onClicked(int position, View view) {
//                showOptions(position, view);
            }
        });
        clubsLv.setAdapter(clubsAdapter);
    }

    @OnClick({R.id.add_clubs_fab})
    public void onclicked(View view) {
        switch (view.getId()) {
            case R.id.add_clubs_fab:
                goTo(this, AddClubsActivity.class
                        , false);
                break;
        }
    }

    private void loadClubs() {
        showProgress(getString(R.string.loading));

        clubsDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    AppHelper.print("Clubs exists and trying to retrive!");
                    retriveDataFromDb(dataSnapshot);
                } else {
                    AppHelper.print("No admins found!");
                    cancelProgress();
                    updateUi();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                cancelProgress();
                updateUi();
                AppHelper.print("Db Error while loading news feed: " + databaseError);
                showToast(ManageClubsActivity.this, getString(R.string.db_error));
            }
        });
    }

    private void retriveDataFromDb(DataSnapshot dataSnapshot) {
        cancelProgress();

        Map<String, Clubs> newsFeedItemsMap = (Map<String, Clubs>) dataSnapshot.getValue();
        clubsArrayList.clear();

        for (Map.Entry<String, Clubs> teamEntry : newsFeedItemsMap.entrySet()) {
            Map map = (Map) teamEntry.getValue();

            Clubs clubs = new Clubs((String) map.get("clubsName"), (String) map.get("clubsImgUrl"));
            clubsArrayList.add(clubs);
        }

        if (clubsArrayList.size() == 0) {
            updateUi();
            return;
        }

        clubsAdapter.notifyDataSetChanged();
        clubsLv.setAdapter(clubsAdapter);

        updateUi();
    }

    private void updateUi() {
        clubsLv.setVisibility(clubsArrayList.size() == 0 ? View.GONE : View.VISIBLE);
        noClubsTv.setVisibility(clubsArrayList.size() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadClubs();
    }

    @Override
    public void onBackPressed() {
        finish();
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
}
