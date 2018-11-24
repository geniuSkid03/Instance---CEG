package com.inspiregeniussquad.handstogether.appActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appData.Users;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;

import butterknife.BindView;
import butterknife.OnClick;

public class SignupActivity extends SuperCompatActivity {

    @BindView(R.id.email)
    TextInputEditText emailEd;

    @BindView(R.id.name)
    TextInputEditText nameEd;

    @BindView(R.id.radio_grp)
    RadioGroup radioGrp;

    @BindView(R.id.male_rb)
    RadioButton maleRdBtn;

    @BindView(R.id.female_rb)
    RadioButton femaleRdBtn;

    @BindView(R.id.register_me)
    AppCompatButton registerMeBtn;

    @BindView(R.id.login_me)
    TextView loginMeBtn;

    private static final int GMAIL_SIGNIN = 333;
    private GoogleApiClient googleApiClient;
    private String mobileNumber, name, email, imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //getting mobile number
        if (getIntent().getExtras() != null) {
            mobileNumber = getIntent().getStringExtra("mobile");
        }

        //setting up clients for email auto-retrival process
        setUpGoogle();
    }

    @OnClick({R.id.login_me, R.id.register_me, R.id.female_rb, R.id.male_rb, R.id.email})
    public void onclicked(View view) {
        switch (view.getId()) {
            case R.id.login_me:
                goTo(this, MobileNumberActivity.class, true);
                break;
            case R.id.register_me:
                String email = emailEd.getText().toString().trim();
                String name = nameEd.getText().toString().trim();
                String gender = maleRdBtn.isChecked() ? Keys.MALE : femaleRdBtn.isChecked() ? Keys.FEMALE : "";

                //function to validate and register user
                checkAndRegister(email, name, gender);
                break;
            case R.id.female_rb:
                maleRdBtn.setChecked(false);
                femaleRdBtn.setChecked(true);
                break;
            case R.id.male_rb:
                femaleRdBtn.setChecked(false);
                maleRdBtn.setChecked(true);
                break;
            case R.id.email:
                //clear views
                emailEd.setText("");
                nameEd.setText("");

                //function to show email list
                getUserEmail();
                break;
        }
    }

    private void checkAndRegister(String email, String name, String gender) {
        showProgress(getString(R.string.registering_you));

        if (TextUtils.isEmpty(email)) {
            cancelProgress();
            showToast(SignupActivity.this, getString(R.string.enter_email));
            return;
        }

        if (TextUtils.isEmpty(name)) {
            cancelProgress();
            showToast(SignupActivity.this, getString(R.string.enter_name));
            return;
        }

        if (TextUtils.isEmpty(gender)) {
            cancelProgress();
            showToast(SignupActivity.this, getString(R.string.choose_gender));
            return;
        }

        insertUserIntoDb(new Users(name, email, mobileNumber, gender));
    }

    private void insertUserIntoDb(final Users users) {
        usersDatabaseReference.child(usersDatabaseReference.push().getKey()).setValue(users, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError == null) {
                    goToHome(users);
                } else {
                    cancelProgress();
                    showInfoAlert(getString(R.string.cannot_create_account));
                }
            }
        });
    }

    private void goToHome(Users users) {
        dataStorage.saveString(Keys.USER_NAME, users.getName());
        dataStorage.saveString(Keys.MOBILE, users.getMobile());
        dataStorage.saveString(Keys.USER_EMAIL, users.getEmail());
        dataStorage.saveString(Keys.USER_GENDER, users.getGender());
        dataStorage.saveString(Keys.PROFILE_IMAGE, imgUrl);

        if (!TextUtils.isEmpty(imgUrl)) {
            users.setImgUrl(imgUrl);
        }

        dataStorage.saveString(Keys.USER_DATA, gson.toJson(users));
        dataStorage.saveBoolean(Keys.IS_ONLINE, true);

        cancelProgress();
        goTo(this, HomeActivity.class, true);
    }

    private void setUpGoogle() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        GoogleApiClient.OnConnectionFailedListener googleConnectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                AppHelper.showToast(SignupActivity.this, getString(R.string.google_conn_failed));

            }
        };

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, googleConnectionFailedListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
    }

    //getting user all email accounts in phone
    private void getUserEmail() {
        if (googleApiClient.isConnected()) {
            AppHelper.print("Trying to get user email");
            Intent signInIntent =
                    Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(signInIntent, GMAIL_SIGNIN);
        } else {
            AppHelper.showToast(SignupActivity.this, "Google Api client not connected");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        AppHelper.print("Result code: " + resultCode);

        if (resultCode == RESULT_OK) {
            if (requestCode == GMAIL_SIGNIN) {
                AppHelper.print("OnActivityResult : gmail sign in");
                //getting email data
                GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                checkEmails(googleSignInResult);
            }
        }
    }


    //Getting email values and setting it in views
    private void checkEmails(GoogleSignInResult googleSignInResult) {
        AppHelper.print("Checking emails");

        if (googleSignInResult.isSuccess()) {
            GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();
            if (googleSignInAccount != null) {
                name = googleSignInAccount.getDisplayName();
                email = googleSignInAccount.getEmail();
                imgUrl = String.valueOf(googleSignInAccount.getPhotoUrl());

                emailEd.setText(email);
                nameEd.setText(name);
                AppHelper.print("Image Url: " + imgUrl);
            } else {
                AppHelper.print("Can't get email id");
            }

            //signing out chosen email
            signOutEmail();
        } else {
            showSnack(getString(R.string.choose_email));
        }
    }

    private void signOutEmail() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.clearDefaultAccountAndReconnect().setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    AppHelper.print("Email Signed Out!");
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        //connecting to google api client
        if (googleApiClient != null) {
            googleApiClient.connect();
            AppHelper.print("GoogleApi client connected");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        //disconnecting google api client
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
            AppHelper.print("GoogleApi client dis connected");
        }
    }
}
