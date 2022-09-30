package com.example.food.Activity;

import android.content.Intent;
import android.os.Bundle;
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

public class ChangePasswordActivity extends AppCompatActivity {

    private FragmentChangePasswordScreenBinding binding;
    private UserViewModel userViewModel;
    String password="";
    User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentChangePasswordScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        user = AppUtils.getAccount2(this);
        setControls();
        setEvents();
    }


   

    private void setEvents() {
        binding.btnBackSignUp.setOnClickListener(view -> finish());
        binding.btnEditProfileScreen.setOnClickListener(view ->{
            RequestChangePassword request = getRequest();
            if(request!=null)
                // call api change
                userViewModel.callChangePassword(request);
        });


        // observer user change
        userViewModel.getuserMultable().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                AppUtils.saveAccount2(ChangePasswordActivity.this, user);
                AppUtils.savePassword(ChangePasswordActivity.this, password);
                Toast.makeText(ChangePasswordActivity.this, "Change password success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ChangePasswordActivity.this, ProfileActivity.class));
            }


        });

        userViewModel.getMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(ChangePasswordActivity.this, s, Toast.LENGTH_SHORT).show();
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

    }


    private RequestChangePassword getRequest() {
        String oldPass = binding.editTextCurrentPassword.getText().toString();
        String newPass = binding.editTextNewPassword.getText().toString();
        String confirmPass = binding.editTextConfirmPass.getText().toString();
        User user = AppUtils.getAccount2(this);
        String username ="";
        if(user!=null){
            username = user.getUsername();
        }


        RequestChangePassword request = new RequestChangePassword(username, oldPass, newPass, confirmPass);

        if(oldPass.equals("") || oldPass.length() < 6){
            binding.textInputCurrentPass.setError("Mật khẩu ít nhất 6 kí tự");
            binding.editTextCurrentPassword.requestFocus();
            return null;
        }else{
            binding.textInputCurrentPass.setErrorEnabled(false);
        }
        String password = AppUtils.getPassword(this);
        if(!oldPass.equals(password)){
            binding.textInputCurrentPass.setError("Mật khẩu hiện tại không đúng");
            binding.editTextCurrentPassword.requestFocus();
            return null;
        }else{
            binding.textInputCurrentPass.setErrorEnabled(false);
        }


        if(newPass.equals("") || newPass.length() < 6){
            binding.textInputNewPass.setError("Mật khẩu ít nhất 6 kí tự");
            binding.editTextNewPassword.requestFocus();
            return null;
        }else{
            binding.textInputNewPass.setErrorEnabled(false);
        }

        if(!newPass.equals(confirmPass)){
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
        // call api
    }
}
