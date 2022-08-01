package com.snappretail.trackingapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

public class GPS_status_Broadcast extends BroadcastReceiver {
    String channel_id = "notification_channel";
    NotificationManager notificationManager;

    public void showNotification(Context co, String title, String message) {
        // Pass the intent to switch to the MainActivity
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
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


        notificationManager = (NotificationManager) Applications.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
        // Check if the Android Version is greater than Oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channel_id, "web_app", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(0, builder.build());
    }

    private RemoteViews getCustomDesign(String title, String message) {
        RemoteViews remoteViews = new RemoteViews(Applications.getAppContext().getPackageName(), R.layout.notification);
        remoteViews.setTextViewText(R.id.title, title);
        remoteViews.setTextViewText(R.id.message, message);
        remoteViews.setImageViewResource(R.id.icon,
                R.drawable.ic_launcher_background);
        return remoteViews;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showNotification(context, "Location Alert", "Your Location is Disable Kindly enable");
            //   context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//            if(!Applications.isActive)
//            {  Intent i = new Intent();
//                i.setClassName(context.getPackageName(), Splashactivity.class.getName());
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(i);
//            }
//            Intent in=new Intent(context,DialogMessageActivity.class);
//           // intent.putExtra("LocationMessage",);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(in);
//
            Log.d("TAG", "onReceive: gps  not enabled");
        } else {
            notificationManager = (NotificationManager) Applications.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(0);
            Log.d("TAG", "onReceive: gps enabled");
        }

    }
}
