package com.instance.ceg.appActivities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.reflect.TypeToken;
import com.instance.ceg.R;
import com.instance.ceg.appData.Admin;
import com.instance.ceg.appData.Keys;
import com.instance.ceg.appData.Users;
import com.instance.ceg.appHelpers.DbHelper;
import com.instance.ceg.appUtils.AppHelper;

import java.util.ArrayList;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
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
    private String mobileNumber, name, email, gender;

    private GoogleSignInClient googleSignInClient;

//    private CallbackManager mCallbackManager;
//    private AccessTokenTracker mTokenTracker;
//    private ProfileTracker mProfileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singnup_new);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //getting mobile number
        if (getIntent().getExtras() != null) {
            mobileNumber = getIntent().getStringExtra("mobile");
        }

        //setting up clients for email auto-retrieval process
        setUpGoogle();

        //setting up facebook integration
        setUpFacebook();

        adminDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    AppHelper.print("loading admin data");
                    adminSnapShot = dataSnapshot;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                AppHelper.print("User info not loaded");
                AppHelper.print("Db Error: " + databaseError.getMessage());
            }
        });
    }

    private DataSnapshot adminSnapShot;

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
                //loginWithFb();
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

//        ArrayList<String> likedPostArrayList = new ArrayList<>();
//        ArrayList<String> commentedPostArrayList = new ArrayList<>();
//        ArrayList<String> bookmarkedPostArrayList = new ArrayList<>();
//
//        likedPostArrayList.add("0");
//        commentedPostArrayList.add("0");
//        bookmarkedPostArrayList.add("0");
//
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            String token = instanceIdResult.getToken();
            AppHelper.print("FCM TOKEN: " + token);

            String adminValue = "-1";

            if(adminSnapShot != null) {
                ArrayList<Admin> adminsArraylist = new ArrayList<>();

                Map<String, Admin> teamDataMap = (Map<String, Admin>) adminSnapShot.getValue();
                adminsArraylist.clear();

                for (Map.Entry<String, Admin> teamDataEntry : teamDataMap.entrySet()) {
                    Map map = (Map) teamDataEntry.getValue();

                    Admin admin = new Admin((String) map.get("name"), (String) map.get("mobile"),
                            (String) map.get("position"), (String) map.get("designation"));

                    AppHelper.print("Name: " + admin.getName());
                    AppHelper.print("Mobile: " + admin.getMobile());
                    AppHelper.print("Position: " + admin.getPosition());

                    adminsArraylist.add(admin);
                }

                dataStorage.saveString(Keys.ADMIN_INFO, gson.toJson(adminsArraylist));

                AppHelper.print("Admin info available: " + dataStorage.isDataAvailable(Keys.ADMIN_INFO));

                AppHelper.print("User mobile: "+dataStorage.getString(Keys.MOBILE));

                for (Admin admin : adminsArraylist) {
                    if (admin.getMobile().equalsIgnoreCase(dataStorage.getString(Keys.MOBILE))) {
                        AppHelper.print("Admin mobile equals");
                        adminValue = admin.getPosition();
                        dataStorage.saveBoolean(Keys.IS_ADMIN, true);
                        dataStorage.saveString(Keys.ADMIN_VALUE, admin.getPosition());
                    } else {
                        AppHelper.print("Admin mobile not equals");
                    }
                }
            }

            getDbHelper().insertUser(new Users("", name, email, mobileNumber, gender, adminValue, token),
                    new DbHelper.InsertUserCallback() {
                @Override
                public void onSuccess(Users users) {
                    goToHome(users);
                }

                @Override
                public void onError() {
                    cancelProgress();
                    showInfoAlert(getString(R.string.cannot_create_account));
                }
            });
        });
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

    private void goToHome(Users users) {
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

        cancelProgress();
        showToast(this, "Account has been created successfully!");
        goTo(this, MainActivity.class, true);
    }

    private void setUpFacebook() {
        // Facebook Initialization
//        FacebookSdk.sdkInitialize(getApplicationContext());

        //  create a callback manager to handle login responses
//        mCallbackManager = CallbackManager.Factory.create();

        // set up Profile Tracker for Profile Change and Set up Token Tracker for Access Token Changes.
//        setupTokenTracker();
//        setupProfileTracker();
//
//        mTokenTracker.startTracking();
//        mProfileTracker.startTracking();
    }

