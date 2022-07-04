package com.example.food.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

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
    User user;
    private MyService myService;
    private boolean isServiceConnected;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MyService.MyBinder myBinder = (MyService.MyBinder) iBinder;
            myService = myBinder.getMyService();
            isServiceConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            myService = null;
            isServiceConnected = false;

        }
    };
    public static final String TAG = IntroActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);
        if (!isGooglePlayServicesAvailable(this)) {
            Toast.makeText(this, "Not enough google play service", Toast.LENGTH_SHORT).show();
        }
        saveNumberOfTimeLaunchApp(this);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        user = AppUtils.getAccount(getSharedPreferences(AppUtils.ACCOUNT, MODE_PRIVATE));
        password = AppUtils.getPassword(this);

        getTokenFireBase();
        //getSupportActionBar().hide();
        startServiceWithFirstTimeLaunchApp();


    }

    private void saveNumberOfTimeLaunchApp(Context context) {
        int timeLaunchApp = AppUtils.getNumberOfTimeLaunchApp(context);
        if (timeLaunchApp == -1) {
            AppUtils.saveNumberOfTimeLaunchApp(context, 1);
        } else {
            AppUtils.saveNumberOfTimeLaunchApp(context, timeLaunchApp + 1);
        }
    }

    private void startServiceWithFirstTimeLaunchApp() {
//        if (AppUtils.isFirstTimeLaunchApp(this)) {
//            startForegroundAndBoundService();
//        }
        startForegroundAndBoundService();

    }

    private void startForegroundAndBoundService() {
        Intent intent = new Intent(this, MyService.class);
        startService(intent);

//        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void stopForegroundService() {
        Intent intent = new Intent(this, MyService.class);
        stopService(intent);
    }

    private void stopBoundService() {
        if (isServiceConnected) {
            unbindService(mServiceConnection);
            isServiceConnected = false;
        }
    }


    private void getTokenFireBase() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        AppUtils.saveTokenFireBase(IntroActivity.this, token);
                        Log.e(TAG, "token_firebase:" + token);
                        checkPass();
                    }
                });
    }

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

    public boolean isGooglePlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, 2404).show();
            }
            return false;
        }
        return true;
    }
}