package com.example.food.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.food.R;
import com.example.food.databinding.FragmentOtpPhoneBinding;
import com.example.food.viewmodel.UserViewModel;

import java.util.Timer;
import java.util.TimerTask;

public class OTPPhoneActivity extends AppCompatActivity {
    FragmentOtpPhoneBinding binding;
    UserViewModel userViewModel;
    String next;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentOtpPhoneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        setTimer();
        setEvents();
        callApi();

    }





    private void setTimer() {
        binding.txtResendOtp.setVisibility(View.GONE);
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                binding.txtTime.setText( millisUntilFinished / 1000 +"s");
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                binding.txtResendOtp.setVisibility(View.VISIBLE);
                binding.txtTime.setText("Hết thời gian!");
            }

        }.start();
//        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(new TimerTask() {
//            int time = 59;
//            @Override
//            public void run() {
//                binding.txtTime.setText("00:" + (time<10?"0"+ time:time));
//                time--;
//                if(time<0){
//                    timer.cancel();
//                    binding.txtResendOtp.setEnabled(true);
//                }
//            }
//        }, 0, 1000);
    }

    private void callApi() {
        if(getIntent()!=null){
            String phoneNumber = getIntent().getStringExtra("phoneNumber");
            binding.txtPhoneNumber.setText(phoneNumber);
        }
        userViewModel.sendOTPToPhoneNumber(binding.txtPhoneNumber.getText().toString());
    }

    private void setEvents() {
        next=getIntent().getStringExtra("next");
        if(next!=null){
            next="signin";
        }else {
            next = "forgotpass";
        }
        binding.btnConfirm.setOnClickListener(view -> {
            String otp = binding.editTextOTP.getText().toString();
            if(userViewModel.checkOTP(otp)){
                Intent intent;
                if(next.equals("forgotpass")){
                    intent = new Intent(this, ResetPasswordActivity.class);
                }else{
                    intent = new Intent(this, SignupActivity.class);
                }
                intent.putExtra("phoneNumber", binding.txtPhoneNumber.getText().toString());
                startActivity(intent);
            }

            else {
                Toast.makeText(this, "OTP không đúng", Toast.LENGTH_SHORT).show();
            }
        });
        binding.btnBack.setOnClickListener(view -> {
            finish();
        });
        binding.editTextOTP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(binding.editTextOTP.getText().toString().length()==5){
                    binding.btnConfirm.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.txtResendOtp.setOnClickListener(view -> {
            callApi();
            setTimer();
        });
    }

}
