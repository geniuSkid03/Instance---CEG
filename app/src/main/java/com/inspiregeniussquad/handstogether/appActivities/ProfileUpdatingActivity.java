package com.inspiregeniussquad.handstogether.appActivities;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.reflect.TypeToken;
import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appData.Admin;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appData.Users;
import com.inspiregeniussquad.handstogether.appHelpers.DbHelper;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;
import com.inspiregeniussquad.handstogether.appUtils.ImageHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class ProfileUpdatingActivity extends SuperCompatActivity {

    @BindView(R.id.user_profile)
    ImageView userProfileIv;

    @BindView(R.id.name)
    EditText userNameEd;

    @BindView(R.id.email)
    EditText userEmailEd;

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
    private String userImagePath, gender, userName,
            userEmail;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_updation);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setTitle(getString(R.string.update_profile));
        }

        genderRdGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.male_rb:
                        gender = Keys.MALE;
                        break;
                    case R.id.female_rb:
                        gender = Keys.FEMALE;
                        break;
                }
            }
        });

        loadCurrentProfile();
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

    private void loadCurrentProfile() {
        if (!TextUtils.isEmpty(dataStorage.getString(Keys.USER_NAME))) {
            userNameEd.setText(dataStorage.getString(Keys.USER_NAME));
        }

        if (!TextUtils.isEmpty(dataStorage.getString(Keys.USER_EMAIL))) {
            userEmailEd.setText(dataStorage.getString(Keys.USER_EMAIL));
        }

        if (!TextUtils.isEmpty(dataStorage.getString(Keys.USER_GENDER))) {
            switch (dataStorage.getString(Keys.USER_GENDER)) {
                case Keys.MALE:
                    maleRdBtn.setChecked(true);
                    userProfileIv.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_man));
                    break;
                case Keys.FEMALE:
                    femaleRdBtn.setChecked(true);
                    userProfileIv.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_girl));
                    break;
                case Keys.UNSPECIFIED:
                    userProfileIv.setBackground(ContextCompat.getDrawable(this, R.drawable.gender_unspecified));
                    break;
                default:
                    userProfileIv.setBackground(ContextCompat.getDrawable(this, R.drawable.gender_unspecified));
                    break;
            }
        }
    }

    @OnClick({R.id.update_profile})
    public void onBtnClicked(View view) {
        switch (view.getId()) {
            case R.id.update_profile:
                if (isAllDataAvailable()) {
                    updateProfile();
                }
                break;
        }
    }

    private boolean isAllDataAvailable() {
        userName = userNameEd.getText().toString().trim();
        userEmail = userEmailEd.getText().toString().trim();

        if (TextUtils.isEmpty(userName)) {
            AppHelper.showToast(this, "Enter your name!");
            return false;
        }

        if (TextUtils.isEmpty(userEmail)) {
            AppHelper.showToast(this, "Enter your email!");
            return false;
        }

        return true;
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
        if (!isFinishing()) {
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
            userImgUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, userImgUri);
            startActivityForResult(intent, requestCode);
        }
    }

    private void showPermissionDenied() {
        showInfoAlert("Permissions to access camera or gallery was not available, go to app settings and enable all permissions");
    }

    private void updateProfile() {
        showProgress("Updating your profile information...");

        final Users users = new Users();
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                AppHelper.print("FCM TOKEN: "+token);
                users.setFcmToken(token);
            }
        });

        users.setName(userName);
        users.setEmail(userEmail);

        dataStorage.saveString(Keys.USER_NAME, userName);
        dataStorage.saveString(Keys.USER_EMAIL, userEmail);
        dataStorage.saveString(Keys.USER_GENDER, gender);

        users.setUserId(dataStorage.getString(Keys.USER_ID));
        users.setMobile(dataStorage.getString(Keys.MOBILE));
        users.setGender(!TextUtils.isEmpty(gender) ? gender : getString(R.string.un_specified));
//        users.setLikedPosts(likedPostArrayList);
//        users.setCommentedPosts(commentedPostArrayList);
//        users.setBookmarkedPosts(bookmarkedPostArrayList);

        if (dataStorage.isDataAvailable(Keys.ADMIN_INFO)) {
            String admins = dataStorage.getString(Keys.ADMIN_INFO);
            ArrayList<Admin> adminArrayList = gson.fromJson(admins, new TypeToken<ArrayList<Admin>>() {
            }.getType());

            for (Admin admin : adminArrayList) {
                if (admin.getMobile().equalsIgnoreCase(dataStorage.getString(Keys.MOBILE))) {
                    users.setIsAdmin(admin.getPosition());
                } else {
                    users.setIsAdmin("-1");
                }
            }
        }

        new DbHelper().updateUser(users, new DbHelper.UserDbCallback() {
            @Override
            public void onUpdated() {
                loadCurrentProfile();
                cancelProgress();
                AppHelper.showToast(ProfileUpdatingActivity.this, "Your profile has been" +
                        "updated successfully!");
            }

            @Override
            public void onFailed() {
                cancelProgress();
                AppHelper.showToast(ProfileUpdatingActivity.this, "Some error occurred, " +
                        "try again later!");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*if (isAboveM()) {
            permissionHelper.initPermissions(permissions);
            if (!permissionHelper.isAllPermissionAvailable()) {
                permissionHelper.askAllPermissions();
            }
        }*/

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

        if (resultCode == RESULT_OK) {
            if (requestCode == OPEN_CAMERA) {
                Glide.with(this).load(userImgUri).into(userProfileIv);

                userImagePath = getImagePath(userImgUri);
            } else if (requestCode == CHOOSE_IMAGE) {
                userImgUri = data.getData();

                userImagePath = ImageHelper.getPath(this, userImgUri);

                Glide.with(this).load(userImgUri).into(userProfileIv);
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
