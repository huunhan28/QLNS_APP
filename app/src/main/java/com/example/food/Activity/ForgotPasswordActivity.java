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
    String next;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        next = getIntent().getStringExtra("next");
        setEvents();
        observer();

    }


    private void setEvents() {
        binding.btnConfirm.setOnClickListener(view -> {
            String email = binding.editTextPhone.getText().toString();
            if(!email.matches("^(.+)@(\\S+)$")){
                binding.textInputLayoutPhone.setError("Email không đúng định dạng");
                return;
            }
            checkSDT(email);
            return;
        });
        title = getIntent().getStringExtra("title");
        if (title != "Quên mật khẩu") {
            binding.textViewTitle.setText(title);
            binding.textViewDescription.setText("Vui lòng nhập email để xác thực đăng ký");
        } else {
            binding.textViewTitle.setText("Quên mật khẩu");
        }
        binding.btnBack.setOnClickListener(view -> finish());
    }

    private void observer() {
        userViewModel.getExistUser().observe(this, aBoolean -> {
            boolean check = false;
            String phoneNumber = binding.editTextPhone.getText().toString();
            if (title.equals("Quên mật khẩu")) {
                if (aBoolean) {
                    check = true;
                    binding.textInputLayoutPhone.setErrorEnabled(false);
                } else {
                    binding.textInputLayoutPhone.setError("Email chưa đăng ký tài khoản");
                }
            } else {
                if (aBoolean) {
                    binding.textInputLayoutPhone.setError("Email đã đăng ký tài khoản");
                } else {
                    binding.textInputLayoutPhone.setErrorEnabled(false);
                    check = true;
                }
            }
            if (check) {
                if (phoneNumber != null) {
                    Intent intent = new Intent(this, OTPPhoneActivity.class);
                    intent.putExtra("phoneNumber", phoneNumber);
                    intent.putExtra("next", next);
                    startActivity(intent);
                }
            }

        });
    }


    private void checkSDT(String sdt) {
        userViewModel.checkUserByPhoneNumber(sdt);
    }
}
