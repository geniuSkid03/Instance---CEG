package com.inspiregeniussquad.handstogether.appUtils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.inspiregeniussquad.handstogether.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class PermissionHelper {


    private static final int REQ_CODE_FOR_PERMISSION = 1;
    public Activity activity;

    private ArrayList<String> requiredPermissions;
    private ArrayList<String> ungrantedPermissions = new ArrayList<>();
    private boolean isAllPermissionsGranted = false;

    private AlertDialog permissionHelper;


    public PermissionHelper(Activity activity) {
        this.activity = activity;
        permissionHelper = new AlertDialog.Builder(activity).create();
    }

    public void initPermissions(String[] permissions) {
        requiredPermissions = new ArrayList<>();
        Collections.addAll(requiredPermissions, permissions);
        AppHelper.print("PermissionsHelperActivity (init premissions): " + Arrays.toString(requiredPermissions.toArray()));
    }

    public boolean isAllPermissionGranted(int[] grantResults) {
        for (int i : grantResults) {
            if (i != PackageManager.PERMISSION_GRANTED) {
                isAllPermissionsGranted = false;
                return false;
            }
        }
        isAllPermissionsGranted = true;
        return true;
    }

    public boolean isPermissionAvailable(String permission) {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void askPermission(String permission) {
        ActivityCompat.requestPermissions(activity, new String[]{permission}, REQ_CODE_FOR_PERMISSION);
    }

    public boolean isAllPermissionAvailable() {
        boolean isAllPermissionAvailable = true;
        for (String permission : requiredPermissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                isAllPermissionAvailable = false;
                break;
            }
        }
        return isAllPermissionAvailable;
    }

    public void requestPermissionsIfDenied() {
        ungrantedPermissions = getUnGrantedPermissionsList();
        if (canShowPermissionRationaleDialog()) {
            showMessageOKCancel(activity.getResources().getString(R.string.permission_message),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            askAllPermissions();
                        }
                    });
            return;
        }
        askAllPermissions();
    }

    public void askAllPermissions() {
        ungrantedPermissions = getUnGrantedPermissionsList();
        if (ungrantedPermissions.size() > 0) {
            AppHelper.print("Asking all permissions");
            AppHelper.print("ungranted permissions: " + Arrays.toString(ungrantedPermissions.toArray()));

            ActivityCompat.requestPermissions(activity,
                    ungrantedPermissions.toArray(new String[ungrantedPermissions.size()]), REQ_CODE_FOR_PERMISSION);
        }
    }


    public void requestPermissionsIfDenied(final String permission) {
        if (canShowPermissionRationaleDialog(permission)) {
            showMessageOKCancel(activity.getResources().getString(R.string.permission_message),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            askPermission(permission);
                            //askAllPermissions();
                        }
                    });
            return;
        }
//        askAllPermissions();
        askPermission(permission);
    }

    private boolean canShowPermissionRationaleDialog() {
        boolean shouldShowRationale = false;
        for (String permission : ungrantedPermissions) {
            boolean shouldShow = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
            if (shouldShow) {
                shouldShowRationale = true;
            }
        }
        return shouldShowRationale;
    }

    private boolean canShowPermissionRationaleDialog(String permission) {
        boolean shouldShowRationale = false;
        boolean shouldShow = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
        if (shouldShow) {
            shouldShowRationale = true;
        }
        return shouldShowRationale;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {

        permissionHelper.setTitle(activity.getString(R.string.permission_title));
        permissionHelper.setMessage(message);
        permissionHelper.setButton(AlertDialog.BUTTON_POSITIVE, activity.getString(R.string.ok), okListener);
        permissionHelper.setButton(AlertDialog.BUTTON_NEGATIVE, activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finish();
            }
        });

        if (permissionHelper != null && !permissionHelper.isShowing()) {
            permissionHelper.show();
        }

    }

    public ArrayList<String> getUnGrantedPermissionsList() {
        ArrayList<String> list = new ArrayList<>();
        for (String permission : requiredPermissions) {
            int result = ActivityCompat.checkSelfPermission(activity, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                list.add(permission);
            }
        }
        return list;
    }

    public ArrayList<String> checkUngrantedPermissions(ArrayList<String> requiredPermissions) {
        ArrayList<String> arrayList = new ArrayList<>();
        //array = new String[requiredPermissions.size()];

//        array = requiredPermissions.toArray(new String[requiredPermissions.size()]);

        for (String permission : requiredPermissions) {
            if (!isPermissionAvailable(permission)) {
                arrayList.add(permission);
            }
        }
        return arrayList;
    }

    public String[] getUnGrantedPermissionArray() {
        return getUnGrantedPermissionsList().toArray(new String[getUnGrantedPermissionsList().size()]);
    }

    public boolean isRationaleNeeded() {
        return ungrantedPermissions.size() != 0;
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivity(intent);
    }
}
