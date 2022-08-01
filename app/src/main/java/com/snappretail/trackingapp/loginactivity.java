package com.snappretail.trackingapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.snappretail.trackingapp.Interface.MyWebService;
import com.snappretail.trackingapp.Utils.Helper;
import com.snappretail.trackingapp.Utils.Utils;
import com.snappretail.trackingapp.databinding.ActivityLoginBinding;
import com.snappretail.trackingapp.model.login_model.LoginModel;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class loginactivity extends AppCompatActivity {
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    String latitude, longitude;
    SharedPreferences sharedPreferences;
    private ActivityLoginBinding binding;
    private FusedLocationProviderClient fusedLocationClient;

    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {

        String deviceId;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            deviceId = Settings.Secure.getString(
                    context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephony.getDeviceId() != null) {
                deviceId = mTelephony.getDeviceId();
            } else {
                deviceId = Settings.Secure.getString(
                        context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }
        }

        return deviceId;
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        getLocation();
        binding.loginBtn.setOnClickListener(v -> {
            getLogin();
        });
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            latitude = Double.toString(location.getLatitude());
                            longitude = Double.toString(location.getLongitude());
                        }
                    }
                });
    }

    private void getLogin() {
        if (binding.username.getText().toString().equalsIgnoreCase("")) {
            binding.username.setError("Enter Correct Username");
            return;
        } else if (binding.username.getText().toString().equalsIgnoreCase("")) {
            binding.passwordToggle.setError("Enter Password");
            return;
        } else {

            if (!Utils.statusCheck(loginactivity.this)) {
                Utils.buildAlertMessageNoGps(this);
            } else {
                sendloginresponse();
            }

        }//        MyWebService myWebService = Helper.myWebService();
//        Call<LoginModel> logingetUserLogin = myWebService.getLogingetUserLogin(binding.username.getText().toString(), binding.passwordToggle.getText().toString(),getDeviceId(getApplicationContext()),latitude,longitude);
//        AlertDialog.Builder mBulider = new AlertDialog.Builder(this);
//        View mview = getLayoutInflater().inflate(R.layout.progressdailog, null);
//        mBulider.setView(mview);
//        AlertDialog dialog = mBulider.create();
//        dialog.setCancelable(false);
//        dialog.show();
//        logingetUserLogin.enqueue(new Callback<LoginModel>() {
//            @Override
//            public void onResponse(@NonNull Call<LoginModel> call, @NonNull Response<LoginModel> response) {
//            if(response.isSuccessful()){
//
//                if(response.code()>=200 && response.code()<=299)
//                {
//                    if (response.body() != null) {
//                        if (response.body().getState().equalsIgnoreCase("Access Granted")){
//                            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor myEdit = sharedPreferences.edit();
//                            myEdit.putString("firstlogin","true");
//                            myEdit.putString("representativeID",Integer.toString(response.body().getLoginResp().getRepresentativeID()) );
//                            myEdit.putString("applicationID",response.body().getLoginResp().getId());
//                            myEdit.commit();
//                            Intent intent = new Intent(loginactivity.this, LocationServiceActivity.class);
//                            startActivity(intent);
//
//                        }else
//                        {
//                            Toast.makeText(loginactivity.this, response.body().getState(), Toast.LENGTH_SHORT).show();
//                            dialog.dismiss();
//                        }
//                        //Log.d("TAG", "onResponse: "+response.body().getLoginResp().getIsActive());
//                    }
//                    else{
//                        Toast.makeText(loginactivity.this, "hello world", Toast.LENGTH_SHORT).show();
//
//                        dialog.dismiss();
//                    }
//                }
//                else if (response.code() == 400) {
//                    dialog.dismiss();
//
//                } else if (response.code() == 401) {
//
//                    dialog.dismiss();
//                } else if (response.code() == 404) {
//                    Log.d("TAG", "onResponse: error");
//                    dialog.dismiss();
//                } else if (response.code() == 429) {
//
//                    dialog.dismiss();
//                } else if (response.code() == 414) {
//
//                    dialog.dismiss();
//                } else if (response.code() == 408) {
//
//                    dialog.dismiss();
//                } else if (response.code() == 500) {
//
//                    dialog.dismiss();
//                } else if (response.code() == 503) {
//
//                    dialog.dismiss();
//                }
//            }else
//            {   dialog.dismiss();
//                Toast.makeText(loginactivity.this, "Not Successfull", Toast.LENGTH_SHORT).show();
//
//            }
//
//            }
//
//            @Override
//            public void onFailure(Call<LoginModel> call, Throwable t) {
//                Log.d("TAG", "onErrorResponse: error : [" + t.getMessage() + "]");
//
//            }
//        });
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean isConnected() throws InterruptedException, IOException {
        String command = "ping -c 1 google.com";
        return Runtime.getRuntime().exec(command).waitFor() == 0;
    }

    void sendloginresponse() {
        if (isNetworkAvailable()) {
            MyWebService myWebService = Helper.myWebService();
            Call<LoginModel> logingetUserLogin = myWebService.getLogingetUserLogin(binding.username.getText().toString().trim(), binding.passwordToggle.getText().toString().trim(), getDeviceId(getApplicationContext()), latitude, longitude);
          //  AlertDialog.Builder mBulider = new AlertDialog.Builder(this);
         //   View mview = getLayoutInflater().inflate(R.layout.progressdailog, null);

           // mBulider.setView(mview);
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.progressdailog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            try {
                dialog.setCancelable(false);
                dialog.show();
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("TAG", "sendloginresponse: " + e.getLocalizedMessage());
            }
            logingetUserLogin.enqueue(new Callback<LoginModel>() {
                @Override
                public void onResponse(@NonNull Call<LoginModel> call, @NonNull Response<LoginModel> response) {
                    if (response.isSuccessful()) {

                        if (response.code() >= 200 && response.code() <= 299) {
                            if (response.body() != null) {
                                if (response.body().getState().equalsIgnoreCase("Access Granted")) {
                                    @SuppressLint("CommitPrefEdits") SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                    myEdit.putString("firstlogin", "true");
                                    myEdit.putString("representativeID", Integer.toString(response.body().getLoginResp().getRepresentativeID()));
                                    myEdit.putString("applicationID", response.body().getLoginResp().getId());
                                    myEdit.commit();
                                    Intent intent = new Intent(loginactivity.this, LocationServiceActivity.class);
                                    startActivity(intent);

                                } else {
                                    Toast.makeText(loginactivity.this, response.body().getState(), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                                Log.d("TAG", "onResponse: "+response.body().getLoginResp().getIsActive());
                            } else {
                                Toast.makeText(loginactivity.this, "hello world", Toast.LENGTH_SHORT).show();

                                dialog.dismiss();
                            }
                        } else if (response.code() == 400) {
                           dialog.dismiss();

                        } else if (response.code() == 401) {

                            dialog.dismiss();
                        } else if (response.code() == 404) {
                            Log.d("TAG", "onResponse: error");
                          dialog.dismiss();
                        } else if (response.code() == 429) {

                           dialog.dismiss();
                        } else if (response.code() == 414) {

                           dialog.dismiss();
                        } else if (response.code() == 408) {

                           dialog.dismiss();
                        } else if (response.code() == 500) {

                          dialog.dismiss();
                        } else if (response.code() == 503) {

                         dialog.dismiss();
                        }
                    } else {
                       dialog.dismiss();
                        Toast.makeText(loginactivity.this, "Not Successfull", Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onFailure(Call<LoginModel> call, Throwable t) {
                    Log.d("TAG", "onErrorResponse: error : [" + t.getMessage() + "]");

                }
            });
        } else {
            Toast.makeText(this, "Network Not connected", Toast.LENGTH_SHORT).show();
        }
    }


}