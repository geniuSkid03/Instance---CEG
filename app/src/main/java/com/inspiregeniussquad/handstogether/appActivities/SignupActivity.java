package com.inspiregeniussquad.handstogether.appActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.reflect.TypeToken;
import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appData.Admin;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appData.Users;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

public class SignupActivity extends SuperCompatActivity {

    @BindView(R.id.email)
    EditText emailEd;

    @BindView(R.id.name)
    EditText nameEd;

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

    @BindView(R.id.facebook_login)
    ImageView fbLoginIv;

    @BindView(R.id.google_login)
    ImageView googleLoginIv;

    private static final int GMAIL_SIGNIN = 333;
    private GoogleApiClient googleApiClient;
    private String mobileNumber, name, email, gender;

    private CallbackManager mCallbackManager;
    private AccessTokenTracker mTokenTracker;
    private ProfileTracker mProfileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //getting mobile number
        if (getIntent().getExtras() != null) {
            mobileNumber = getIntent().getStringExtra("mobile");
        }

        //setting up clients for email auto-retrival process
        setUpGoogle();

        //setting up facebook integration
        setUpFacebook();
    }

    @OnClick({R.id.login_me, R.id.register_me, R.id.female_rb, R.id.male_rb, R.id.facebook_login, R.id.google_login})
    public void onclicked(View view) {
        switch (view.getId()) {
            case R.id.login_me:
                goTo(this, MobileNumberActivity.class, true);
                break;
            case R.id.register_me:
                email = emailEd.getText().toString().trim();
                name = nameEd.getText().toString().trim();
                gender = maleRdBtn.isChecked() ? Keys.MALE : femaleRdBtn.isChecked() ? Keys.FEMALE : "";

                //function to validate and register user
                checkAndRegister(email, name, gender, 1);
                break;
            case R.id.female_rb:
                maleRdBtn.setChecked(false);
                femaleRdBtn.setChecked(true);
                break;
            case R.id.male_rb:
                femaleRdBtn.setChecked(false);
                maleRdBtn.setChecked(true);
                break;
            case R.id.facebook_login:
                loginWithFb();
                break;
            case R.id.google_login:
                getUserEmail();
                break;
        }
    }

    /*
     *  1- direct sign up (name, email, gender)
     *  2- fb sign up (name, email, img)
     *  3- google sign up (name, email, img)
     *
     * */

    private void checkAndRegister(String email, String name, String gender, int signUpMethod) {
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

        if (signUpMethod == 1) {
            if (TextUtils.isEmpty(gender)) {
                cancelProgress();
                showToast(SignupActivity.this, getString(R.string.choose_gender));
                return;
            }
        }

        AppHelper.print("Sign up method: " + signUpMethod);
        AppHelper.print("Name: " + name);
        AppHelper.print("Email: " + email);
        AppHelper.print("Mobile: " + mobileNumber);
//        AppHelper.print("profile: "+profileImg);

        if (dataStorage.isDataAvailable(Keys.ADMIN_INFO)) {
            String admins = dataStorage.getString(Keys.ADMIN_INFO);
            ArrayList<Admin> adminArrayList = gson.fromJson(admins, new TypeToken<ArrayList<Admin>>() {
            }.getType());

            if(adminArrayList != null && adminArrayList.size() > 0) {
                for (Admin admin : adminArrayList) {
                    AppHelper.print("Admin mobile numbers: " + admin.getMobile());

                    if (admin.getMobile().equals(mobileNumber)) {
                        createUser("1");
                        return;
                    } else {
                        createUser("0");
                        return;
                    }
                }
            } else {
                createUser("0");
            }
        }
    }

    private void createUser(String isAdmin) {
        AppHelper.print("Creating user as: "+isAdmin);

        ArrayList<String> likedPostArrayList = new ArrayList<>();
        ArrayList<String> commentedPostArrayList = new ArrayList<>();
        ArrayList<String> bookmarkedPostArrayList = new ArrayList<>();

        likedPostArrayList.add("0");
        commentedPostArrayList.add("0");
        bookmarkedPostArrayList.add("0");

        Users users = new Users(name, email, mobileNumber, !TextUtils.isEmpty(gender) ? gender : getString(R.string.un_specified),
                likedPostArrayList, commentedPostArrayList, bookmarkedPostArrayList, isAdmin);
//        if(!TextUtils.isEmpty(imgUrl)) {
//            uploadUserImage(users);
//        } else {
//        }
        insertUserIntoDb(users);
    }

