package com.inspiregeniussquad.handstogether.appFcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appActivities.MainActivity;
import com.inspiregeniussquad.handstogether.appActivities.SplashActivity;
import com.inspiregeniussquad.handstogether.appData.DataStorage;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;

public class AppFcmService extends FirebaseMessagingService {


    private String title = "", body = "";
    private DataStorage dataStorage;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        AppHelper.print("onMessage received");

        dataStorage = new DataStorage(this);

        if (remoteMessage != null) {
            AppHelper.print("Remote message received");
            if (remoteMessage.getData() != null) {
                if (remoteMessage.getData().get("title") != null) {
                    title = remoteMessage.getData().get("title");
                }
                if (remoteMessage.getData().get("body") != null) {
                    body = remoteMessage.getData().get("body");
                }

                createNotification(title, body);
            }
        }
    }


    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;
    private boolean isLoggedIn;

    private void createNotification(String title, String body) {
        Intent intent = null;

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        isLoggedIn = dataStorage.getBoolean(Keys.IS_ONLINE);

        if (isLoggedIn) {
            intent = new Intent(this, MainActivity.class);
        } else {
            intent = new Intent(this, SplashActivity.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        notificationBuilder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(getString(R.string.default_notification_channel_id),
                    getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
//            notificationChannel.enableVibration(true);
//            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert notificationManager != null;
            notificationBuilder.setChannelId(getString(R.string.default_notification_channel_id));
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }

}
