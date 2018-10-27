package com.inspiregeniussquad.handstogether.appActivities;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appUtils.ImageHelper;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.OnClick;

public class ProfileUpdatingActivity extends SuperCompatActivity {

    @BindView(R.id.choose_image)
    ImageButton chooseImgBtn;

    @BindView(R.id.user_profile)
    ImageView userProfileIv;

    @BindView(R.id.name)
    TextInputEditText userNameEd;

    @BindView(R.id.email)
    TextInputEditText userEmailEd;

    @BindView(R.id.radio_grp)
    RadioGroup genderRdGrp;

    @BindView(R.id.male_rb)
    RadioButton maleRdBtn;

    @BindView(R.id.female_rb)
    RadioButton femaleRdBtn;

    @BindView(R.id.update_profile)
    AppCompatButton updateProfileBtn;

    private final int CHOOSE_IMAGE = 90;
    private final int OPEN_CAMERA = 100;

    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    private Uri userImgUri;
    private String userImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_updation);
    }

    @OnClick({R.id.choose_image, R.id.update_profile})
    public void onBtnClicked(View view) {
        switch (view.getId()) {
            case R.id.choose_image:
                showChooserDialog();
                break;
            case R.id.update_profile:
                updateProfile();
                break;
        }
    }

    private void showChooserDialog() {
        final CharSequence[] items = {getString(R.string.open_camera), getString(R.string.pick_from_gallery), getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_photo));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = dataStorage.getBoolean(Keys.PERMISSIONS_GRANTED);
                if (items[item].equals(getString(R.string.open_camera))) {
                    if (result) {
                        getPermissionAndOpenCamera();
                    } else {
                        showPermissionDenied();
                    }
                } else if (items[item].equals(getString(R.string.pick_from_gallery))) {
                    if (result) {
                        galleryIntent();
                    } else {
                        showPermissionDenied();
                    }
                } else if (items[item].equals(getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        if(!isFinishing()){
            builder.show();
        }
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), CHOOSE_IMAGE);
    }

    private void getPermissionAndOpenCamera() {
        permissionHelper.initPermissions(new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE});
        if (!permissionHelper.isAllPermissionAvailable()) {
            permissionHelper.askAllPermissions();
        } else if (permissionHelper.isAllPermissionAvailable()) {
            openCamera(OPEN_CAMERA);
        }
    }

    private void openCamera(int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues values = new ContentValues();

        if (requestCode == CHOOSE_IMAGE) {
            values.put(MediaStore.Images.Media.TITLE, "User Details");
            values.put(MediaStore.Images.Media.DESCRIPTION, "User image");
            userImgUri = getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, userImgUri);
            startActivityForResult(intent, requestCode);
        }
    }

    private void showPermissionDenied() {
        showInfoAlert("Permissions to access camera or gallery was not available, go to app settings and enable all permissions");
    }

    private void updateProfile() {
        //todo update profile to database
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isAboveM()) {
            permissionHelper.initPermissions(permissions);
            if (!permissionHelper.isAllPermissionAvailable()) {
                permissionHelper.askAllPermissions();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (!permissionHelper.isAllPermissionGranted(grantResults)) {
            permissionHelper.initPermissions(permissionHelper.getUnGrantedPermissionArray());
            permissionHelper.askAllPermissions();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if(requestCode == OPEN_CAMERA) {
                Picasso.get().load(userImgUri).into(userProfileIv);

                userImagePath = getImagePath(userImgUri);
            } else if (requestCode == CHOOSE_IMAGE) {
                userImgUri = data.getData();

                userImagePath = ImageHelper.getPath(this, userImgUri);

                Picasso.get().load(userImgUri).into(userProfileIv);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
