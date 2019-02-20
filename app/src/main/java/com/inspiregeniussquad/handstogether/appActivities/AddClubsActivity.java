package com.inspiregeniussquad.handstogether.appActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appData.Clubs;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;

import butterknife.BindView;

public class AddClubsActivity extends SuperCompatActivity {

    @BindView(R.id.llMainContents)
    CardView addClubsCv;

    @BindView(R.id.club_name)
    EditText clubNameEd;

    @BindView(R.id.club_image)
    ImageView clubIv;

    @BindView(R.id.club_image_choose)
    ImageView clubImgChooserIv;

    @BindView(R.id.save_club)
    FloatingActionButton saveFab;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private String clubName = "", clubImg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clubs);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        saveFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAllOk()) {
                    updateClubsToDB(new Clubs(clubName, clubImg));
                }
            }
        });

        clubImgChooserIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataStorage.getBoolean(Keys.PERMISSIONS_GRANTED)) {
                    openGallery();
                } else {
                    showToast(AddClubsActivity.this, getString(R.string.permissoin_denied_string));
                }
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);
    }

    private boolean isAllOk() {
        clubName = clubNameEd.getText().toString().trim();
        if (TextUtils.isEmpty(clubName)) {
            showToast(this, getString(R.string.enter_name));
            return false;
        }

        if (TextUtils.isEmpty(clubImg)) {
            showToast(this, getString(R.string.choose_image));
            return false;
        }

        return true;
    }

    private Uri uploadedImg;

    private void updateClubsToDB(final Clubs clubs) {
        String clubsImgName = clubs.getClubsName() + "_icon";

        showProgress(getString(R.string.uploading_image));

        final StorageReference storageRef = storageReference.child("ClubsIcon/" + clubsImgName);

        uploadTask = storageRef.putFile(Uri.parse(clubs.getClubsImgUrl()));

        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    showInfoAlert(getString(R.string.upload_failed));
                    AppHelper.print("Task unsuccessful!");
                    throw task.getException();
                }
                cancelProgress();

                return storageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    uploadedImg = task.getResult();
                    AppHelper.print("Upload " + uploadedImg);
                    cancelProgress();

                    if (uploadedImg != null) {
                        String photoStringLink = uploadedImg.toString();
                        AppHelper.print("Uploaded logo Uri " + photoStringLink);

                        onImageUploaded(uploadedImg, clubs);
                    } else {
                        cancelProgress();
                        AppHelper.print("Image uploaded but uri null");
                    }
                } else {
                    AppHelper.print("Task failed: " + task.getException());
                    cancelProgress();
                    showInfoAlert(getString(R.string.upload_failed));
                }
            }
        });
    }

    private void onImageUploaded(Uri uploadedImg, Clubs clubs) {
        showProgress(getString(R.string.updating_club_info));

        String clubId = clubsDbRef.push().getKey();

        if (clubId != null) {
            Clubs clubs1 = new Clubs(clubs.getClubsName(), String.valueOf(uploadedImg));
            clubs1.setClubsId(clubId);

            clubsDbRef.child(clubId).setValue(clubs1, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    cancelProgress();

                    if (databaseError == null) {
                        showSimpleAlert(getString(R.string.club_added_successfully), getString(R.string.ok),
                                new SimpleAlert() {
                                    @Override
                                    public void onBtnClicked(DialogInterface dialogInterface, int which) {
                                        dialogInterface.dismiss();
                                        clearUi();
                                    }
                                });
                    } else {
                        AppHelper.print("Database error: " + databaseError.getMessage());

                        showSimpleAlert(getString(R.string.db_error), getString(R.string.ok),
                                new SimpleAlert() {
                                    @Override
                                    public void onBtnClicked(DialogInterface dialogInterface, int which) {
                                        dialogInterface.dismiss();
                                        clearUi();
                                    }
                                });
                    }
                }
            });
        } else {
            showToast(this, getString(R.string.some_error_occurred));
            AppHelper.print("Club id null, cannot insert value");
        }
    }

    private void clearUi() {
        clubIv.setImageResource(0);
        clubNameEd.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                if (data != null) {
                    clubImg = String.valueOf(data.getData());
                    Glide.with(this).load(data.getData()).into(clubIv);
                }
            }
        }
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
