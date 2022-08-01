package com.snappretail.trackingapp.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.Priority;
import com.snappretail.trackingapp.Applications;
import com.snappretail.trackingapp.Database.AppDatabase;
import com.snappretail.trackingapp.Database.Databasesample;
import com.snappretail.trackingapp.Database.data_interface;
import com.snappretail.trackingapp.Interface.MyWebService;
import com.snappretail.trackingapp.LocationServiceActivity;
import com.snappretail.trackingapp.MainActivity;
import com.snappretail.trackingapp.R;
import com.snappretail.trackingapp.Utils.Helper;
import com.snappretail.trackingapp.Utils.Utils;
import com.snappretail.trackingapp.model.login_model.LoginModel;
import com.snappretail.trackingapp.model.relocation_model.RepLocationModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyService extends Service {
    private String TAG = "MyService";
    public static boolean isServiceRunning;
    private String CHANNEL_ID = "NOTIFICATION_CHANNEL";
    private FusedLocationProviderClient fusedLocationProviderClient;
    String representativid,applicationid;
    SharedPreferences sharedPreferences;
    Calendar c;
    AppDatabase db;
    data_interface task_interface;
    Databasesample data;
    List<Databasesample> datas;
    Context context;
    NotificationManager notificationManager;




    public MyService() {
        Log.d(TAG, "constructor called");
        isServiceRunning = false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        isServiceRunning=true;
        sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        representativid=sharedPreferences.getString("representativeID","");
        applicationid=sharedPreferences.getString("applicationID","");
        Log.d(TAG, "onCreate called");

        createNotificationChannel();
       // isServiceRunning = true;
    }

    PendingIntent pendingIntent;

    @SuppressLint({"VisibleForTests", "MissingPermission"})
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        db = AppDatabase.getInstance(getApplicationContext());
        task_interface = db.task_interface();
        Log.d(TAG, "onStartCommand called");
        fusedLocationProviderClient = new FusedLocationProviderClient(this);
        Intent notificationIntent = new Intent(this, LocationServiceActivity.class);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.S)
        {
            pendingIntent = PendingIntent.getActivity(this,
                    0, notificationIntent, PendingIntent.FLAG_MUTABLE);
        }
        else{
             pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);}
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Service is Running")
                .setContentText("Listening for Screen Off/On events")
              //  .setSmallIcon(R.drawable.ic_wallpaper_black_24dp)
                .setContentIntent(pendingIntent)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .build();

        startForeground(1, notification);


        LocationRequest  locationRequest = new LocationRequest();
        locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
     //   locationRequest.setFastestInterval(900000);
        locationRequest.setInterval(60000);
        Log.d(TAG, "onStartCommand: ++++++");
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }
            
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                try{
                if(!Utils.statusCheck(Applications.getAppContext())){
                    Utils.buildAlertMessageNoGps(
                            Applications.getAppContext()
                    );
                }
                else{
                    locationresult(locationResult);
                }

                }catch (Exception e)
                {
                    e.printStackTrace();
                    Log.d("TAG", "onLocationResult: ERROR "+e.getLocalizedMessage());
                }




            }
        }, null);

        return START_STICKY;
    }
    private void locationresult(LocationResult locationResult)
    {
        Log.d(TAG, "onLocationResult: "+locationResult.getLocations());
        String latitude =Double.toString(locationResult.getLastLocation().getLatitude()) ;
        String longitude=Double.toString(locationResult.getLastLocation().getLongitude());

        c=Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss zzz ", Locale.ENGLISH);
        String time = dateFormat.format(c.getTime());
//        NotificationCompat.Builder builder
//                = new NotificationCompat
//                .Builder(Applications.getAppContext(),
//                "noti")
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setAutoCancel(false)
//                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
//                .setOnlyAlertOnce(true)
//                .setOngoing(true)
//                .setContentTitle("Service Lat long")
//                .setContentText(time+" lat:"+latitude+" "+"long: "+longitude);
        notificationManager = (NotificationManager) Applications.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
        // Check if the Android Version is greater than Oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("noti", "web_app", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
       // notificationManager.notify(2, builder.build());
        Log.d(TAG, "onLocationResult: "+time);
        Log.d(TAG, "onLocationResult: representative ID "+representativid);
        Log.d(TAG, "onLocationResult: application ID "+applicationid);
        data = new Databasesample(time,latitude,longitude,0);

        task_interface.insert(data);
        Log.d("TAG", "onLocationResult: Saved in DB");

        // task_interface.deletealltasks();
        if(haveNetworkConnection())
        {
            Log.d(TAG, "onLocationResult:net onnn ");
            datas=task_interface.getallUnSynctasks();
            for(int i = 0;i<datas.size();i++)
            {
                sendresponse(i);

            }

//                        MyWebService myWebService = Helper.myWebService();
//                        Call<RepLocationModel> repLocationModelCall = myWebService.getLocation(time,latitude,longitude,
//                                representativid,applicationid);
//
//                        repLocationModelCall.enqueue(new Callback<RepLocationModel>() {
//                            @Override
//                            public void onResponse(@NonNull Call<RepLocationModel> call, @NonNull Response<RepLocationModel> response) {
//                                if(response.isSuccessful()){
//                                    if(response.body()!=null)
//                                    {
//                                        if(response.body().getState().equalsIgnoreCase("success"))
//                                        {
//                                            Log.d("TAG", "onResponse: successfully send ");
//                                            //task_interface.deletebyid(datas.get(finalI).getId());
//
//                                        }
//
//                                    }
//                                }
//
//
//
//
//
//                            }
//
//                            @Override
//                            public void onFailure(@NonNull Call<RepLocationModel> call, @NonNull Throwable t) {
//
//                            }
//                        });
//                        if(sendresponse(time,latitude,longitude))
//                        {
//                            Log.d(TAG, "onLocationResult: Finally Successfull ");
//                        }

        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String appName = getString(R.string.app_name);
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    appName,
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy called");
        isServiceRunning = false;
        stopForeground(true);

        // call MyReceiver which will restart this service via a worker
//        Intent broadcastIntent = new Intent(this, MyReceiver.class);
//        sendBroadcast(broadcastIntent);

        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        db = AppDatabase.getInstance(getApplicationContext());
        task_interface = db.task_interface();
        Log.d(TAG, "onBind: ");
        return null;
    }

    public  boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo[] netInfo = cm.getAllNetworkInfo();
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
    synchronized private void  sendresponse(int i)
    {
        MyWebService myWebService = Helper.myWebService();
        Log.d(TAG, "Database params: " + datas.get(i).getTask_time() + " " + datas.get(i).getLatitude() + " " + datas.get(i).getLongitude());
        Call<RepLocationModel> repLocationModelCall = myWebService.getLocation(datas.get(i).getTask_time(), datas.get(i).getLatitude(), datas.get(i).getLongitude(),
                representativid, applicationid);
        int finalI = i;
        repLocationModelCall.enqueue(new Callback<RepLocationModel>() {
            @Override
            public void onResponse(@NonNull Call<RepLocationModel> call, @NonNull Response<RepLocationModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getState().equalsIgnoreCase("success")) {
                            Log.d("TAG", "onResponse: successfully send " + datas.get(finalI).getId());
                            task_interface.setsync(datas.get(finalI).getId(), 1);

                        }

                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<RepLocationModel> call, @NonNull Throwable t) {

            }
        });
    }
 
}
