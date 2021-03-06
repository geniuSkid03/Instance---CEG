package com.instance.ceg.appActivities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.instance.ceg.R;
import com.instance.ceg.appData.Keys;
import com.instance.ceg.appData.Team;
import com.instance.ceg.appData.TeamMembers;
import com.instance.ceg.appHelpers.DbHelper;
import com.instance.ceg.appUtils.AppHelper;
import com.instance.ceg.appViews.CircularImageView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.OnClick;

public class AddTeamMembersActivity extends SuperCompatActivity {

    @BindView(R.id.team_members_names_list)
    ListView teamMembersLv;

    @BindView(R.id.add_btn)
    AppCompatButton addMemberBtn;

    @BindView(R.id.team_member_name)
    EditText teamMemberNameEd;

    @BindView(R.id.team_member_position)
    EditText designationEd;

    @BindView(R.id.no_members_view)
    TextView noMembersTv;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.members_count_display)
    TextView membersCountDisplayTv;

    private ArrayList<TeamMembers> teamMembersArrayList;
    private ArrayAdapter<TeamMembers> teamMembersArrayAdapter;

    private Uri uploadedLogoUri;
    private String memberName;
    private int teamMembersCount;
    private String memberDesignation = "";

    private Team team;
    private boolean isTeamEdit = false;
    private boolean isImageChanged = false;

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

        teamMembersArrayList = new ArrayList<>();


        if (getIntent().getExtras() != null) {
            team = gson.fromJson(getIntent().getStringExtra(Keys.TEAM), Team.class);

            String val = getIntent().getStringExtra("edit_team");
            if (val != null && !val.equals("")) {
                isTeamEdit = true;
                isImageChanged = val.equals("1");
            }

        }

        if (team != null) {
            membersCountDisplayTv.setText(String.format(Locale.getDefault(), "%s %s", getString(R.string.number_of_members), team.gettMemCount()));
            teamMembersCount = Integer.parseInt(team.gettMemCount());
            if (isTeamEdit) {
                AppHelper.print("teamID: "+team.gettId());

                String teamMemId = team.gettMemId();
                if (!TextUtils.isEmpty(teamMemId)) {
                    loadTeamMembersList(teamMemId);
                    AppHelper.print("teamMemId: " + teamMemId);
                }
            } else {
                initAdapter();
            }
        }

