package com.instance.ceg.appUtils.inAppUpdate;

import android.content.IntentSender;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.instance.ceg.BuildConfig;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public class InAppUpdateManager implements LifecycleObserver {

    private int requestCode = 4555;

    private String snackbarMessage = "An update has just been downloaded";
    private String snackbarAction = "RESTART";

    private boolean resumeUpdates = true;
    private boolean useCustomNotification = false;

    private AppCompatActivity activity;
    private View view;
    private Snackbar snackbar;

    private AppUpdateManager appUpdateManager;
    private InAppUpdateHandler handler;
    private InAppUpdateStatus inAppUpdateStatus = new InAppUpdateStatus();

    private InAppUpdateStatus.Constants.UpdateMode mode = InAppUpdateStatus.Constants.UpdateMode.IMMEDIATE;

    private static InAppUpdateManager instance;

    private InstallStateUpdatedListener installStateUpdatedListener = installState -> {
        inAppUpdateStatus.setInstallState(installState);

        reportStatus();

        if (installState.installStatus() == InstallStatus.DOWNLOADED) {
            popupSnackbarForUserConfirmation();
        }
    };


    public static InAppUpdateManager ImmediateUpdate(AppCompatActivity activity, int requestCode) {
        if (instance == null) {
            instance = new InAppUpdateManager(activity, requestCode);
        }
        return instance;
    }

    public static InAppUpdateManager FlexibleUpdate(AppCompatActivity activity, View view, int requestCode) {
        if (instance == null) {
            instance = new InAppUpdateManager(activity, view, requestCode);
        }
        return instance;
    }


    private InAppUpdateManager(AppCompatActivity activity, int requestCode) {
        this.activity = activity;
        this.requestCode = requestCode;
        mode = InAppUpdateStatus.Constants.UpdateMode.IMMEDIATE;
        init();
    }

    private InAppUpdateManager(AppCompatActivity activity, View view, int requestCode) {
        this.activity = activity;
        this.requestCode = requestCode;
        this.view = view;
        mode = InAppUpdateStatus.Constants.UpdateMode.FLEXIBLE;
        setupSnackbar();
        init();
    }


    private void init() {
        activity.getLifecycle().addObserver(this);

        appUpdateManager = AppUpdateManagerFactory.create(this.activity);

        if (mode == InAppUpdateStatus.Constants.UpdateMode.FLEXIBLE)
            appUpdateManager.registerListener(installStateUpdatedListener);

        checkForUpdate(false);
    }

    public InAppUpdateManager mode(InAppUpdateStatus.Constants.UpdateMode mode) {
        this.mode = mode;
        return this;
    }

    public InAppUpdateManager resumeUpdates(boolean resumeUpdates) {
        this.resumeUpdates = resumeUpdates;
        return this;
    }

    public InAppUpdateManager handler(InAppUpdateHandler handler) {
        this.handler = handler;
        return this;
    }

    public InAppUpdateManager useCustomNotification(boolean useCustomNotification) {
        this.useCustomNotification = useCustomNotification;
        return this;
    }

    public InAppUpdateManager snackbarMessage(String snackbarMessage) {
        this.snackbarMessage = snackbarMessage;
        setupSnackbar();
        return this;
    }

    public InAppUpdateManager snackBarAction(String snackbarAction) {
        this.snackbarAction = snackbarAction;
        setupSnackbar();
        return this;
    }


    public InAppUpdateManager snackBarActionColor(int color) {
        snackbar.setActionTextColor(color);
        return this;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        if (resumeUpdates)
            checkNewAppVersionState();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        unregisterListener();
    }

    public void checkForAppUpdate() {
        checkForUpdate(true);
    }

    public void completeUpdate() {
        appUpdateManager.completeUpdate();
    }

    private void checkForUpdate(final boolean startUpdate) {

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();


        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            inAppUpdateStatus.setAppUpdateInfo(appUpdateInfo);

            if (startUpdate) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                    // Request the update.
                    if (mode == InAppUpdateStatus.Constants.UpdateMode.FLEXIBLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                        // Start an update.
                        startAppUpdateFlexible(appUpdateInfo);
                    } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                        // Start an update.
                        startAppUpdateImmediate(appUpdateInfo);
                    }

                    printLog("checkForAppUpdate(): Update available. Version Code: " + appUpdateInfo.availableVersionCode());
                } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_NOT_AVAILABLE) {
                    printLog("checkForAppUpdate(): No Update available. Code: " + appUpdateInfo.updateAvailability());
                }
            }

            reportStatus();
        });

    }

    private void startAppUpdateFlexible(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.FLEXIBLE,
                    // The current activity making the update request.
                    activity,
                    // Include a request code to later monitor this update request.
                    requestCode);
        } catch (IntentSender.SendIntentException e) {
            printLog("error in startAppUpdateFlexible: " + e);
            reportUpdateError(InAppUpdateStatus.Constants.UPDATE_ERROR_START_APP_UPDATE_FLEXIBLE, e);
        }
    }


    private void checkNewAppVersionState() {
        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(appUpdateInfo -> {

                    inAppUpdateStatus.setAppUpdateInfo(appUpdateInfo);

                    //FLEXIBLE:
                    // If the update is downloaded but not installed,
                    // notify the user to complete the update.
                    if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                        popupSnackbarForUserConfirmation();
                        reportStatus();
                        printLog("checkNewAppVersionState(): resuming flexible update. Code: " + appUpdateInfo.updateAvailability());
                    }

                    //IMMEDIATE:
                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                        // If an in-app update is already running, resume the update.
                        startAppUpdateImmediate(appUpdateInfo);
                        printLog("checkNewAppVersionState(): resuming immediate update. Code: " + appUpdateInfo.updateAvailability());

                    }
                });
    }

    private void startAppUpdateImmediate(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    // The current activity making the update request.
                    activity,
                    // Include a request code to later monitor this update request.
                    requestCode);
        } catch (IntentSender.SendIntentException e) {
            printLog("error in startAppUpdateImmediate: " + e.getMessage());
            reportUpdateError(InAppUpdateStatus.Constants.UPDATE_ERROR_START_APP_UPDATE_IMMEDIATE, e);
        }
    }

    private void printLog(String message) {
        if (BuildConfig.DEBUG) {
            Log.e("Debug: ", message);
        }
    }

    private void popupSnackbarForUserConfirmation() {
        if (!useCustomNotification) {
            if (snackbar != null && snackbar.isShownOrQueued())
                snackbar.dismiss();

            if (snackbar != null) {
                snackbar.show();
            }
        }
    }

    private void setupSnackbar() {
        snackbar = Snackbar.make(view,
                snackbarMessage,
                Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction(snackbarAction, view -> {
            appUpdateManager.completeUpdate();
        });
    }

    private void unregisterListener() {
        if (appUpdateManager != null && installStateUpdatedListener != null)
            appUpdateManager.unregisterListener(installStateUpdatedListener);
    }

    private void reportUpdateError(int errorCode, Throwable error) {
        if (handler != null) {
            handler.onInAppUpdateError(errorCode, error);
        }
    }

    private void reportStatus() {
        if (handler != null) {
            handler.onInAppUpdateStatus(inAppUpdateStatus);
        }
    }


    public interface InAppUpdateHandler {
        void onInAppUpdateError(int code, Throwable error);

        void onInAppUpdateStatus(InAppUpdateStatus inAppUpdateStatus);
    }

}
