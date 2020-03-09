package com.instance.ceg.appActivities;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.instance.ceg.BuildConfig;
import com.instance.ceg.R;
import com.instance.ceg.appBroadcastReceivers.SignalReceiver;
import com.instance.ceg.appData.Admin;
import com.instance.ceg.appData.DataStorage;
import com.instance.ceg.appData.Keys;
import com.instance.ceg.appData.Users;
import com.instance.ceg.appHelpers.DbHelper;
import com.instance.ceg.appInterfaces.Action;
import com.instance.ceg.appUtils.AppHelper;
import com.instance.ceg.appUtils.PermissionHelper;
import com.instance.ceg.appUtils.exceptionHelper.AppExceptionHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import butterknife.ButterKnife;

public class SuperCompatActivity extends AppCompatActivity {

    protected Gson gson = new Gson();

    public FirebaseAuth firebaseAuth;
    public DatabaseReference parentDatabaseReference, childDatabaseReference,
            teamDatabaseReference, adminDbReference, clubsDbRef,
            membersDbReference, commentsDbReference, circularDbReference, newsDbReference;
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

    private DbHelper dbHelper;

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

        if (BuildConfig.DEBUG) {
            AppHelper.print("Exception debug block");
            if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof AppExceptionHelper)) {
                AppHelper.print("Exception if block");
                Thread.setDefaultUncaughtExceptionHandler(
                        new AppExceptionHelper(getFilesDir().getAbsolutePath()));
            } else {
                AppHelper.print("Exception else block");
            }
        }


        //initializing butter knife
        ButterKnife.bind(this);

        //shared preferences data
        dataStorage = new DataStorage(this);

        //permissions helper
        permissionHelper = new PermissionHelper(this);

        dbHelper = new DbHelper();
    }

    public DbHelper getDbHelper() {
        return dbHelper;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //init fire base instance
        FirebaseApp.initializeApp(this);

        DbHelper.getFirebaseDatabase();

        //fire base authentication
        firebaseAuth = FirebaseAuth.getInstance();

        //fire base database
        parentDatabaseReference = FirebaseDatabase.getInstance().getReference();
        teamDatabaseReference = FirebaseDatabase.getInstance().getReference().child(Keys.TABLE_TEAM);
        adminDbReference = FirebaseDatabase.getInstance().getReference().child(Keys.TABLE_ADMIN);
        clubsDbRef = FirebaseDatabase.getInstance().getReference().child(Keys.TABLE_CLUBS);
        membersDbReference = FirebaseDatabase.getInstance().getReference().child(Keys.TABLE_MEMBERS);
        commentsDbReference = FirebaseDatabase.getInstance().getReference().child(Keys.TABLE_COMMENTS);
        circularDbReference = FirebaseDatabase.getInstance().getReference().child(Keys.TABLE_CIRCULAR);
        newsDbReference = FirebaseDatabase.getInstance().getReference().child(Keys.TABLE_NEWSFEED);

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

    public void slideUpView(View view, Context context) {
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_up_anim));
    }

    public void slideDown(View view, Context context) {
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_down_anim));
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
            AppHelper.print("Showing progress");
        }
    }

    //to hide showing progress view
    protected void cancelProgress() {
        if (progressDialog.isShowing() && progressDialog != null) {
            progressDialog.dismiss();
            AppHelper.print("Progress cancelled");
        }
    }

    //open activity with shared element transition
    protected void openWithImageTransition(Context from, Class to, boolean close,
                                           ImageView imageView, String key, String value) {
        Intent intent = new Intent(from, to);
        intent.putExtra(key, value);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, imageView, getString(R.string.image_transition));
        startActivity(intent, options.toBundle());
        if (close) {
            this.finish();
        }
    }

    protected void openWithMultipleImageTransition(Context from, Class to, boolean close, ImageView viewOne, ImageView viewTwo, String key, String value) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Pair viewOnePair = Pair.create(viewOne, viewOne.getTransitionName());
            Pair viewTwoPair = Pair.create(viewTwo, viewTwo.getTransitionName());
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation((Activity) from, viewOnePair, viewTwoPair);
            Intent intent = new Intent(from, to);
            intent.putExtra(key, value);
            startActivity(intent, activityOptions.toBundle());
            if (close) {
                finish();
            }

        } else {
            goTo(from, to, close, key, value);
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
            ((TextView) snackBarView.findViewById(R.id.snackbar_text)).setTextColor(ContextCompat.getColor(this, R.color.white)); /*Setting text color*/

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
        try {
            if (connectionChangeReceiver != null && !connectionChangeReceiver.isRegistered()) {
                connectionChangeReceiver.register();
            }
        } catch (Exception e) {

        }
    }

    private void unRegisterReceivers() {
        try {
            if (connectionChangeReceiver != null && connectionChangeReceiver.isRegistered()) {
                connectionChangeReceiver.unRegister();
            }
        } catch (Exception e) {
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
            return path;
            //return getCompressedImagePath(path);
        } catch (Exception e) {
            AppHelper.print("Registration process : Exception in image compression");
            e.printStackTrace();
            return path;
        }
    }

    protected void checkDataAndOpen() {

        AppHelper.print("Is_Online: " + dataStorage.getBoolean(Keys.IS_ONLINE));

        if (dataStorage.getBoolean(Keys.PERMISSIONS_GRANTED)) {
            if (dataStorage.isDataAvailable(Keys.MOBILE)) {
                if (dataStorage.getBoolean(Keys.IS_ONLINE)) {
                    loadProfileAndOpenHome();
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

    private void loadProfileAndOpenHome() {
        new DbHelper().getUser(new DbHelper.UserCallback() {
            @Override
            public void onDataReceived(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    retriveMobileFromDatabase(dataSnapshot);
                } else {
                    goTo(SuperCompatActivity.this, MainActivity.class, true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void retriveMobileFromDatabase(DataSnapshot dataSnapshot) {
        ArrayList<String> phoneNumbers = new ArrayList<>();

        Map<String, Users> value = (Map<String, Users>) dataSnapshot.getValue();

        phoneNumbers.clear();

        if (value != null) {
            for (Map.Entry<String, Users> entry : value.entrySet()) {
                Map usersMap = (Map) entry.getValue();

                String phoneNumber = (String) usersMap.get(Keys.ATTR_MOBILE);

                AppHelper.print("phoneNumber: "+phoneNumber);

                if (phoneNumber != null) {
                    phoneNumbers.add(phoneNumber);
                }

                if (phoneNumber != null && dataStorage.getString(Keys.MOBILE).equals("") &&
                        dataStorage.getString(Keys.MOBILE).equals(phoneNumber)) {

                    Users users = new Users((String) usersMap.get(Keys.ATTR_USERNAME),
                            (String) usersMap.get(Keys.ATTR_EMAIL),
                            (String) usersMap.get(Keys.ATTR_MOBILE),
                            (String) usersMap.get(Keys.ATTR_GENDER),
                            (String) usersMap.get(Keys.IS_ADMIN));

                    AppHelper.print("ATTR_USERNAME: "+usersMap.get(Keys.ATTR_USERNAME));
                    AppHelper.print("ATTR_EMAIL: "+usersMap.get(Keys.ATTR_EMAIL));
                    AppHelper.print("ATTR_MOBILE: "+usersMap.get(Keys.ATTR_MOBILE));


                    users.setUserId((String) usersMap.get("userId"));

                    String admins = dataStorage.getString(Keys.ADMIN_INFO);
                    ArrayList<Admin> adminArrayList = gson.fromJson(admins, new TypeToken<ArrayList<Admin>>() {
                    }.getType());

                    if (adminArrayList != null && adminArrayList.size() > 0) {
                        for (Admin admin : adminArrayList) {
                            if (admin.getMobile().equals(phoneNumber)) {
                                dataStorage.saveBoolean(Keys.IS_ADMIN, true);
                                dataStorage.saveString(Keys.ADMIN_VALUE, admin.getPosition());
                            } else {
                                dataStorage.saveBoolean(Keys.IS_ADMIN, false);
                            }
                        }
                    }
                }
            }
        }

        goTo(SuperCompatActivity.this, MainActivity.class, true);
    }

    private void goToPermissions() {
        goTo(this, PermissionsHelperActivity.class, true);
    }

    private void goToMobVerification() {
        goTo(this, MobileNumberActivity.class, true);
    }

//    private String getCompressedImagePath(String actualImagePath) throws IOException {
//        return getCompressedImagePath(this, actualImagePath);
//    }

//    public static String getCompressedImagePath(Context context, String imagePath) throws IOException {
//        File actualFile = new File(imagePath);
//        AppHelper.print("Actual: " + actualFile.getAbsolutePath() + " " + actualFile.length());
//        File compressedFile =         new ImageCompress(context).getLowBitmap(String.valueOf(actualFile);
//        AppHelper.print("Compressed: " + compressedFile.getAbsolutePath() + " " + compressedFile.length());
//        return compressedFile.getAbsolutePath();
//    }

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
