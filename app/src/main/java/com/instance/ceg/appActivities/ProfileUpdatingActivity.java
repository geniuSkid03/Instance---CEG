package com.instance.ceg.appActivities;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.instance.ceg.R;
import com.instance.ceg.appData.Admin;
import com.instance.ceg.appData.Keys;
import com.instance.ceg.appData.Users;
import com.instance.ceg.appHelpers.DbHelper;
import com.instance.ceg.appUtils.AppHelper;
import com.instance.ceg.appUtils.ImageHelper;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
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

        genderRdGrp.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.male_rb:
                    gender = Keys.MALE;
                    break;
                case R.id.female_rb:
                    gender = Keys.FEMALE;
                    break;
            }
        });

        loadCurrentProfile();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadCurrentProfile() {

        if(dataStorage.getString(Keys.USER_DATA) != null) {
            Users users = new Gson().fromJson(dataStorage.getString(Keys.USER_DATA), Users.class);
        }


        if (!TextUtils.isEmpty(dataStorage.getString(Keys.USER_NAME))) {
            userNameEd.setText(dataStorage.getString(Keys.USER_NAME));
            AppHelper.print("Name: "+dataStorage.getString(Keys.USER_NAME));
        } else {
            AppHelper.print("Name not available");

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
                default:
                    userProfileIv.setBackground(ContextCompat.getDrawable(this, R.drawable.gender_unspecified));
                    break;
            }
        }
    }

    @OnClick({R.id.update_profile})
    public void onBtnClicked(View view) {
        if (view.getId() == R.id.update_profile) {
            if (isAllDataAvailable()) {
                updateProfile();
            }
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

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            String token = instanceIdResult.getToken();
            AppHelper.print("FCM TOKEN: " + token);

            String adminValue = "-1";

            if (dataStorage.isDataAvailable(Keys.ADMIN_INFO)) {
                String admins = dataStorage.getString(Keys.ADMIN_INFO);
                ArrayList<Admin> adminArrayList = gson.fromJson(admins, new TypeToken<ArrayList<Admin>>() {
                }.getType());

                for (Admin admin : adminArrayList) {
                    if (admin.getMobile().equalsIgnoreCase(dataStorage.getString(Keys.MOBILE))) {
                        adminValue = admin.getPosition();
                    }
                }
            }

            Users users = new Users(dataStorage.getString(Keys.USER_ID),
                    userName, userEmail, dataStorage.getString(Keys.MOBILE), gender, adminValue, token);

            new DbHelper().updateUser(users, new DbHelper.UserDbCallback() {
                @Override
                public void onUpdated() {
                    saveProfileToLocal(users);
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
        });

    }

    private void saveProfileToLocal(Users users) {
        dataStorage.saveBoolean("is_profile_updated", true);

        dataStorage.saveString(Keys.USER_NAME, users.getName());
        dataStorage.saveString(Keys.MOBILE, users.getMobile());
        dataStorage.saveString(Keys.USER_EMAIL, users.getEmail());
        dataStorage.saveString(Keys.USER_GENDER, users.getGender());
        dataStorage.saveString(Keys.USER_DATA, gson.toJson(users));
        dataStorage.saveString(Keys.USER_ID, users.getUserId());
        dataStorage.saveString(Keys.ADMIN_VALUE, users.getIsAdmin());
        dataStorage.saveString(Keys.USER_FCM, users.getFcmToken());

        dataStorage.saveBoolean(Keys.IS_ONLINE, true);

//        dataStorage.saveString(Keys.USER_BOOKMARKS, gson.toJson(users.getBookmarkedPosts()));

        String admins = dataStorage.getString(Keys.ADMIN_INFO);
        ArrayList<Admin> adminArrayList = gson.fromJson(admins, new TypeToken<ArrayList<Admin>>() {
        }.getType());

        if (adminArrayList != null && adminArrayList.size() > 0) {
            for (Admin admin : adminArrayList) {
                if (admin.getMobile().equals(users.getMobile())) {
                    dataStorage.saveBoolean(Keys.IS_ADMIN, true);
                    dataStorage.saveString(Keys.ADMIN_VALUE, admin.getPosition());
                } else {
                    dataStorage.saveBoolean(Keys.IS_ADMIN, false);
                }
            }
        }

        loadCurrentProfile();
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
