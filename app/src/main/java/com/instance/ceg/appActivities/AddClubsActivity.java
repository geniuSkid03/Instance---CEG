package com.instance.ceg.appActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
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
import com.google.gson.Gson;
import com.instance.ceg.R;
import com.instance.ceg.appData.Clubs;
import com.instance.ceg.appData.Keys;
import com.instance.ceg.appHelpers.DbHelper;
import com.instance.ceg.appUtils.AppHelper;

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

    private String clubName = "", clubImg = "", clubId ="";

    private boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clubs);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        String clubsStr = getIntent().getStringExtra("Clubs");
        if(!TextUtils.isEmpty(clubsStr)){
            Clubs clubs = new Gson().fromJson(clubsStr, Clubs.class);
            if(clubs != null) {
                isEdit = true;
                updateViews(clubs);
            }
        }

        saveFab.setOnClickListener(v -> {
            if (isAllOk()) {
                if(isEdit) {
                    AppHelper.printLog("SaveFab click isEditClubs");
                    updateClub(clubId, clubName, clubImg);
                } else {
                    AppHelper.printLog("SaveFab click addClubs");
                    insertClub(new Clubs(clubName, clubImg));
                }
            }
        });

        clubImgChooserIv.setOnClickListener(v -> {
            if (dataStorage.getBoolean(Keys.PERMISSIONS_GRANTED)) {
                openGallery();
            } else {
                showToast(AddClubsActivity.this, getString(R.string.permissoin_denied_string));
            }
        });
    }

    private void updateClub(String clubId, String clubName, String clubImg) {
        showProgress("Updating club information");

        Clubs clubs = new Clubs();
        clubs.setClubsId(clubId);
        clubs.setClubsName(clubName);
        clubs.setClubsImgUrl(clubImg);

        new DbHelper().insertClubs(clubs, false, isEditAndImageEdited, new DbHelper.ClubsCallback() {
            @Override
            public void onSuccess() {
                cancelProgress();
                showSimpleAlert(getString(R.string.club_added_successfully), getString(R.string.ok),
                        (dialogInterface, which) -> {
                            dialogInterface.dismiss();
                            clearUi();
                        });
            }

            @Override
            public void onFailed() {
                cancelProgress();
                showSimpleAlert(getString(R.string.db_error), getString(R.string.ok),
                        (dialogInterface, which) -> {
                            dialogInterface.dismiss();
                            clearUi();
                        });
            }
        });
    }

    private void updateViews(Clubs clubs) {
        clubNameEd.setText(clubs.getClubsName());
        Glide.with(this).load(clubs.getClubsImgUrl()).into(clubIv);

        clubName = clubs.getClubsName();
        clubImg = clubs.getClubsImgUrl();
        clubId = clubs.getClubsId();
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

    private boolean isEditAndImageEdited = false;

    private void insertClub(final Clubs clubs) {
        showProgress("Processing clubs information...");

        new DbHelper().insertClubs(clubs,true, false, new DbHelper.ClubsCallback() {
            @Override
            public void onSuccess() {
                cancelProgress();

                showSimpleAlert(getString(R.string.club_added_successfully), getString(R.string.ok),
                        (dialogInterface, which) -> {
                            dialogInterface.dismiss();
                            clearUi();
                        });
            }

            @Override
            public void onFailed() {
                cancelProgress();

                showSimpleAlert(getString(R.string.db_error), getString(R.string.ok),
                        (dialogInterface, which) -> {
                            dialogInterface.dismiss();
                            clearUi();
                        });
            }
        });
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
                    if(isEdit) {
                        isEditAndImageEdited = true;
                    }
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
