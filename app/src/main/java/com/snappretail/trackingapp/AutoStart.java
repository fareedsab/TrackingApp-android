package com.snappretail.trackingapp;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.snappretail.trackingapp.service.MyService;

public class AutoStart extends BroadcastReceiver {
    String channel_id = "notification_channel1";
    NotificationManager notificationManager1;
    public static boolean IS_SERVICE_RUNNING = false;
    public void showNotification(Context co, String title, String message) {
        // Pass the intent to switch to the MainActivity
        Intent intent = new Intent(co,LocationServiceActivity.class);
        // Assign channel ID

        // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
        // the activities present in the activity stack,
        // on the top of the Activity that is to be launched
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Pass the intent to PendingIntent to start the
        // next Activity
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(co,
                    0, intent, PendingIntent.FLAG_MUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(co,
                    0, intent, 0);
        }


        // Create a Builder object using NotificationCompat
        // class. This will allow control over all the flags
        NotificationCompat.Builder builder
                = new NotificationCompat
                .Builder(Applications.getAppContext(),
                channel_id)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(false)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setOnlyAlertOnce(true)
                .setOngoing(true)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent);


        notificationManager1 = (NotificationManager) Applications.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
        // Check if the Android Version is greater than Oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channel_id, "web_app", NotificationManager.IMPORTANCE_HIGH);
            notificationManager1.createNotificationChannel(notificationChannel);
        }
        notificationManager1.notify(1, builder.build());
    }

    @SuppressLint("NewApi")
    @Override
    public void onReceive(Context context, Intent intent) {
        if(!IS_SERVICE_RUNNING) {
            showNotification(context, context.getResources().getString(R.string.app_name), "Tap to Cancel");
            Log.d("TAG", "onReceive: boot fareed");
        }
//        Intent serviceIntent = new Intent(context, MyService.class);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            context.getApplicationContext().startForegroundService(serviceIntent);
//        }else
//        {
//            context.getApplicationContext().startService(serviceIntent);
//        }
    }
}
