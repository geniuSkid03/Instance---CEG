package com.inspiregeniussquad.handstogether.appActivities;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appData.Keys;

import butterknife.BindView;

public class SplashActivity extends SuperCompatActivity {

    @BindView(R.id.logo)
    ImageView splashIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    protected void show() {
        //this handler will execute after "seconds" time
        // here seconds = 3000 (i.e) 3 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkDataAndOpen();
            }
        }, 3000);
    }

    private void checkDataAndOpen() {
        if (dataStorage.isDataAvailable(Keys.MOBILE)) {
            if (dataStorage.isDataAvailable(Keys.IS_ONLINE)) {
                if (dataStorage.getBoolean(Keys.IS_ONLINE)) {
                    goTo(this, HomeActivity.class, true);
                } else {
                    goToMobVerification();
                }
            } else {
                goToMobVerification();
            }
        } else {
            goToMobVerification();
        }
    }

    private void goToMobVerification() {
        goTo(this, MobileNumberActivity.class, true);

    }

    @Override
    protected void onResume() {
        super.onResume();

        //running progress bar
        show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //clearing animation
//        splashIv.clearAnimation();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
