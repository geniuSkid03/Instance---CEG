package com.inspiregeniussquad.handstogether.appFragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appActivities.AddTeamMembersActivity;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appData.Team;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class AddTeamFragment extends SuperFragment {

    private AppCompatButton addTeamBtn;
    private ImageView teamLogoIv;
    private ImageButton imgLoadBtn, reduceTeamMemberBtn, addTeamMemberBtn;
    private EditText teamNameEd, teamMottoEd, teamMembersCountEd;

    private Uri teamImgUri, finalTeamImgUri;

    private static final int CHOOSE_FILE = 101;
    private static final int OPEN_CAMERA = 100;

    private String teamName, teamMotto, teamMembersCount, teamMembersNames;
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
        teamMembersCountEd = view.findViewById(R.id.team_members_count);
        addTeamMemberBtn = view.findViewById(R.id.minus_team_member);
        reduceTeamMemberBtn = view.findViewById(R.id.add_team_member);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.next_btn:
                        onFirstStepCompleted();
//                        checkAndAddTeamData();
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
//        final CharSequence[] items = {getString(R.string.open_camera), getString(R.string.pick_from_gallery), getString(R.string.cancel)};
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("Add Photo!");
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//        boolean result = dataStorage.getBoolean(Keys.PERMISSIONS_GRANTED);
//                if (items[item].equals(getString(R.string.open_camera))) {
//                    if (result) {
//                        getPermissionAndOpenCamera();
//                    } else {
//                        showPermissionDenied();
//                    }
//                } else if (items[item].equals(getString(R.string.pick_from_gallery))) {
        if (dataStorage.getBoolean(Keys.PERMISSIONS_GRANTED)) {
            galleryIntent();
        } else {
            showToast(getString(R.string.permissoin_denied_string));
        }
//                } else if (items[item].equals(getString(R.string.cancel))) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), CHOOSE_FILE);
    }

//    private void getPermissionAndOpenCamera() {
//        permissionHelper.initPermissions(new String[]{Manifest.permission.CAMERA,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE});
//        if (!permissionHelper.isAllPermissionAvailable()) {
//            permissionHelper.askAllPermissions();
//        } else if (permissionHelper.isAllPermissionAvailable()) {
//            openCamera();
//        }
//    }

//    private void openCamera() {
//        if (getActivity() == null) {
//            AppHelper.print("getActivity null");
//            return;
//        }
//
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        ContentValues values = new ContentValues();
//
//        values.put(MediaStore.Images.Media.TITLE, "Team Details");
//        values.put(MediaStore.Images.Media.DESCRIPTION, "Team Logo");
//        teamImgUri = getActivity().getContentResolver().insert(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, teamImgUri);
//        getActivity().startActivityForResult(intent, OPEN_CAMERA);
//
//    }

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
        Team team = new Team(teamName, teamMotto, teamImgUri.toString(), null, teamMembersCount);
        goTo(getActivity(), AddTeamMembersActivity.class, true, Keys.TEAM, gson.toJson(team));
    }

//    private void checkAndAddTeamData() {
//        if (isAllDataAvailable()) {
//            uploadTeamDataToDb();
//        }
//    }

    private boolean isAllDataAvailable() {
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

        teamMembersCount = teamMembersCountEd.getText().toString().trim();
        if (!TextUtils.isDigitsOnly(teamMembersCount) && TextUtils.isEmpty(teamMembersCount)) {
            showToast(getString(R.string.enter_team_count));
            return false;
        }

//        teamMembersNames = teamMembersNamesEd.getText().toString().trim();
//        if (TextUtils.isEmpty(teamMembersNames)) {
//            showToast(getString(R.string.enter_team_members_names));
//            return false;
//        }

        return true;
    }

    //pre checking if team data already exists
