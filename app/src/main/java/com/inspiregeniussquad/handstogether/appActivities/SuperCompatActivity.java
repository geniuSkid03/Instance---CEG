package com.inspiregeniussquad.handstogether.appActivities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.inspiregeniussquad.handstogether.BuildConfig;
import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appBroadcastReceivers.SignalReceiver;
import com.inspiregeniussquad.handstogether.appData.DataStorage;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appData.Users;
import com.inspiregeniussquad.handstogether.appInterfaces.Action;
import com.inspiregeniussquad.handstogether.appUtils.AppExceptionHelper;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;
import com.inspiregeniussquad.handstogether.appUtils.PermissionHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;
import id.zelory.compressor.Compressor;

public class SuperCompatActivity extends AppCompatActivity {

    protected Gson gson = new Gson();

    public FirebaseAuth firebaseAuth;
    public DatabaseReference parentDatabaseReference, childDatabaseReference,
            usersDatabaseReference, teamDatabaseReference, adminDbReference, clubsDbRef;
    public StorageReference storageReference;
    public FirebaseStorage firebaseStorage;

    public ProgressDialog progressDialog;

    protected DataStorage dataStorage;
    public List<Users> newUser;

    protected Animation scaleUpAnim, scaleDownAnim;

    protected SignalReceiver connectionChangeReceiver;
    protected AlertDialog noInternetDialog, infoAlert, simpleAlertDialog, okCancelAlertDialog;
    protected PermissionHelper permissionHelper;

    protected UploadTask uploadTask;

    protected boolean useDarkTheme;

    protected static final String PREFS_NAME = "prefs";
    protected static final String PREF_DARK_THEME = "dark_theme";


    @Override
    public void setContentView(int layoutResID) {
//
//        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
//        useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);
//
//        if(useDarkTheme) {
//            setTheme(R.style.AppThemeDark);
//        }

        super.setContentView(layoutResID);

        //initializing butter knife
        ButterKnife.bind(this);

        //shared preferences data
        dataStorage = new DataStorage(this);

        //permissions helper
        permissionHelper = new PermissionHelper(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (BuildConfig.DEBUG) {
            if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof AppExceptionHelper)) {
                Thread.setDefaultUncaughtExceptionHandler(
                        new AppExceptionHelper(getFilesDir().getAbsolutePath()));
            }
        }

        //init fire base instance
        FirebaseApp.initializeApp(this);

        //fire base authentication
        firebaseAuth = FirebaseAuth.getInstance();

        //fire base database
        parentDatabaseReference = FirebaseDatabase.getInstance().getReference();
        usersDatabaseReference = FirebaseDatabase.getInstance().getReference().child(Keys.TABLE_USER);
        teamDatabaseReference = FirebaseDatabase.getInstance().getReference().child(Keys.TABLE_TEAM);
        adminDbReference = FirebaseDatabase.getInstance().getReference().child(Keys.TABLE_ADMIN);
        clubsDbRef = FirebaseDatabase.getInstance().getReference().child(Keys.TABLE_CLUBS);

        //fire base storage
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        newUser = new ArrayList<>();

        //progress view
        progressDialog = new ProgressDialog(this);

        //alert dialogs
        infoAlert = new AlertDialog.Builder(this).create();
        simpleAlertDialog = new AlertDialog.Builder(this).create();
        okCancelAlertDialog = new AlertDialog.Builder(this).create();

