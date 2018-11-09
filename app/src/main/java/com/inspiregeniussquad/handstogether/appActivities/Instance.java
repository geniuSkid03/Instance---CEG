package com.inspiregeniussquad.handstogether.appActivities;

import android.app.Application;

import com.firebase.client.Firebase;

public class Instance extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);

    }
}
