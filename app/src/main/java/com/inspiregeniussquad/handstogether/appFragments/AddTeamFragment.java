package com.inspiregeniussquad.handstogether.appFragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appActivities.AddTeamMembersActivity;
import com.inspiregeniussquad.handstogether.appData.Clubs;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appData.Team;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class AddTeamFragment extends SuperFragment {

    private AppCompatButton addTeamBtn;
    private ImageView teamLogoIv;
    private ImageButton imgLoadBtn, reduceTeamMemberBtn, addTeamMemberBtn;
    private EditText teamNameEd, teamMottoEd, teamMembersCountEd, teamDescEd;
    private Spinner clubNameSpinner;

    private Uri teamImgUri, finalTeamImgUri;

    private static final int CHOOSE_FILE = 101;
    private static final int OPEN_CAMERA = 100;

    private String teamName, teamMotto, teamMembersCount, teamDesc;
    private int membersCount = 10;

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater.inflate(R.layout.add_team_fragment, container, false));
    }

    private View initView(View view) {

        addTeamBtn = view.findViewById(R.id.next_btn);
        teamLogoIv = view.findViewById(R.id.team_logo);
        imgLoadBtn = view.findViewById(R.id.load_team_image);
        teamNameEd = view.findViewById(R.id.team_name);
        teamMottoEd = view.findViewById(R.id.team_motto);
        teamDescEd = view.findViewById(R.id.team_desc);
        teamMembersCountEd = view.findViewById(R.id.team_members_count);
        addTeamMemberBtn = view.findViewById(R.id.minus_team_member);
        reduceTeamMemberBtn = view.findViewById(R.id.add_team_member);
        clubNameSpinner = view.findViewById(R.id.club_name_spinner);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.next_btn:
                        onFirstStepCompleted();
                        break;
                    case R.id.load_team_image:
                        showOptionsForLoadingImage();
                        break;
                    case R.id.add_team_member:
                        addByOne();
                        break;
                    case R.id.minus_team_member:
                        minusByOne();
                        break;
                }
            }
        };

        addTeamBtn.setOnClickListener(clickListener);
        imgLoadBtn.setOnClickListener(clickListener);
        addTeamMemberBtn.setOnClickListener(clickListener);
        reduceTeamMemberBtn.setOnClickListener(clickListener);

        loadClubInfoSpinner();

        return view;
    }

    private void addByOne() {
        membersCount = membersCount + 1;
        teamMembersCountEd.setText(String.format(Locale.getDefault(), "%d", membersCount));
    }

    private void minusByOne() {
        if (membersCount > 10) {
            membersCount = membersCount - 1;
            teamMembersCountEd.setText(String.format(Locale.getDefault(), "%d", membersCount));
        }
    }

    private void showOptionsForLoadingImage() {
        if (dataStorage.getBoolean(Keys.PERMISSIONS_GRANTED)) {
            galleryIntent();
        } else {
            showToast(getString(R.string.permissoin_denied_string));
        }
    }

    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, CHOOSE_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CHOOSE_FILE) {
                teamImgUri = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), teamImgUri);
                    if (bitmap != null) {
                        Picasso.get().load(teamImgUri).into(teamLogoIv);
                    } else {
                        AppHelper.showToast(getActivity(), "Image bitmap null");
                    }
                } catch (Exception e) {
                    AppHelper.showToast(getActivity(), "Exception in parsing image");
                }

            }
        }
    }

    private void onFirstStepCompleted() {
        if (isAllDataAvailable()) {
            getAllTeamMembers();
        }
    }

    private void getAllTeamMembers() {
        Team team = new Team(teamName, teamMotto, teamDesc, teamImgUri.toString(), null, teamMembersCount, clubName);
        goTo(getActivity(), AddTeamMembersActivity.class, true, Keys.TEAM, gson.toJson(team));
    }

    private boolean isAllDataAvailable() {
        if (TextUtils.isEmpty(clubName) || clubName.equalsIgnoreCase(getString(R.string.choose_club))) {
            showToast(getString(R.string.choose_clubs));
            return false;
        }

        if (teamImgUri == null) {
            showToast(getString(R.string.provide_team_logo));
            return false;
        }

        teamName = teamNameEd.getText().toString().trim();
        if (TextUtils.isEmpty(teamName)) {
            showToast(getString(R.string.enter_team_name));
            return false;

        }

        teamMotto = teamMottoEd.getText().toString().trim();
        if (TextUtils.isEmpty(teamMotto)) {
            showToast(getString(R.string.enter_team_motto));
            return false;
        }

        teamDesc = teamDescEd.getText().toString().trim();
        if (TextUtils.isEmpty(teamDesc)) {
            showToast(getString(R.string.enter_desc));
            return false;
        }

        teamMembersCount = teamMembersCountEd.getText().toString().trim();
        if (!TextUtils.isDigitsOnly(teamMembersCount) && TextUtils.isEmpty(teamMembersCount)) {
            showToast(getString(R.string.enter_team_count));
            return false;
        }
        return true;
    }

    private void loadClubInfoSpinner() {
        showProgress(getString(R.string.loading_clubs));

        clubDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    retriveClubsData(dataSnapshot);
                } else {
                    cancelProgress();
                    AppHelper.print("Team doesn't exist!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                cancelProgress();
                showToast(getString(R.string.db_error));
                AppHelper.print("Db Error: " + databaseError);
            }
        });
    }

    private ArrayList<Clubs> clubsArrayList = new ArrayList<>();

    private void retriveClubsData(DataSnapshot dataSnapshot) {

        AppHelper.print("Retriving clubs information");

        Map<String, Team> teamData = (Map<String, Team>) dataSnapshot.getValue();

        clubsArrayList.clear();
        clubsArrayList.add(0, new Clubs(getString(R.string.choose_club), ""));

        for (Map.Entry<String, Team> teamEntry : teamData.entrySet()) {
            Map map = (Map) teamEntry.getValue();

            Clubs clubs = new Clubs((String) map.get("clubsName"), (String) map.get("clubsImgUrl"));
            clubsArrayList.add(clubs);
        }

        if (clubsArrayList.size() != 0) {
            loadWithSpinner(clubsArrayList);
        } else {
            cancelProgress();
            AppHelper.print("Club size 0");
        }
    }

    private String clubName = "";

    private void loadWithSpinner(ArrayList<Clubs> clubsArrayList) {
        final String teamNameArray[] = new String[clubsArrayList.size()];

        for (int i = 0; i < clubsArrayList.size(); i++) {
            teamNameArray[i] = clubsArrayList.get(i).getClubsName();
        }

        ArrayAdapter<String> spinnerArrayAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, teamNameArray);

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        clubNameSpinner.setAdapter(spinnerArrayAdapter);

        clubNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                clubName = teamNameArray[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                showToast(getString(R.string.choose_club));
            }
        });

        cancelProgress();
    }


}
