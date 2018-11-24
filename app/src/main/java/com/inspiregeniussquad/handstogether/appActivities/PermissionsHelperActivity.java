package com.inspiregeniussquad.handstogether.appActivities;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ListView;

import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appAdapters.PermissionsAdapter;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appUtils.PermissionHelper;

import butterknife.BindView;
import butterknife.OnClick;

public class PermissionsHelperActivity extends SuperCompatActivity {

    @BindView(R.id.continue_btn)
    AppCompatButton continueBtn;

    @BindView(R.id.permissions_list)
    ListView permissionLv;

    protected String[] requiredPermissions = { Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions_helper);

        int[] permissionIcons = {R.drawable.permission_phone,
                R.drawable.permission_sms,
                R.drawable.permission_storage,
                R.drawable.permission_camera};

        String[] permissionsTitle = {getString(R.string.phone_title),
                getString(R.string.sms_title),
                getString(R.string.storage_title),
                getString(R.string.camera_title)};

        String[] permissionHint = {getString(R.string.phone_permsn_hint),
                getString(R.string.sms_permsn_hint),
                getString(R.string.storage_permsn_hint),
                getString(R.string.camera_permsn_hint)};

        PermissionsAdapter permissionsAdapter = new PermissionsAdapter(this, permissionIcons, permissionsTitle, permissionHint);
        permissionLv.setAdapter(permissionsAdapter);
    }

    @OnClick({R.id.continue_btn})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.continue_btn:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    askPermissions();
                } else {
                    performFunctions();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void askPermissions() {
        permissionHelper.initPermissions(requiredPermissions);
        if (!permissionHelper.isAllPermissionAvailable()) {
            permissionHelper.askAllPermissions();
        } else {
            performFunctions();
        }
    }

    private void performFunctions() {
        dataStorage.saveBoolean(Keys.PERMISSIONS_GRANTED, true);
        checkDataAndOpen();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (permissionHelper.isAllPermissionGranted(grantResults)) {
            performFunctions();
        } else {
            permissionHelper.initPermissions(permissionHelper.getUnGrantedPermissionArray());
            permissionHelper.askAllPermissions();
        }
    }
}
