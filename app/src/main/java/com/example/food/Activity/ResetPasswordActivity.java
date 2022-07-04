package com.example.food.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.food.Domain.request.RequestChangePassword;
import com.example.food.databinding.FragmentChangePasswordScreenBinding;
import com.example.food.Domain.User;
import com.example.food.util.AppUtils;
import com.example.food.viewmodel.UserViewModel;

public class ResetPasswordActivity extends AppCompatActivity {

    private FragmentChangePasswordScreenBinding binding;
    private UserViewModel userViewModel;
    String password="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentChangePasswordScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setControls();
        setEvents();
    }



    private void setEvents() {
        binding.btnBackSignUp.setOnClickListener(view -> finish());
        binding.btnEditProfileScreen.setOnClickListener(view ->
                resetPassword()
        );


        // observer user change
        userViewModel.getuserMultable().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                AppUtils.saveAccount2(ResetPasswordActivity.this, user);
                AppUtils.savePassword(ResetPasswordActivity.this, password);
                Toast.makeText(ResetPasswordActivity.this, "Change password success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ResetPasswordActivity.this, HomeActivity.class));
            }


        });

        userViewModel.getMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(ResetPasswordActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetPassword() {
        RequestChangePassword request = getRequest();
        if(request!=null)
            // call api change
            userViewModel.callResetPassword(request);
    }



    private void changePassword() {
        //get input
        RequestChangePassword request = getRequest();
        if(request!=null)
            // call api change
            userViewModel.callChangePassword(request);
    }


    private RequestChangePassword getRequest() {
        String oldPass = binding.editTextCurrentPassword.getText().toString();
        String newPass = binding.editTextNewPassword.getText().toString();
        String confirmPass = binding.editTextConfirmPass.getText().toString();

        String username ="";
        if(getIntent()!=null){
            username = getIntent().getStringExtra("phoneNumber");
        }
        RequestChangePassword request = new RequestChangePassword(username, oldPass, newPass, confirmPass);

        if(!request.isValidNewPassword()){
            binding.textInputNewPass.setError("Mật khẩu ít nhất 6 kí tự");
            binding.editTextNewPassword.requestFocus();
            return null;
        }else{
            binding.textInputNewPass.setErrorEnabled(false);
        }

        if(!request.isValidConfirmPassword()){
            binding.textInputConfirmPass.setError("Mật khẩu không khớp");
            binding.editTextConfirmPass.requestFocus();
            return null;
        }else{
            binding.textInputConfirmPass.setErrorEnabled(false);
        }
        password = newPass;
        return request;
    }



    private void setControls() {
        // initialize data
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        binding.textInputCurrentPass.setVisibility(View.GONE);
        // call api
    }
}