//    private void uploadUserImage(final Users users) {
//        String imgName = users.getName()+"_img";
//        showProgress(getString(R.string.uploading_data));
//
//        File file = new File(users.getImgUrl());
//
//        Uri imgUri = Uri.fromFile(file);
//
//        AppHelper.print("Image Uri: "+imgUri);
//
//        final StorageReference storageRef = storageReference.child(Keys.USER_PROF + imgName);
//
//        uploadTask = storageRef.putFile(imgUri);
//
//        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//            @Override
//            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                if (!task.isSuccessful()) {
//                    showInfoAlert(getString(R.string.upload_failed));
//                    AppHelper.print("Task unsuccessful!");
//                    throw task.getException();
//                }
//                cancelProgress();
//
//                return storageRef.getDownloadUrl();
//            }
//        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//            @Override
//            public void onComplete(@NonNull Task<Uri> task) {
//                if (task.isSuccessful()) {
//                    cancelProgress();
//
//                    if (task.getResult() != null) {
//                        String photoStringLink = task.getResult().toString();
//                        AppHelper.print("Uploaded logo Uri " + photoStringLink);
//
//                        users.setImgUrl(photoStringLink);
//
//                        insertUserIntoDb(users);
//
//                    } else {
//                        cancelProgress();
//                        AppHelper.print("Image uploaded but uri null");
//                    }
//                } else {
//                    cancelProgress();
//                    showInfoAlert(getString(R.string.upload_failed));
//                }
//            }
//        });
//    }

    private void insertUserIntoDb(final Users users) {
        usersDatabaseReference.child(users.getMobile()).setValue(users, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null) {
                    goToHome(users);
                } else {
                    cancelProgress();
                    showInfoAlert(getString(R.string.cannot_create_account));
                }
            }
        });
    }

    //todo save user image in firebase
    private void goToHome(Users users) {
        dataStorage.saveString(Keys.USER_NAME, users.getName());
        dataStorage.saveString(Keys.MOBILE, users.getMobile());
        dataStorage.saveString(Keys.USER_EMAIL, users.getEmail());
        dataStorage.saveString(Keys.USER_GENDER, users.getGender());
//        dataStorage.saveString(Keys.PROFILE_IMAGE, imgUrl);
//
//        if (!TextUtils.isEmpty(imgUrl)) {
//            users.setImgUrl(imgUrl);
//        }

        dataStorage.saveString(Keys.MOBILE, users.getMobile());
        dataStorage.saveString(Keys.USER_DATA, gson.toJson(users));
        dataStorage.saveBoolean(Keys.IS_ONLINE, true);

        cancelProgress();

        goTo(this, MainActivity.class, true);
    }

    private void setUpFacebook() {
        // Facebook Initialization
        FacebookSdk.sdkInitialize(getApplicationContext());

        //  create a callback manager to handle login responses
        mCallbackManager = CallbackManager.Factory.create();

        // set up Profile Tracker for Profile Change and Set up Token Tracker for Access Token Changes.
        setupTokenTracker();
        setupProfileTracker();

        mTokenTracker.startTracking();
        mProfileTracker.startTracking();
    }

    private void setupTokenTracker() {
        mTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                //   Log.i(FACEBOOK, "Profile Access Token Change " + currentAccessToken);
            }
        };
    }

    private void setupProfileTracker() {
        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                //   Log.i(FACEBOOK, "Profile Tracker profile changed" + currentProfile);
                if (currentProfile != null) {
                    //   Log.i(FACEBOOK, "Profile" + currentProfile);
                    /*try {
                        if(currentProfile.getProfilePictureUri(256, 256)!=null) {
                            Uri uri=currentProfile.getProfilePictureUri(300, 300);
                            Define.fb_picture=uri.toString();
                            Log.i(FACEBOOK, "Profile Tracker FB Profile Picture" + Define.fb_picture);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/

                }
            }
        };
    }

    private void loginWithFb() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                String fb_first_name = "", fb_last_name = "";
                                try {
                                    if (!object.getString("first_name").equals("")) {
                                        fb_first_name = object.getString("first_name");
                                        AppHelper.print("first_name: " + fb_first_name);
                                    }
                                    if (!object.getString("last_name").equals("")) {
                                        fb_last_name = object.getString("last_name");
                                        AppHelper.print("last_name: " + fb_last_name);
                                    }

                                    name = fb_first_name + fb_last_name;

                                    if (!object.getString("email").equals("")) {
                                        email = object.getString("email");
                                        AppHelper.print("email: " + email);
                                    }

//                                    if (!object.getString("gender").equals("")) {
//                                        gender = object.getString("gender");
//                                        AppHelper.print("gender: " + gender);
//                                    }

//                                    if (!object.getJSONObject("picture").getJSONObject("data").getString("url").equals("")) {
//                                        imgUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");
//                                        AppHelper.print("PROFILE PIC" + imgUrl);
//                                    }

//                                    dataStorage.saveString(Keys.USER_PROFILE, imgUrl);

                                    checkAndRegister(email, name, "", 2);

                                } catch (JSONException e) {
                                    AppHelper.print("Exception in logging with fib");
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "first_name,last_name,name,email,gender,picture.width(256).height(256)");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();

            }

            @Override
            public void onCancel() {
                showToast(SignupActivity.this, getString(R.string.fb_login_cancelled));
            }

            @Override
            public void onError(FacebookException error) {
                showToast(SignupActivity.this, getString(R.string.b_login_error));
            }
        });
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
                if (data != null) {
                    GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                    checkEmails(googleSignInResult);
                } else {
                    showSnack(getString(R.string.gmail_sign_in_failed));
                }
            } else {
                mCallbackManager.onActivityResult(requestCode, resultCode, data);
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

                AppHelper.print("Gmail sign in: " + name+"\t"+email);

                checkAndRegister(email, name, getString(R.string.un_specified), 3);
            } else {
                showSnack(getString(R.string.google_sign_failed));
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
