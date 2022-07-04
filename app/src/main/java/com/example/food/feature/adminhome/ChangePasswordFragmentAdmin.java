package com.example.food.feature.adminhome;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.food.Domain.request.RequestChangePassword;
import com.example.food.databinding.FragmentChangePasswordScreenBinding;
import com.example.food.Domain.User;
import com.example.food.util.AppUtils;
import com.example.food.viewmodel.UserViewModel;

public class ChangePasswordFragmentAdmin extends AppCompatActivity {

    private FragmentChangePasswordScreenBinding binding;
    private UserViewModel userViewModel;
    private String navigateTo = "Profile";
    private String phoneNumber = "";
    RequestChangePassword request;


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
        binding.btnEditProfileScreen.setOnClickListener(view -> {
            changePassword();
        } );

        // observer user change
//        userViewModel.getuserMultable().observe(this, new Observer<User>() {
//            @Override
//            public void onChanged(User user) {
//                AppUtils.saveAccount2(ChangePasswordFragmentAdmin.this, user);
//                Toast.makeText(ChangePasswordFragmentAdmin.this, "Change password success", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//
//
//        });
//
//        userViewModel.getMessage().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//                Toast.makeText(ChangePasswordFragmentAdmin.this, s, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void resetPassword() {
        RequestChangePassword request = getRequest();
        if(request!=null)
            // call api change
            userViewModel.callResetPassword(request);
    }



    private void changePassword() {
        //get input
        request = getRequest();
        if(request!=null)
        // call api change
        userViewModel.callChangePassword2(request)
                .subscribe(responseObjectResponse -> {
                    if(responseObjectResponse.code()==200){
                        User user = responseObjectResponse.body().getData();
                        AppUtils.saveAccount2(this, user);
                        AppUtils.savePassword(this, request.getNewPassword());
                        Toast.makeText(this, "Dổi mật khẩu thành công", Toast.LENGTH_LONG).show();
                        finish();
                    }else{
                        Toast.makeText(this, AppUtils.getErrorMessage(responseObjectResponse.errorBody().string()), Toast.LENGTH_LONG).show();

                    }
                });
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

            if(!request.isValidOldPassword()){
                binding.textInputCurrentPass.setError("Không được trống.");
                binding.editTextCurrentPassword.requestFocus();
                return null;
            }else{
                binding.textInputCurrentPass.setErrorEnabled(false);
            }
        if(!request.isValidNewPassword()){
            binding.textInputNewPass.setError("Không được trống.");
            binding.editTextNewPassword.requestFocus();
            return null;
        }else{
            binding.textInputNewPass.setErrorEnabled(false);
        }

        if(!request.isValidConfirmPassword()){
            binding.textInputConfirmPass.setError("Không được trống.");
            binding.editTextConfirmPass.requestFocus();
            return null;
        }else{
            binding.textInputConfirmPass.setErrorEnabled(false);
        }
        return request;
    }



    private void setControls() {
        // initialize data
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        // call api
    }
}
