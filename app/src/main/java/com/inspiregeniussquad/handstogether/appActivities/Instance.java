package com.inspiregeniussquad.handstogether.appActivities;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.firebase.client.Firebase;
import com.inspiregeniussquad.handstogether.R;


public class Instance extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);

//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/base_font.ttf")
//                .setFontAttrId(R.attr.fontPath)
//                .build()
//        );

//        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
//                .resetViewBeforeLoading(true)
//                .cacheOnDisk(true)
//                .cacheInMemory(true)
//                .imageScaleType(ImageScaleType.EXACTLY)
//                .displayer(new FadeInBitmapDisplayer(300))
//                .build();
//
//        ImageLoaderConfiguration imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(getApplicationContext())
//                .defaultDisplayImageOptions(displayImageOptions)
//                .memoryCache(new WeakMemoryCache())
//                .diskCacheSize(100 * 1024 * 1024)
//                .build();
//
//        ImageLoader.getInstance().init(imageLoaderConfiguration);

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.default_notification_channel_id);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(getString(R.string.default_notification_channel_id),
                    name, importance);
            mChannel.setDescription(description);
            NotificationManager notificationManager = (NotificationManager) getSystemService(
                    NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(mChannel);
        }
    }
}
