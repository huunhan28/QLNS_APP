package com.example.food.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.food.Api.Api;
import com.example.food.R;
import com.example.food.databinding.FragmentSigninBinding;
import com.example.food.feature.adminhome.AdminActivity;
import com.example.food.feature.signin.SigninFragment;
import com.example.food.Domain.User;
import com.example.food.util.AppUtils;
import com.example.food.viewmodel.UserViewModel;

import dmax.dialog.SpotsDialog;

public class SigninActivity extends AppCompatActivity {
    public static final String TAG = SigninFragment.class.getName();

    FragmentSigninBinding binding;
    Api api;
    private UserViewModel userViewModel;
    private User user;
    private CheckBox cbRemember;
    String gmail="";
    boolean checkPass;
    String password;
    boolean updateTokenFireBase = false;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentSigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initData();
        setEvents();

    }

    private void setEvents() {
        binding.tvForgotPassword.setOnClickListener(view -> startActivity(new Intent(this, ForgotPasswordActivity.class)));
        binding.btnSignIn.setOnClickListener(view -> {
            alertDialog.show();
            singinProcess(binding.editTextUsernameSignIn.getText().toString(),
                    binding.editTextPasswordSignIn.getText().toString());
        });
        binding.btnSignUp.setOnClickListener(view -> navigateToEnterPhoneNumber());
    }

    private void navigateToEnterPhoneNumber() {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        intent.putExtra("title", "Đăng ký số điện thoại");
        startActivity(intent);
    }

    private void initData() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        user = AppUtils.getAccount2(this);
        password=AppUtils.getPassword(this);
        alertDialog = new SpotsDialog.Builder().setContext(this).setTheme(R.style.CustomProgressBarDialog).build();

    }

    private void updateTokenFireBaseUser() {
        if(user!=null){
            String token = AppUtils.getTokenFireBase(this);
            if(user.getTokenFireBase()!=token){
                userViewModel.callUpdateTokenFireBaseUser(user.getId(),
                        token);
                System.out.println("Token_firebase:" + token);
            }
        }
    }

    @SuppressLint("CheckResult")
    private void singinProcess(String username, String password) {
        userViewModel.makeApiCallSignIn(username, password).subscribe(userDTO -> {
            alertDialog.dismiss();
            if(userDTO.code()==200){
                user = userDTO.body().getUser();
                AppUtils.saveAccount2(this, user);
                AppUtils.savePassword(this, password);
                updateTokenFireBaseUser();
                if(user.getRoles().size()>=0){
                    if(user.getRoles().stream().filter(role -> role.getName().equals(AppUtils.ROLES[1])).findFirst().isPresent()){
                        Intent i = new Intent(this, AdminActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);

                    }else{
                        startActivity(new Intent(SigninActivity.this, HomeActivity.class));
                    }
                    finish();
                }
            }else{
                Toast.makeText(this, "Vui lòng kiểm tra lại thông tin đăng nhập", Toast.LENGTH_SHORT).show();
            }
        }, throwable -> {
            alertDialog.dismiss();
            Toast.makeText(this, throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();;

        });

    }

}