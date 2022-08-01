package com.snappretail.trackingapp;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.Priority;
import com.snappretail.trackingapp.service.MyService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MyWorker extends Worker {


    private  LocationRequest locationRequest;
    Context context;
    //   private Object Result;
    FusedLocationProviderClient  fusedLocationProviderClient;

    public MyWorker
            (@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context=context;

//        fusedLocationProviderClient.requestLocationUpdates(locationRequest,callback ,null);
    } @SuppressLint("MissingPermission")

    public void startService() {
        Log.d("TAG", "startService called");
//        if (!MyService.isServiceRunning)
        Intent serviceIntent = new Intent(context, MyService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.getApplicationContext().startForegroundService(serviceIntent);
        }else
        {
            context.getApplicationContext().startService(serviceIntent);
        }
//            ContextCompat.startForegroundService(this, serviceIntent);
//        }
    }
    @Override
    public Result doWork() {
            startService();

        Calendar c = Calendar.getInstance();
        String myFormat = "hh:mm:ss a";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        String time = dateFormat.format(c.getTime());



        if(haveNetworkConnection())
        {
            time=time+" Connected ";
        }
        else
        {
            time=time+" Not Connected ";
        }

       String title = "Message from Activity!";
        Log.d("TAG", "doWork: "+"is running");
//        sendNotification(title, time);
        final OneTimeWorkRequest simpleRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
                .setInitialDelay(10, TimeUnit.SECONDS)
                .addTag("simple_work")
                .build();
        WorkManager.getInstance().enqueue(simpleRequest);
        return Result.success();
    }
LocationCallback callback =new LocationCallback() {
        @Override
        public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
            super.onLocationAvailability(locationAvailability);
        }

        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Log.d("TAG", "onLocationResult: "+locationResult.getLastLocation());
        }
    };
    public void sendNotification(String title, String message) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        //If on Oreo then notification required a notification channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Tracker", "Worker_manager",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "Tracker")
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher);

        notificationManager.notify(1, notification.build());
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;


        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    @Override
    public void onStopped() {
        super.onStopped();
        MyService.isServiceRunning=false;
    }


}
