package com.instance.ceg.appActivities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.instance.ceg.R;
import com.instance.ceg.appAdapters.ClubsAdapter;
import com.instance.ceg.appData.Clubs;
import com.instance.ceg.appUtils.AppHelper;

import java.util.ArrayList;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
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
        clubsAdapter = new ClubsAdapter(this, clubsArrayList, this::showOptions);
        clubsLv.setAdapter(clubsAdapter);
    }

    private void showOptions(int position, View view) {
        View optionsView = LayoutInflater.from(this).inflate(R.layout.news_edit_options, null);
        TextView editTv = optionsView.findViewById(R.id.edit);
        TextView deleteTv = optionsView.findViewById(R.id.delete);

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setView(optionsView);

        View.OnClickListener clickListener = view1 -> {
            switch (view1.getId()) {
                case R.id.delete:
                    alertDialog.dismiss();
                    proceedToDelete(position);
                    break;
                case R.id.edit:
                    alertDialog.dismiss();
                    proceedtoEdit(position);
                    break;
            }
        };

        editTv.setOnClickListener(clickListener);
        deleteTv.setOnClickListener(clickListener);

        alertDialog.setCancelable(true);
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    private void proceedtoEdit(int position) {
        Clubs clubs = clubsArrayList.get(position);
        if(clubs != null) {
            goTo(this, AddClubsActivity.class , false, "Clubs", new Gson().toJson(clubs));
        }
    }

    private void proceedToDelete(int position) {
        AppHelper.print("Trying to delete: " + position);
        showProgress(getString(R.string.deleting_news));

        Clubs clubs = clubsArrayList.get(position);
        if(clubs != null) {
            Query deleteQuery = clubsDbRef.orderByChild("clubsName").equalTo(clubs.getClubsName());
            deleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot deleteSnapshot : dataSnapshot.getChildren()) {
                        deleteSnapshot.getRef().removeValue();
                    }
                    refreshClubs();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    cancelProgress();
                    AppHelper.print("Db error while trying to delete: " + databaseError.getMessage());
                    showToast(ManageClubsActivity.this, getString(R.string.db_error));
                }
            });
        }
    }

    private void refreshClubs() {
        loadClubs();
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