//    private void uploadTeamDataToDb() {
//
//        showProgress(getString(R.string.validating_info));
//
//        teamDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    checkWithExistingTeams((Map<String, Object>) dataSnapshot.getValue());
//                } else {
//                    //no team has registered
//                    //creating new Team
//                    insertIntoStorage(new Team(teamName, teamMotto, teamImgUri.toString(), teamMembersNames, String.valueOf(teamMembersCount)));
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                cancelProgress();
//                //show error as db error
//                showToast(getString(R.string.team_already_exist));
//            }
//        });
//    }

//    private void checkWithExistingTeams(Map<String, Object> value) {
//        ArrayList<String> teamArrayList = new ArrayList<>();
//
//        //getting team data from database
//        for (Map.Entry<String, Object> entry : value.entrySet()) {
//            Map teamMap = (Map) entry.getValue();
//            String teamName = (String) teamMap.get(Keys.ATTR_NAME);
//            AppHelper.print("Name: " + teamName);
//
//            teamArrayList.add(teamName.toLowerCase());
//        }
//
//        //checking if team name already exists in database
//        if (teamArrayList.size() != 0) {
//            if (teamArrayList.contains(teamName.toLowerCase())) {
//                cancelProgress();
//
//                showSimpleAlert(getString(R.string.team_already_exist), getString(R.string.ok), new SimpleAlert() {
//                    @Override
//                    public void onBtnClicked(DialogInterface dialogInterface, int which) {
//                        dialogInterface.dismiss();
//                    }
//                });
//            } else {
//                insertIntoStorage();
//            }
//        } else {
//            insertIntoStorage();
//        }
//    }

//    private void insertIntoStorage() {
//        Team team = new Team(teamName, teamMotto, teamImgUri.toString(), teamMembersNames, String.valueOf(teamMembersCount));
//
//        insertIntoTeam(team);
//    }

//    private void insertIntoStorage(Team team) {
//        showProgress(getString(R.string.creating_team_profile));
//
//        AppHelper.print("Team: " + team.getTeamName() + "\t" + team.getTeamMotto() + "\t" + team.getTeamLogoUri());
//
//        final StorageReference storageRef = teamLogoStorageReference.child("TeamLogo/" + teamName.trim() + "_Logo");
//
//        uploadTask = storageRef.putFile(teamImgUri);
//
//        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//            @Override
//            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                if (!task.isSuccessful()) {
//                    cancelProgress();
//                    AppHelper.showToast(getActivity(), getString(R.string.db_error));
//                    throw Objects.requireNonNull(task.getException());
//                }
//                return storageRef.getDownloadUrl();
//            }
//        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//            @Override
//            public void onComplete(@NonNull Task<Uri> task) {
//                if (task.isSuccessful()) {
//                    finalTeamImgUri = task.getResult();
//                    cancelProgress();
//
//                    if (finalTeamImgUri != null) {
//                        insertIntoStorage();
//                    } else {
//                        AppHelper.print("Team logo uri null");
//                        showToast(getString(R.string.db_error));
//                    }
//
//                } else {
//                    cancelProgress();
//                }
//            }
//        });
//    }

//    private void insertIntoTeam(Team team) {
//        teamDbReference.child(Objects.requireNonNull(teamDbReference.push().getKey())).setValue(team, new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
//                cancelProgress();
//
//                if (databaseError == null) {
//                    showSimpleAlert(getString(R.string.team_added_successfully), getString(R.string.ok), new SimpleAlert() {
//                        @Override
//                        public void onBtnClicked(DialogInterface dialogInterface, int which) {
//                            dialogInterface.dismiss();
//                        }
//                    });
//                } else {
//                    AppHelper.print("Database error: " + databaseError.getMessage());
//
//                    showSimpleAlert(getString(R.string.db_error), getString(R.string.ok), new SimpleAlert() {
//                        @Override
//                        public void onBtnClicked(DialogInterface dialogInterface, int which) {
//                            dialogInterface.dismiss();
//                        }
//                    });
//                }
//            }
//        });
//    }

}
