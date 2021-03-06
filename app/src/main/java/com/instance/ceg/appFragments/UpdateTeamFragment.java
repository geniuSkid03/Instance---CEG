package com.instance.ceg.appFragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.instance.ceg.R;
import com.instance.ceg.appActivities.AddTeamMembersActivity;
import com.instance.ceg.appData.Keys;
import com.instance.ceg.appData.Team;
import com.instance.ceg.appUtils.AppHelper;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

public class UpdateTeamFragment extends SuperFragment {

    private LinearLayout contentViewLayout;
    private AppCompatButton updateTeamBtn;
    private ImageView teamLogoIv;
    private ImageButton imgLoadBtn, reduceTeamMemberBtn, addTeamMemberBtn;
    private EditText teamNameEd, teamMottoEd, teamMembersCountEd, teamDescEd, teamFoundedEd;
    private Spinner clubNameSpinner;

    private Uri teamImgUri, finalTeamImgUri;

    private static final int CHOOSE_FILE = 101;
    private static final int OPEN_CAMERA = 100;


    private String tName,
            tMotto,
            tLogo,
            tMemCount,
            tDesc,
            tId, tMemID,
            tClubId,
            tClubName, tFoundedYear;
    private int membersCount = 10;

    private ArrayList<Team> teamsList = new ArrayList<>();

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater.inflate(R.layout.update_team_fragment, container, false));
    }

    private View initView(View view) {

        contentViewLayout = view.findViewById(R.id.team_update_content);
        teamNameEd = view.findViewById(R.id.team_name);
        teamMottoEd = view.findViewById(R.id.team_motto);
        updateTeamBtn = view.findViewById(R.id.update_team_btn);
        teamLogoIv = view.findViewById(R.id.team_logo);
        imgLoadBtn = view.findViewById(R.id.load_team_image);
        teamNameEd = view.findViewById(R.id.team_name);
        teamMottoEd = view.findViewById(R.id.team_motto);
        teamDescEd = view.findViewById(R.id.team_desc);
        teamMembersCountEd = view.findViewById(R.id.team_members_count);
        addTeamMemberBtn = view.findViewById(R.id.minus_team_member);
        reduceTeamMemberBtn = view.findViewById(R.id.add_team_member);
        clubNameSpinner = view.findViewById(R.id.club_name_spinner);
        teamFoundedEd = view.findViewById(R.id.team_founded);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.update_team_btn:
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

        updateTeamBtn.setOnClickListener(clickListener);
        imgLoadBtn.setOnClickListener(clickListener);
        addTeamMemberBtn.setOnClickListener(clickListener);
        reduceTeamMemberBtn.setOnClickListener(clickListener);

        loadTeamIntoSpinner();

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

    private boolean isImageChanged = false;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CHOOSE_FILE) {
                teamImgUri = data.getData();
                if (teamImgUri != null) {
                    isImageChanged = true;
                    tLogo = teamImgUri.toString();
                }
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), teamImgUri);
                    if (bitmap != null) {
                        Glide.with(getContext()).load(teamImgUri).into(teamLogoIv);
                    } else {
                        AppHelper.showToast(getActivity(), "Image bitmap null");
                    }
                } catch (Exception e) {
                    AppHelper.showToast(getActivity(), "Exception in parsing image");
                }

            }
        }
    }


    private void loadTeamIntoSpinner() {
        showProgress(getString(R.string.loading));

        teamDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    retriveTeamData(dataSnapshot);
                    AppHelper.print("Team exist!");
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

    private void retriveTeamData(DataSnapshot dataSnapshot) {
        AppHelper.print("Retriving clubs information");

        Map<String, Team> teamData = (Map<String, Team>) dataSnapshot.getValue();

        teamsList.clear();
        teamsList.add(0, new Team(getString(R.string.choose_club), ""));

        for (Map.Entry<String, Team> teamEntry : teamData.entrySet()) {
            Map map = (Map) teamEntry.getValue();

            Team team = new Team(
                    (String) map.get("tName"),
                    (String) map.get("tMotto"),
                    (String) map.get("tLogo"),
                    (String) map.get("tMemCount"),
                    (String) map.get("tDesc"),
                    (String) map.get("tId"),
                    (String) map.get("tClubId"),
                    (String) map.get("tClubName"),
                    (String) map.get("tFounded"),
                    (String) map.get("tMemId"));

            teamsList.add(team);
        }

        if (teamsList.size() != 0) {
            loadWithSpinner(teamsList);
        } else {
            cancelProgress();
            AppHelper.print("Club size 0");
        }
    }


    private void loadWithSpinner(final ArrayList<Team> teamsList) {
        final String teamNameArray[] = new String[teamsList.size()];

        for (int i = 0; i < teamsList.size(); i++) {
            teamNameArray[i] = teamsList.get(i).gettName();
            AppHelper.print("Team Name: " + teamsList.get(i).gettName());
        }

        ArrayAdapter<String> spinnerArrayAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, teamNameArray);

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        clubNameSpinner.setAdapter(spinnerArrayAdapter);

        clubNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                onClubInfoReceived(teamsList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                showToast(getString(R.string.choose_club));
            }
        });

        cancelProgress();
    }

    private void onClubInfoReceived(Team clubs) {
        tName = clubs.gettName();
        tMotto = clubs.gettMotto();
        tLogo = clubs.gettLogo();
//        teamImgUri = Uri.parse(clubs.gettLogo());
        tMemCount = clubs.gettMemCount();
        tDesc = clubs.gettDesc();
        tId = clubs.gettId();
        tMemID = clubs.gettMemId();
        tClubId = clubs.gettClubId();
        tClubName = clubs.gettClubName();
        tMemCount = clubs.gettMemCount();
        tFoundedYear = clubs.gettFounded();

        contentViewLayout.setVisibility(View.VISIBLE);
        Glide.with(getContext()).load(tLogo).into(teamLogoIv);

        teamNameEd.setText(tName);
        teamMottoEd.setText(tMotto);
        teamDescEd.setText(tDesc);
        teamFoundedEd.setText(tFoundedYear);
        teamMembersCountEd.setText(tMemCount);
    }

    public void refresh() {
        loadTeamIntoSpinner();

        teamNameEd.setText("");
        teamMottoEd.setText("");
        teamDescEd.setText("");
        teamFoundedEd.setText("");
        teamMembersCountEd.setText("");
    }

    private void onFirstStepCompleted() {
        if (isAllDataAvailable()) {
            getAllTeamMembers();
        }
    }

    private boolean isAllDataAvailable() {
        if (TextUtils.isEmpty(tClubName) || tClubName.equalsIgnoreCase(getString(R.string.choose_club)) || tClubId.equals("")) {
            showToast(getString(R.string.choose_clubs));
            return false;
        }

        if (teamImgUri == null && TextUtils.isEmpty(tLogo)) {
            showToast(getString(R.string.provide_team_logo));
            return false;
        }

        tName = teamNameEd.getText().toString().trim();
        if (TextUtils.isEmpty(tName)) {
            showToast(getString(R.string.enter_team_name));
            return false;

        }

        tMotto = teamMottoEd.getText().toString().trim();
        if (TextUtils.isEmpty(tMotto)) {
            showToast(getString(R.string.enter_team_motto));
            return false;
        }

        tDesc = teamDescEd.getText().toString().trim();
        if (TextUtils.isEmpty(tDesc)) {
            showToast(getString(R.string.enter_desc));
            return false;
        }

        tMemCount = teamMembersCountEd.getText().toString().trim();
        if (!TextUtils.isDigitsOnly(tMemCount) && TextUtils.isEmpty(tMemCount)) {
            showToast(getString(R.string.enter_team_count));
            return false;
        }

        tFoundedYear = teamFoundedEd.getText().toString().trim();
        if (TextUtils.isEmpty(tFoundedYear)) {
            showToast(getString(R.string.enter_founded_year));
            return false;
        } else if (tFoundedYear.length() != 4) {
            showToast(getString(R.string.enter_valid_year));
            return false;
        }

        return true;
    }

    private void getAllTeamMembers() {

        Team team = new Team();
        team.settName(tName);
        team.settMotto(tMotto);
        team.settDesc(tDesc);
        team.settLogo(tLogo);
        team.settClubId(tClubId);
        team.settClubName(tClubName);
        team.settMemCount(tMemCount);
        team.settFounded(tFoundedYear);
        team.settMemId(tMemID);
        team.settId(tId);

        goTo(getActivity(), AddTeamMembersActivity.class, false, Keys.TEAM, gson.toJson(team), "edit_team", isImageChanged ? "1" : "0");
    }
}
