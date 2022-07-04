package com.example.food.Activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.food.databinding.FragmentForgotPasswordBinding;
import com.example.food.viewmodel.UserViewModel;

public class ForgotPasswordActivity extends AppCompatActivity {
    FragmentForgotPasswordBinding binding;
    UserViewModel userViewModel;
    String title;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        setEvents();
        observer();

    }


    private void setEvents() {
        binding.btnConfirm.setOnClickListener(view -> {
            String sdt = binding.editTextPhone.getText().toString();
            checkSDT(sdt);
        });
        title = getIntent().getStringExtra("title");
        if (title != null) {
            binding.textViewTitle.setText(title);
            binding.textViewDescription.setText("Vui lòng nhập số điện thoại để xác thực đăng ký");
        } else {
            binding.textViewTitle.setText("Quên mật khẩu");
        }
        binding.btnBack.setOnClickListener(view -> finish());
    }

    private void observer() {
        userViewModel.getExistUser().observe(this, aBoolean -> {
            boolean check = false;
            String phoneNumber = binding.editTextPhone.getText().toString();
            if (title == null) {
                if (aBoolean) {
                    check = true;
                    binding.textInputLayoutPhone.setErrorEnabled(false);
                } else {
                    binding.textInputLayoutPhone.setError("Số điện thoại chưa đăng ký tài khoản");
                }
            } else {
                if (aBoolean) {
                    binding.textInputLayoutPhone.setError("Số điện thoại đã đăng ký tài khoản");
                } else {
                    binding.textInputLayoutPhone.setErrorEnabled(false);
                    check = true;
                }
            }
            if (check) {
                if (phoneNumber != null) {
                    Intent intent = new Intent(this, OTPPhoneActivity.class);
                    intent.putExtra("phoneNumber", phoneNumber);
                    startActivity(intent);
                }
            }

        });
    }


    private void checkSDT(String sdt) {
        userViewModel.checkUserByPhoneNumber(sdt);
    }
}