//        final String[] positionNames = {getString(R.string.choose), getString(R.string.president), getString(R.string.member), getString(R.string.vice_president), getString(R.string.content_dev),
//                getString(R.string.creative_head), getString(R.string.production_deisgner), getString(R.string.tresurer), getString(R.string.hrm),
//                getString(R.string.SEM), getString(R.string.dance_manager), getString(R.string.logistics_manager), getString(R.string.yt_manager)};
//        loadSpinner(positionNames);

    }

    private void initAdapter() {
        teamMembersArrayAdapter = new ArrayAdapter<TeamMembers>(this, R.layout.members_name_item, teamMembersArrayList) {
            @NonNull
            @Override
            public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.members_name_item, parent, false);
                }

                createList(teamMembersArrayList.get(position), convertView);

                ImageView delete = convertView.findViewById(R.id.delete);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteItem(teamMembersArrayList, position);
                    }
                });

                return convertView;
            }

            @Override
            public int getCount() {
                return teamMembersArrayList.size();
            }

            @Nullable
            @Override
            public TeamMembers getItem(int position) {
                return teamMembersArrayList.get(position);
            }
        };
        teamMembersLv.setAdapter(teamMembersArrayAdapter);

        updateUi();
    }

    private void loadTeamMembersList(String teamMemId) {
        new DbHelper().getTeamMembers(teamMemId, new DbHelper.TeamMembersCallback() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    teamMembersArrayList.add(dataSnapshot1.getValue(TeamMembers.class));
                    AppHelper.showToast(AddTeamMembersActivity.this, "Team members added!");
                    initAdapter();
                }
            }

            @Override
            public void onFail(String error) {
                initAdapter();
                AppHelper.showToast(AddTeamMembersActivity.this, error);
            }
        });

    }

    private void deleteItem(ArrayList<TeamMembers> teamMembersArrayList, int position) {
        teamMembersArrayList.remove(position);
        teamMembersArrayAdapter.notifyDataSetChanged();
        updateUi();
    }

    private void createList(TeamMembers teamMembers, View convertView) {
        if (teamMembers == null || convertView == null) {
            return;
        }

        CircularImageView circularImageView = convertView.findViewById(R.id.member_image);

        Glide.with(convertView).load(team.gettLogo()).into(circularImageView);

        TextView memberNameTv = convertView.findViewById(R.id.mem_name);
        memberNameTv.setText(teamMembers.getTeamMemberName());

        TextView membersPositionTv = convertView.findViewById(R.id.mem_position);
        membersPositionTv.setText(teamMembers.getTeamMemberPosition());

        AppHelper.print("Trying to create members list:\n " + teamMembers.getTeamMemberName() + "\t" + teamMembers.getTeamMemberPosition());
    }

    private void updateUi() {
        teamMembersLv.setVisibility(teamMembersArrayList.size() != 0 ? View.VISIBLE : View.GONE);
        noMembersTv.setVisibility(teamMembersArrayList.size() == 0 ? View.VISIBLE : View.GONE);

        if (team.gettMemCount() != null) {
            membersCountDisplayTv.setText(String.format(Locale.getDefault(), "%s %d", getString(R.string.number_of_members), Integer.valueOf(team.gettMemCount()) - teamMembersArrayList.size()));
            membersCountDisplayTv.setVisibility(View.VISIBLE);
        }
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
                checkAndProceedInsertion();
                break;
            case android.R.id.home:
                askAndGoBack();
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

//    private void loadSpinner(final String[] positionNames) {
//        ArrayAdapter positionAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, positionNames);
//        positionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        teamMemberPositionSpinner.setAdapter(positionAdapter);
//
//        teamMemberPositionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                memberPosition = position;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                showSnack(getString(R.string.choose_position));
//            }
//        });
//    }

    private void checkAndAddMember() {
        memberName = teamMemberNameEd.getText().toString().trim();
        if (TextUtils.isEmpty(memberName)) {
            showSnack(getString(R.string.enter_name));
            return;
        }

        memberDesignation = designationEd.getText().toString().trim();
        if (TextUtils.isEmpty(memberDesignation)) {
            showSnack(getString(R.string.choose_position));
            return;
        }

        if (teamMembersCount == teamMembersArrayList.size()) {
            showSnack(getString(R.string.maximum_number_reached));
            return;
        }

        if (teamMembersArrayList.size() == 0) {
            addToList(memberName, memberDesignation);
        } else {
            for (TeamMembers teamMembers1 : new ArrayList<>(teamMembersArrayList)) {
                if (teamMembers1.getTeamMemberName().equalsIgnoreCase(memberName)) {
                    showToast(this, getString(R.string.member_exist));
                    return;
                } else {
                    addToList(memberName, memberDesignation);
                    return;
                }
            }
        }
    }

    private void addToList(String memberName, String designation) {
        TeamMembers teamMembers = new TeamMembers();
        teamMembers.setTeamMemberName(memberName);
        teamMembers.setTeamMemberPosition(designation);

        teamMembersArrayList.add(teamMembers);
        teamMembersArrayAdapter.notifyDataSetChanged();

        if (teamMembersArrayList.size() != 0) {
            AppHelper.print("Adding team members: " + teamMembers.getTeamMemberName() + "\t" +
                    teamMembers.getTeamMemberPosition());
        }

        updateUi();

        teamMemberNameEd.setText("");
        designationEd.setText("");

        AddTeamMembersActivity.this.memberName = null;
        AddTeamMembersActivity.this.memberDesignation = "";
    }

    private void checkAndProceedInsertion() {
        if (teamMembersCount != teamMembersArrayList.size()) {
            showCountMissingAlert();
        } else {
            insertIntoDb();
        }
    }

    private void showCountMissingAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.hello_admin));
        alertDialog.setMessage(getString(R.string.members_count_not_matching));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                insertIntoDb();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        if (!isFinishing() && !alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    private void insertIntoDb() {
        if(isImageChanged) {
            Uri teamLogoUri = Uri.parse(team.gettLogo());
            String teamLogoName = team.gettName() + "_Logo";

            showProgress(getString(R.string.uploading_data));

            final StorageReference storageRef = storageReference.child("TeamLogo/" + teamLogoName);

            uploadTask = storageRef.putFile(teamLogoUri);

            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        showInfoAlert(getString(R.string.upload_failed));
                        AppHelper.print("Task unsuccessful!");
                        throw Objects.requireNonNull(task.getException());
                    }
                    cancelProgress();

                    return storageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        uploadedLogoUri = task.getResult();
                        AppHelper.print("Upload " + uploadedLogoUri);
                        cancelProgress();

                        if (uploadedLogoUri != null) {
                            String photoStringLink = uploadedLogoUri.toString();
                            AppHelper.print("Uploaded logo Uri " + photoStringLink);

                            onImageUploaded(uploadedLogoUri.toString());
                        } else {
                            cancelProgress();
                            AppHelper.print("Image uploaded but uri null");
                        }
                    } else {
                        cancelProgress();
                        showInfoAlert(getString(R.string.upload_failed));
                    }
                }
            });
        } else {
            onImageUploaded(team.gettLogo());
        }
    }

    private void onImageUploaded(String logoUri) {
        showProgress(isTeamEdit ? getString(R.string.updating_team_in_progress) : getString(R.string.registering_team_in_progress));

        team.settLogo(logoUri);

        String teamId = isTeamEdit ? team.gettId() : teamDatabaseReference.push().getKey();
        final String teamMembersId = isTeamEdit ? team.gettMemId() : membersDbReference.push().getKey();

        team.settId(teamId);
        team.settMemId(teamMembersId);

        AppHelper.print("teamId: "+team.gettId());
        AppHelper.print("gettMemId: "+team.gettMemId());

        if (isTeamEdit) {
            getDbHelper().updateTeamData(team, new DbHelper.UpdateCallback() {
                @Override
                public void onSuccess() {
                    updateTeamMembers(teamMembersId);
                }

                @Override
                public void onFailed() {
                    cancelProgress();
                    showErrorDialog();
                }
            });
        } else {

            teamDatabaseReference.child(team.gettId()).setValue(team,
                    (databaseError, databaseReference) -> {
                        if (databaseError == null) {
                            insertTeamMembers(teamMembersId);
                        } else {
                            cancelProgress();

                            AppHelper.print("Database error: " + databaseError.getMessage());
                            showErrorDialog();
                        }
                    });
        }
    }

    private void showErrorDialog() {
        showSimpleAlert(getString(R.string.db_error), getString(R.string.ok), (dialogInterface, which) -> {
            goHome();
            dialogInterface.dismiss();
        });
    }

    private void updateTeamMembers(String teamMembersId) {
        getDbHelper().updateTeamMembers(teamMembersId, teamMembersArrayList, new DbHelper.UpdateCallback() {
            @Override
            public void onSuccess() {
                cancelProgress();
                showSuccessDialog();
            }

            @Override
            public void onFailed() {
                cancelProgress();
                showErrorDialog();
            }
        });
    }

    private void showSuccessDialog() {
        showSimpleAlert(getString(R.string.team_added_successfully), getString(R.string.ok), new SimpleAlert() {
            @Override
            public void onBtnClicked(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
                goHome();
            }
        });
    }

    private void insertTeamMembers(String teamMembersId) {

        membersDbReference.child(teamMembersId).setValue(teamMembersArrayList, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                cancelProgress();

                if (databaseError == null) {
                    showSuccessDialog();

                } else {
                    AppHelper.print("Database error: " + databaseError.getMessage());

                    showErrorDialog();
                }
            }
        });
    }

    private void goHome() {
        goTo(this, MainActivity.class, true);
    }

    private void askAndGoBack() {
        showOkCancelAlert(getString(R.string.exit_without_addning_team), new OkCancelAlert() {
            @Override
            public void onOkClicked(DialogInterface dialogInterface, int which) {
                goHome();
            }

            @Override
            public void onCancelClicked(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        askAndGoBack();
    }
}


