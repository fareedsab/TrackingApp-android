package com.snappretail.trackingapp;

import static com.snappretail.trackingapp.Utils.Utils.buildAlertMessageNoGps;
import static com.snappretail.trackingapp.Utils.Utils.statusCheck;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.snappretail.trackingapp.Utils.Utils;
import com.snappretail.trackingapp.databinding.ActivitySplashScreenBinding;

public class Splashactivity extends AppCompatActivity {
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private ActivitySplashScreenBinding binding;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen);
        initAnimation();



    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            dialog = Utils.LocationPermissionALert(this);
        } catch (Exception e) {
            Log.d("TAG", "onCreate: "+ e.getLocalizedMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(Splashactivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        },
                        REQUEST_CODE_LOCATION_PERMISSION);
            } else {
                ActivityCompat.requestPermissions(Splashactivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        },
                        REQUEST_CODE_LOCATION_PERMISSION);
            }
        } else {

            setappDirection();
            //     }
        }

    }

    private void initAnimation() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            getWindow().getInsetsController().hide(WindowInsets.Type.statusBars());
//        } else {
//            getWindow().setFlags(
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN
//            );
//        }
//        Animation splashAnim = AnimationUtils.loadAnimation(this, R.anim.splash_animation);
//        binding.logo.setAnimation(splashAnim);
//        Path path = new Path();
//        int screenSize = getResources().getConfiguration().screenLayout &
//                Configuration.SCREENLAYOUT_SIZE_MASK;
//        path.arcTo(0f, 30f, screenSize/2, 40f, 0f, 100f, true);
//        ObjectAnimator.ofFloat(binding.btnSplash, View.X, View.Y, path).setDuration(1000).start();
       // binding.btnSplash.animate().translationX(-20);
        binding.btnSplash.animate().rotationX(360).setDuration(1000);
        binding.logo.animate().rotationX(360).setDuration(1000);
        binding.logodesc.animate().rotationX(360).setDuration(1000);

//        float parentCenterX = binding.framelayout.getX() + binding.framelayout.getWidth()/2;
//        float parentCenterY = binding.framelayout.getY() + binding.framelayout.getHeight()/2;
//        binding.btnSplash.animate().translationX(parentCenterX - binding.btnSplash.getWidth()/2).translationY(parentCenterY - binding.btnSplash.getHeight()/2);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           @NonNull int[] grantresults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantresults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantresults.length > 0) {
            if (grantresults[0] == PackageManager.PERMISSION_GRANTED) {
                if (!statusCheck(Splashactivity.this)) {
                    buildAlertMessageNoGps(Splashactivity.this);
                } else {


                    setappDirection();
                }


            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void setappDirection() {
        // SharedPreferenceManager.RemoveAllShared(this);
        if (!statusCheck(Splashactivity.this)) {
            if (!dialog.isShowing()) {
                dialog.show();
            }
            //        buildAlertMessageNoGps(Splashactivity.this);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (SharedPreferenceManager.getSomeStringValue(Splashactivity.this, "firstlogin") != null) {
                        Intent intent = new Intent(Splashactivity.this, LocationServiceActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(Splashactivity.this, loginactivity.class);
                        startActivity(intent);
                    }


                }
            }, 1500);

        }
    }
}