//    private void setupTokenTracker() {
//        mTokenTracker = new AccessTokenTracker() {
//            @Override
//            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
//                   Log.i(FACEBOOK, "Profile Access Token Change " + currentAccessToken);
//            }
//        };
//    }
//
//    private void setupProfileTracker() {
//        mProfileTracker = new ProfileTracker() {
//            @Override
//            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
//                   Log.i(FACEBOOK, "Profile Tracker profile changed" + currentProfile);
//                if (currentProfile != null) {
//                       Log.i(FACEBOOK, "Profile" + currentProfile);
//                    /*try {
//                        if(currentProfile.getProfilePictureUri(256, 256)!=null) {
//                            Uri uri=currentProfile.getProfilePictureUri(300, 300);
//                            Define.fb_picture=uri.toString();
//                            Log.i(FACEBOOK, "Profile Tracker FB Profile Picture" + Define.fb_picture);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }*/
//
//                }
//            }
//        };
//    }

//    private void loginWithFb() {
//        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
//        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(),
//                        new GraphRequest.GraphJSONObjectCallback() {
//
//                            @Override
//                            public void onCompleted(JSONObject object, GraphResponse response) {
//                                String fb_first_name = "", fb_last_name = "";
//                                try {
//                                    if (!object.getString("first_name").equals("")) {
//                                        fb_first_name = object.getString("first_name");
//                                        AppHelper.print("first_name: " + fb_first_name);
//                                    }
//                                    if (!object.getString("last_name").equals("")) {
//                                        fb_last_name = object.getString("last_name");
//                                        AppHelper.print("last_name: " + fb_last_name);
//                                    }
//
//                                    name = fb_first_name + fb_last_name;
//
//                                    if (!object.getString("email").equals("")) {
//                                        email = object.getString("email");
//                                        AppHelper.print("email: " + email);
//                                    }
//
////                                    if (!object.getString("gender").equals("")) {
////                                        gender = object.getString("gender");
////                                        AppHelper.print("gender: " + gender);
////                                    }
//
////                                    if (!object.getJSONObject("picture").getJSONObject("data").getString("url").equals("")) {
////                                        imgUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");
////                                        AppHelper.print("PROFILE PIC" + imgUrl);
////                                    }
//
////                                    dataStorage.saveString(Keys.USER_PROFILE, imgUrl);
//
//                                    checkAndRegister(email, name, "", 2);
//
//                                } catch (JSONException e) {
//                                    AppHelper.print("Exception in logging with fib");
//                                }
//                            }
//                        });
//                Bundle parameters = new Bundle();
//                parameters.putString("fields", "first_name, last_name, name, email");
//                graphRequest.setParameters(parameters);
//                graphRequest.executeAsync();
//            }
//
//            @Override
//            public void onCancel() {
//                showToast(SignupActivity.this, getString(R.string.fb_login_cancelled));
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                showToast(SignupActivity.this, getString(R.string.b_login_error));
//            }
//        });
//    }

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

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

//        googleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, googleConnectionFailedListener)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
//                .build();
    }

    //getting user all email accounts in phone
    private void getUserEmail() {
        if (googleSignInClient != null) {
            AppHelper.print("Trying to get user email");
            Intent signInIntent = googleSignInClient.getSignInIntent();
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
                //mCallbackManager.onActivityResult(requestCode, resultCode, data);
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

                AppHelper.print("Gmail sign in: " + name + "\t" + email);

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
        if(googleSignInClient!= null) {
            googleSignInClient.signOut();
        }
//        if (googleApiClient != null && googleApiClient.isConnected()) {
//            googleApiClient.clearDefaultAccountAndReconnect().setResultCallback(new ResultCallback<Status>() {
//                @Override
//                public void onResult(@NonNull Status status) {
//                    AppHelper.print("Email Signed Out!");
//                }
//            });
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        //connecting to google api client
//        if (googleApiClient != null) {
//            googleApiClient.connect();
//            AppHelper.print("GoogleApi client connected");
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        //disconnecting google api client
//        if (googleApiClient != null && googleApiClient.isConnected()) {
//            googleApiClient.disconnect();
//            AppHelper.print("GoogleApi client dis connected");
//        }
    }
}