        //no internet alert receiver
        connectionChangeReceiver = new SignalReceiver(this, Keys.NO_INTERNET_CONNECTION, new Action() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onConnectionChangeDetected(AppHelper.isNetworkAvailable(context));
            }
        });

        //view for internet connection failure
        View noInternetView = LayoutInflater.from(this).inflate(R.layout.no_internet_view, null);
        AppCompatButton retryBtn = noInternetView.findViewById(R.id.retry_btn);
        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissInternetAlert();
                onConnectionChangeDetected(AppHelper.isNetworkAvailable(SuperCompatActivity.this));
            }
        });
        noInternetDialog = new AlertDialog.Builder(this).create();
        noInternetDialog.setView(noInternetView);
        noInternetDialog.setCancelable(false);
        Objects.requireNonNull(noInternetDialog.getWindow()).setBackgroundDrawable(ContextCompat.getDrawable(this, android.R.color.transparent));

        //pre-loading animations
        loadAllAnims();
    }

    private void loadAllAnims() {
        scaleDownAnim = AnimationUtils.loadAnimation(this, R.anim.scale_down_animation);
        scaleUpAnim = AnimationUtils.loadAnimation(this, R.anim.scale_up_animatin);
    }

    protected StorageReference getStorageReference() {
        return storageReference;
    }

    protected DatabaseReference getReferenceFromDatabase(String tableName) {
        return FirebaseDatabase.getInstance().getReference(tableName);
    }

    private void onConnectionChangeDetected(boolean isInternetAvailable) {

        AppHelper.print("Network result: " + isInternetAvailable);

        if (!isInternetAvailable) {
            showNoInternetAlert();
        } else {
            dismissInternetAlert();
            onRetryClicked();
        }
    }

    private void showNoInternetAlert() {
        if (!noInternetDialog.isShowing() && !isFinishing()) {
            noInternetDialog.show();
        }
    }

    protected void onRetryClicked() {

    }

    private void dismissInternetAlert() {
        if (noInternetDialog != null && noInternetDialog.isShowing()) {
            noInternetDialog.dismiss();
        }
    }


    //for starting activity with single intent values
    protected void goTo(Context from, Class to, boolean close, String key, String data) {
        if (!this.getClass().getSimpleName().equals(to.getClass().getSimpleName())) {
            if (key != null && data != null) {
                startActivity(new Intent(from, to).putExtra(key, data));
            }
            if (close) {
                finish();
            }
        }

    }

    //for starting activity with two intent values
    protected void goTo(Context from, Class to, boolean close, String key, String data, String key1, String data1) {
        if (!this.getClass().getSimpleName().equals(to.getClass().getSimpleName())) {
            if (key != null && data != null && key1 != null && data1 != null) {
                startActivity(new Intent(from, to).putExtra(key, data).putExtra(key1, data1));
            }
            if (close) {
                finish();
            }
        }

    }

    //for starting activity without intent values
    protected void goTo(Context from, Class to, boolean close) {
        if (!this.getClass().getSimpleName().equals(to.getClass().getSimpleName())) {
            startActivity(new Intent(from, to));
            if (close) {
                finish();
            }
        }
    }

    //to show progress view
    protected void showProgress(String msg) {
        if (progressDialog != null && !isFinishing()) {
            progressDialog.setMessage(msg);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    //to hide showing progress view
    protected void cancelProgress() {
        if (progressDialog.isShowing() && progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    protected void showInfoAlert(String msg) {
        if (infoAlert != null) {
            if (msg != null) {
                infoAlert.setMessage(msg);
                infoAlert.setButton(android.app.AlertDialog.BUTTON_POSITIVE, getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                infoAlert.setCancelable(false);
                if (!isFinishing()) {
                    infoAlert.show();
                }
            }
        }
    }

    protected void showSnack(String msg) {
        final View view = getRootView();
        if (view != null && msg != null) {
            Snackbar snackbar = Snackbar.make(view, msg, /*isLong ? Snackbar.LENGTH_LONG :*/ Snackbar.LENGTH_LONG);

            View snackBarView = snackbar.getView();

            //snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.red));  /*setting up action bar color*/
            snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark)); /*setting background color*/
            ((TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(ContextCompat.getColor(this, R.color.white)); /*Setting text color*/

            if (!isFinishing()) {
                snackbar.show();
            }
        }
//        Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT).show();
    }

    private View getRootView() {
        final ViewGroup contentViewGroup = findViewById(android.R.id.content);
        View rootView = null;

        if (contentViewGroup != null)
            rootView = contentViewGroup.getChildAt(0);

        if (rootView == null)
            rootView = getWindow().getDecorView().getRootView();

        return rootView;
    }

    private void applyEnterAnimation() {
        overridePendingTransition(R.anim.slide_in_anim, R.anim.slide_out_anim);
    }

    private void applyExitAnimation() {
        overridePendingTransition(R.anim.slide_out_anim, R.anim.slide_in_anim);
    }

    private void registerReceivers() {
        if (connectionChangeReceiver != null && !connectionChangeReceiver.isRegistered()) {
            connectionChangeReceiver.register();
        }
    }

    private void unRegisterReceivers() {
        if (connectionChangeReceiver != null && connectionChangeReceiver.isRegistered()) {
            connectionChangeReceiver.unRegister();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //registering broadcast receivers
        registerReceivers();
    }

    @Override
    protected void onPause() {

        //removing broadcast receivers
        unRegisterReceivers();

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);

//        applyEnterAnimation();
    }

    @Override
    public void finish() {
        super.finish();

//        applyExitAnimation();
    }

    protected void showToast(Context context, String msg) {
        if (context != null && context.getResources() != null) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
    }

    protected boolean isAboveM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    protected String getImagePath(Uri uri) {
        String path = AppHelper.getRealPathFromURI(this, uri);
        try {
            return getCompressedImagePath(path);
        } catch (Exception e) {
            AppHelper.print("Registration process : Exception in image compression");
            e.printStackTrace();
            return path;
        }
    }

    protected void checkDataAndOpen() {

        AppHelper.print("Is_Online: "+dataStorage.getBoolean(Keys.IS_ONLINE));

        if (dataStorage.getBoolean(Keys.PERMISSIONS_GRANTED)) {
            if (dataStorage.isDataAvailable(Keys.MOBILE)) {
                if (dataStorage.getBoolean(Keys.IS_ONLINE)) {
                    goTo(this, MainActivity.class, true);
                } else {
                    goToMobVerification();
                }
            } else {
                goToMobVerification();
            }
        } else {
            goToPermissions();
        }
    }

    private void goToPermissions() {
        goTo(this, PermissionsHelperActivity.class, true);
    }

    private void goToMobVerification() {
        goTo(this, MobileNumberActivity.class, true);
    }

    private String getCompressedImagePath(String actualImagePath) throws IOException {
        return getCompressedImagePath(this, actualImagePath);
    }

    public static String getCompressedImagePath(Context context, String imagePath) throws IOException {
        File actualFile = new File(imagePath);
        AppHelper.print("Actual: " + actualFile.getAbsolutePath() + " " + actualFile.length());
        File compressedFile = new Compressor(context).compressToFile(actualFile);
        AppHelper.print("Compressed: " + compressedFile.getAbsolutePath() + " " + compressedFile.length());
        return compressedFile.getAbsolutePath();
    }

    protected void showSimpleAlert(String msg, String btnText, final SimpleAlert simpleAlert) {
        if (simpleAlertDialog != null) {
            simpleAlertDialog.setMessage(msg);
            simpleAlertDialog.setButton(AlertDialog.BUTTON_POSITIVE, btnText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    simpleAlert.onBtnClicked(dialog, which);
                }
            });
            simpleAlertDialog.show();
        }
    }

    protected void showOkCancelAlert(String msg, final OkCancelAlert okCancelAlert) {
        if (okCancelAlertDialog != null) {
            okCancelAlertDialog.setMessage(msg);
            okCancelAlertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    okCancelAlert.onOkClicked(dialog, which);
                }
            });
            okCancelAlertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    okCancelAlert.onCancelClicked(dialog, which);
                }
            });
            okCancelAlertDialog.show();
        }
    }

    public interface SimpleAlert {
        void onBtnClicked(DialogInterface dialogInterface, int which);
    }

    public interface OkCancelAlert {
        void onOkClicked(DialogInterface dialogInterface, int which);

        void onCancelClicked(DialogInterface dialogInterface, int which);
    }

}
