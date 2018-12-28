package com.inspiregeniussquad.handstogether.appActivities;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appData.Users;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

public class OtpVerificationActivity extends SuperCompatActivity {

    @BindView(R.id.verify_btn)
    AppCompatButton verifyBtn;

    @BindView(R.id.otp)
    EditText otpEd;

    @BindView(R.id.otp_timer)
    TextView otpTimerTv;

    @BindView(R.id.rsend_otp_btn)
    AppCompatButton resendOtpBtn;

    @BindView(R.id.change_num)
    TextView changeNumberTv;

    private String mobileNumber, receivedOtp, verificationId;
    private PhoneAuthProvider.ForceResendingToken resendingToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        //getting mobile number via intent
        if (getIntent().getExtras() != null) {
            mobileNumber = getIntent().getStringExtra(Keys.MOBILE);
        }

        //for underline
        changeNumberTv.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        //checking mobile number and requesting mobile number
        if (!TextUtils.isEmpty(mobileNumber)) {
            requestOtpFromFirebase(mobileNumber);
        } else {
            AppHelper.showToast(this, "Mobile number not found");
        }
    }

    @OnClick({R.id.rsend_otp_btn, R.id.verify_btn, R.id.change_num})
    public void oNCLick(View view) {
        switch (view.getId()) {
            case R.id.verify_btn: //this will verify OTP
                verifyOtp();
                break;
            case R.id.rsend_otp_btn: //this will resend OTP
                showCountTimer();
                reqOtpAgain(mobileNumber, resendingToken);
                break;
            case R.id.change_num:
                goToMobile();
                break;
        }
    }

    private void goToMobile() {
        goTo(this, MobileNumberActivity.class, true);
    }

    private void reqOtpAgain(final String mobileNumber, PhoneAuthProvider.ForceResendingToken resendingToken) {
        showProgress(getString(R.string.requesting_otp));

        //clearing view
        otpEd.setText("");

        String mobileWithPrefix = String.format("%s%s", "+91", mobileNumber);

        PhoneAuthProvider.getInstance().verifyPhoneNumber(mobileWithPrefix, 60, TimeUnit.SECONDS, this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        cancelProgress();

                        //setting views and receiving sms codes
                        otpEd.setText(phoneAuthCredential.getSmsCode() != null ? phoneAuthCredential.getSmsCode() : "******");
                        receivedOtp = phoneAuthCredential.getSmsCode();

                        //verify OTp
                        verifyOtp();
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        cancelProgress();
                        AppHelper.showToast(OtpVerificationActivity.this, "Verification failed" + "\n\n" + e.toString());
                    }

                    @Override
                    public void onCodeAutoRetrievalTimeOut(String s) {
                        cancelProgress();
                        //showSnackBar(getString(R.string.verification_failed), 1);
                        AppHelper.print("Verification Code auto retrivcal timeout: " + s);
                    }

                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        cancelProgress();
                        AppHelper.print("Verification Code sent to mobile: " + mobileNumber);
                        updateToken(s, forceResendingToken);
                    }
                }, resendingToken);
    }

    //updating token values for future use (i.e) for resending OTP again
    private void updateToken(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
        this.verificationId = verificationId;
        this.resendingToken = forceResendingToken;
    }

    private void requestOtpFromFirebase(final String mobileNumber) {
        showProgress(getString(R.string.requesting_otp));

        //prefix mobile number with country code
        final String mobile = "+91" + mobileNumber;

        //requesting otp
        PhoneAuthProvider.getInstance().verifyPhoneNumber(mobile, 60, TimeUnit.SECONDS, this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        cancelProgress();

                        //getting otp code
                        receivedOtp = phoneAuthCredential.getSmsCode();

                        //if otp received then set that otp to view
                        //if otp not received set it as "*"
                        otpEd.setText(receivedOtp != null ? receivedOtp : "******");

                        AppHelper.showToast(OtpVerificationActivity.this, "Verification completed " + receivedOtp);
                        //Cross check received OTP
                        verifyOtp();
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        cancelProgress();
                        AppHelper.showToast(OtpVerificationActivity.this, "Verification failed" + "\n\n" + e.toString());
                    }

                    @Override
                    public void onCodeAutoRetrievalTimeOut(String s) {
                        super.onCodeAutoRetrievalTimeOut(s);
                        cancelProgress();
                        AppHelper.showToast(OtpVerificationActivity.this, "Code Auto retrival time out" + "\n\n" + s);
                    }

                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        cancelProgress();
//                        AppHelper.showToast(OtpVerificationActivity.this, "Code sent!");

                        //saving this for requesting OTP again
                        verificationId = s;
                        resendingToken = forceResendingToken;
                    }
                });

        showCountTimer();
    }

    private void verifyOtp() {
        String otpInView = otpEd.getText().toString().trim();

        if (!TextUtils.isEmpty(otpInView)) {
            showProgress(getString(R.string.verifying_otp));

            PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, otpInView);
            crossCheckOtp(phoneAuthCredential, mobileNumber);
        } else {
            //otp verification failed
            otpEd.setError(getString(R.string.otp_invalid));
        }
    }

    private void crossCheckOtp(PhoneAuthCredential phoneAuthCredential, final String mobileNumber) {
        firebaseAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //signing out user from firebase
                    FirebaseAuth.getInstance().signOut();

                    //OTP verified successfully
                    //checking user is available in database
                    checkUserExistsInDatabase(mobileNumber);
                } else {
                    cancelProgress();
                    //otp verification failed
                    otpEd.setText("");
                    otpEd.setError(getString(R.string.otp_invalid));
                }
            }
        });
    }

    private void checkUserExistsInDatabase(final String mobileNumber) {

        //checking for internet connection
        if (!AppHelper.isNetworkAvailable(this)) {
            cancelProgress();
            AppHelper.showToast(this, getString(R.string.no_internt_conn));
            return;
        }

        usersDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                    retriveMobileFromDatabase(dataSnapshot, mobileNumber);
                } else {
                    //no user has registered, table was not created
                    //so allow him to signup
                    goToSignUp();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                cancelProgress();
                //show error as db error

                showSnack(getString(R.string.error_in_db_connection));
            }
        });
    }

    private void retriveMobileFromDatabase(DataSnapshot dataSnapshot, String mobileNumber) {
        ArrayList<String> phoneNumbers = new ArrayList<>();

        Map<String, Object> value = (Map<String, Object>) dataSnapshot.getValue();

        for (Map.Entry<String, Object> entry : value.entrySet()) {
            Map usersMap = (Map) entry.getValue();
            phoneNumbers.add((String) usersMap.get(Keys.ATTR_MOBILE));

            //trying to load profile of user, if mobile number found
            if(((String) usersMap.get(Keys.ATTR_MOBILE)).equalsIgnoreCase(mobileNumber)) {
                Users users = new Users((String) usersMap.get(Keys.ATTR_USERNAME),
                        (String) usersMap.get(Keys.ATTR_EMAIL),
                        (String) usersMap.get(Keys.ATTR_MOBILE),
                        (String) usersMap.get(Keys.ATTR_GENDER),
                        (String) usersMap.get(Keys.IS_ADMIN));

                dataStorage.saveString(Keys.IS_ADMIN, users.getIsAdmin());
                dataStorage.saveString(Keys.USER_DATA, gson.toJson(users));
            }
        }

        if (phoneNumbers.size() != 0) {
            if (phoneNumbers.contains(mobileNumber)) {
                //user already registered load profile and go home
                goToHome();
            } else {
                //user not registered
                goToSignUp();
            }
        } else {
            //user not registered
            goToSignUp();
        }
    }

    private void goToSignUp() {
        cancelProgress();
        goTo(OtpVerificationActivity.this, SignupActivity.class, true, Keys.MOBILE, mobileNumber);
    }

    private void goToHome() {
        cancelProgress();
        dataStorage.saveBoolean(Keys.IS_ONLINE, true);
        dataStorage.saveString(Keys.MOBILE, mobileNumber);
        goTo(OtpVerificationActivity.this, MainActivity.class, true);
    }

    private void showCountTimer() {
        otpTimerTv.setVisibility(View.VISIBLE);
        resendOtpBtn.setVisibility(View.GONE);

        //setting count down timer for 60 seconds
        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                otpTimerTv.setText(String.format(Locale.getDefault(), "00 : %02d", millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                otpTimerTv.setVisibility(View.GONE);
                resendOtpBtn.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
//        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
//        alertDialog.setMessage("Are you sure, you want to exit?");
//        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                finish();
//            }
//        });
//        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        if (!isFinishing() && !alertDialog.isShowing()) {
//            alertDialog.show();
//        }
        super.onBackPressed();
    }
}
