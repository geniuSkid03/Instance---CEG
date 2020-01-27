package com.instance.ceg.appUtils.inAppUpdate;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;

public class InAppUpdateStatus {

    private static final int NO_UPDATE = 0;
    private AppUpdateInfo appUpdateInfo;
    private InstallState installState;

    public static class Constants {

        public static final int UPDATE_ERROR_START_APP_UPDATE_FLEXIBLE = 100;
        public static final int UPDATE_ERROR_START_APP_UPDATE_IMMEDIATE = 101;

        enum UpdateMode {
            FLEXIBLE, IMMEDIATE
        }

    }

    public void setAppUpdateInfo(AppUpdateInfo appUpdateInfo){
        this.appUpdateInfo = appUpdateInfo;
    }

    public void setInstallState(InstallState installState) {
        this.installState = installState;
    }

    public boolean isDownloading() {
        return installState != null && installState.installStatus() == InstallStatus.DOWNLOADING;
    }

    public boolean isDownloaded() {
        return installState != null && installState.installStatus() == InstallStatus.DOWNLOADED;
    }

    public boolean isFailed() {
        return installState != null && installState.installStatus() == InstallStatus.FAILED;
    }

    public boolean isUpdateAvailable() {
        return appUpdateInfo != null && appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE;
    }

    public int availableVersionCode() {
        if(appUpdateInfo != null) {
            return appUpdateInfo.availableVersionCode();
        } else {
            return NO_UPDATE;
        }
    }
}
