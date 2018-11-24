package com.inspiregeniussquad.handstogether.appActivities;

import android.app.Application;

import com.firebase.client.Firebase;
import com.inspiregeniussquad.handstogether.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class Instance extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/base_font.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
