package com.example.food.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.food.databinding.FragmentSignupBinding;
import com.example.food.dto.UserDTO;
import com.example.food.Domain.User;
import com.example.food.util.AppUtils;
import com.example.food.viewmodel.UserViewModel;

public class SignupActivity extends AppCompatActivity {

    FragmentSignupBinding binding;
    UserViewModel userViewModel;
    UserDTO userDTOtemp;
    User user;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentSignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initData();
        addEvents();
    }

  

 

    private void initData() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        binding.btnConfirmSignUp.setOnClickListener(view -> {
            signupProcess();
        });
        String phoneNumber = getIntent().getStringExtra("phoneNumber");
        binding.editTextUsernameSignUp.setText(phoneNumber);
        binding.editTextUsernameSignUp.setEnabled(false);
    }

    @SuppressLint("CheckResult")
    private void signupProcess(){
        String username = binding.editTextUsernameSignUp.getText().toString() +"";
        String name = binding.editTextNameSignUp.getText().toString() +"";
        String password = binding.editTextPasswordSignUp.getText().toString() +"";
        String confirmPass = binding.editTextConfirmPasswordSignUp.getText().toString() +"";

        if(username.equals("")){
            binding.errorPhoneRegister.setError("Không được để trống");
            binding.editTextUsernameSignUp.requestFocus();
            return;
        }else{
            if(!username.matches("0[0-9]{9}")){
                binding.errorPhoneRegister.setError("Không đúng định dạng");
                binding.editTextUsernameSignUp.requestFocus();
                return;
            }else{
                binding.errorPhoneRegister.setErrorEnabled(false);
            }
        }

        if(name.equals("")){
            binding.errorNameRegister.setError("Không được để trống");
            binding.editTextNameSignUp.requestFocus();
            return;
        }else{
            binding.errorNameRegister.setErrorEnabled(false);

        }

        if(password.equals("")){
            binding.errorPasswordRegister.setError("Không được để trống");
            binding.editTextPasswordSignUp.requestFocus();
            return;
        }else{
            if(password.length()<6){
                binding.errorPasswordRegister.setError("Mật khẩu phải hơn 6 kí tự");
                binding.editTextPasswordSignUp.requestFocus();
            }else
                binding.errorPasswordRegister.setErrorEnabled(false);

        }

        if(confirmPass.equals("")){
            binding.errorConfirmPasswordRegister.setError("Không được để trống");
            binding.editTextConfirmPasswordSignUp.requestFocus();
            return;
        }else{
            if(!password.equals(confirmPass)){
                binding.errorConfirmPasswordRegister.setError("Mật khẩu không khớp");
                binding.editTextConfirmPasswordSignUp.requestFocus();
                return;
            }else{
                binding.errorConfirmPasswordRegister.setErrorEnabled(false);
            }
        }
        if(password.equals(confirmPass)) {
            userViewModel.makeApiCallSignUp(username, name, password).subscribe(
                    userDTO -> {
                        userDTOtemp = userDTO.body();
                        if (userDTO.isSuccessful() && userDTOtemp.getStatus().equalsIgnoreCase("Ok")) {
                            user = userDTOtemp.getUser();
                            AppUtils.saveAccount2(this, user);
                            AppUtils.savePassword(this, password);
//                            Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                            userViewModel.callUpdateTokenFireBaseUser(user.getId(), AppUtils.getTokenFireBase(this));
                            navigateToHome();
                        }else{
                            Toast.makeText(this, "Số điện thoại đã đăng ký rồi", Toast.LENGTH_SHORT).show();
                        }
                    }
                    , throwable -> {
                        Toast.makeText(this, throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void navigateToHome() {
        startActivity(new Intent(this, HomeActivity.class));
    }

    private void addEvents() {
        binding.btnBackSignUp.setOnClickListener(view -> finish());
    }

}
