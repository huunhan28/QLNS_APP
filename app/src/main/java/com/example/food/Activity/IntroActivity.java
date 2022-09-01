package com.example.food.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.food.Domain.LocationDomain;
import com.example.food.R;
import com.example.food.feature.adminhome.AdminActivity;
import com.example.food.Domain.User;
import com.example.food.firebase.MyService;
import com.example.food.util.AppUtils;
import com.example.food.viewmodel.UserViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class IntroActivity extends AppCompatActivity {

    String password;
    UserViewModel userViewModel;
    LocationManager locationManager;
    User user;
    private MyService myService;
    private boolean isServiceConnected;
//    private ServiceConnection mServiceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//            MyService.MyBinder myBinder = (MyService.MyBinder) iBinder;
//            myService = myBinder.getMyService();
//            isServiceConnected = true;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//            myService = null;
//            isServiceConnected = false;
//
//        }
//    };
    public static final String TAG = IntroActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);
//        if (!isGooglePlayServicesAvailable(this)) {
//            Toast.makeText(this, "Not enough google play service", Toast.LENGTH_SHORT).show();
//        }
//        saveNumberOfTimeLaunchApp(this);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        user = AppUtils.getAccount(getSharedPreferences(AppUtils.ACCOUNT, MODE_PRIVATE));
        password = AppUtils.getPassword(this);
        LocationDomain location = new LocationDomain() ;
        location.setLatitude(10.84778505);
        location.setLongtitude(106.78470671);
        AppUtils.saveLocation(this,location);
        LocationDomain locationShop = new LocationDomain() ;
        locationShop.setLatitude(10.848354);
        locationShop.setLongtitude(106.774406);
        AppUtils.saveLocationShop(this,locationShop);
        checkPass();

//        getTokenFireBase();
        //getSupportActionBar().hide();
//        startServiceWithFirstTimeLaunchApp();
//        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);


    }

//    private void saveNumberOfTimeLaunchApp(Context context) {
//        int timeLaunchApp = AppUtils.getNumberOfTimeLaunchApp(context);
//        if (timeLaunchApp == -1) {
//            AppUtils.saveNumberOfTimeLaunchApp(context, 1);
//        } else {
//            AppUtils.saveNumberOfTimeLaunchApp(context, timeLaunchApp + 1);
//        }
//    }

//    private void startServiceWithFirstTimeLaunchApp() {
////        if (AppUtils.isFirstTimeLaunchApp(this)) {
////            startForegroundAndBoundService();
////        }
//        startForegroundAndBoundService();
//
//    }

//    private void startForegroundAndBoundService() {
//        Intent intent = new Intent(this, MyService.class);
//        startService(intent);
//
////        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
//    }

//    private void stopForegroundService() {
//        Intent intent = new Intent(this, MyService.class);
//        stopService(intent);
//    }

//    private void stopBoundService() {
//        if (isServiceConnected) {
//            unbindService(mServiceConnection);
//            isServiceConnected = false;
//        }
//    }


//    private void getTokenFireBase() {
//        FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(new OnCompleteListener<String>() {
//                    @Override
//                    public void onComplete(@NonNull Task<String> task) {
//                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
//                            return;
//                        }
//                        // Get new FCM registration token
//                        String token = task.getResult();
//                        AppUtils.saveTokenFireBase(IntroActivity.this, token);
//                        Log.e(TAG, "token_firebase:" + token);
//                        checkPass();
//                    }
//                });
//    }

    private void checkPass() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @SuppressLint("CheckResult")
            @Override
            public void run() {
                if (user != null && !password.equalsIgnoreCase("")) {
                    userViewModel.makeApiCallSignIn(user.getUsername(), password)
                            .subscribe(userDTO -> {
                                        if (userDTO.code() == 200) {
                                            AppUtils.PASS_LOGIN = 1;
                                            AppUtils.saveAccount2(IntroActivity.this, userDTO.body().getUser());
                                            userViewModel.callUpdateTokenFireBaseUser(user.getId(), AppUtils.getTokenFireBase(IntroActivity.this));
                                            if (user.getRoles().size() >= 0) {
                                                if (user.getRoles().stream().filter(role -> role.getName().equals(AppUtils.ROLES[1])).findFirst().isPresent()) {
                                                    startActivity(new Intent(IntroActivity.this, AdminActivity.class));
                                                } else {
                                                    startActivity(new Intent(IntroActivity.this, HomeActivity.class));

                                                }
                                            }
                                        } else {
                                            AppUtils.PASS_LOGIN = 0;
                                            startActivity(new Intent(IntroActivity.this, SigninActivity.class));
                                        }
                                        finish();
//
                                    },
                                    throwable -> Toast.makeText(IntroActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show());
                } else {
                    startActivity(new Intent(IntroActivity.this, SigninActivity.class));
                    finish();
                }

            }
        }, 0);
    }

//    public boolean isGooglePlayServicesAvailable(Activity activity) {
//        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
//        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
//        if (status != ConnectionResult.SUCCESS) {
//            if (googleApiAvailability.isUserResolvableError(status)) {
//                googleApiAvailability.getErrorDialog(activity, status, 2404).show();
//            }
//            return false;
//        }
//        return true;
//    }
//    private final LocationListener mLocationListener = location -> {
//    //        String url = "http://maps.google.com/maps?saddr="+location.getLatitude()+","+location.getLongitude()+"&daddr=10.848354,106.774406&mode=driving";
//    //        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
//    //        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
//    //        alertDialog.dismiss();
//    //        startActivity(intent);
//        AppUtils.saveLocation(this,location);
//
//    };
}