package com.snappretail.trackingapp;

import static com.snappretail.trackingapp.AutoStart.IS_SERVICE_RUNNING;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.snappretail.trackingapp.service.MyService;

public class LocationServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locationserviceactivity);
        GPS_status_Broadcast m_gpsChangeReceiver = new GPS_status_Broadcast();
        this.registerReceiver(m_gpsChangeReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
        try {
            NotificationManager notificationManager = (NotificationManager) Applications.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(1);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("TAG", "onCreate: notification nh gayi" + e.getLocalizedMessage());
        }

        //if(!Utils.statusCheck(this))
        //{
        //  Utils.buildAlertMessageNoGps(this);
        //}
        if(!IS_SERVICE_RUNNING) {
            startService();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Applications.isActive = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("TAG", "onStop: is active false");
        Applications.isActive = false;
    }

    private void startworker() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresCharging(true)
                .build();
        final OneTimeWorkRequest simpleRequest = new OneTimeWorkRequest.Builder(MyWorker.class)

                // .setInitialDelay(30, TimeUnit.SECONDS)
                .addTag("simple_work")
                .build();
        WorkManager.getInstance().enqueue(simpleRequest);

    }

    public void startService() {
        Log.d("TAG", "startService called");

        Intent serviceIntent = new Intent(this, MyService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(this, serviceIntent);
        }else
        {
            startService(serviceIntent);
        }
       // ContextCompat.startForegroundService(this, serviceIntent);
        IS_SERVICE_RUNNING=true;

    }
    boolean doubleBackToExitPressedOnce=false;
    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}