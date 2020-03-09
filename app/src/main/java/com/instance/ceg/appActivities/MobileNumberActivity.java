package com.instance.ceg.appActivities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.instance.ceg.R;
import com.instance.ceg.appData.Keys;

import androidx.appcompat.widget.AppCompatButton;
import butterknife.BindView;
import butterknife.OnClick;

public class MobileNumberActivity extends SuperCompatActivity {

    @BindView(R.id.mobile)
    EditText inputMobile;

    @BindView(R.id.continue_btn)
    AppCompatButton continueBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_number_new);
    }

    @OnClick({R.id.continue_btn})
    public void onClick() {
        checkNumberAndReqOtp(inputMobile.getText().toString().trim());
    }

    //checking mobile number correctness
    //and requesting otp
    private void checkNumberAndReqOtp(String mobileNumber) {
        if (!TextUtils.isEmpty(mobileNumber)) {
            if (mobileNumber.length() == 10) {
                goTo(this, OtpVerificationActivity.class, true, Keys.MOBILE, mobileNumber);
            } else {
                inputMobile.setError(getString(R.string.mobile_invalid));
            }
        } else {
            inputMobile.setError(getString(R.string.mobile_number_empty));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
