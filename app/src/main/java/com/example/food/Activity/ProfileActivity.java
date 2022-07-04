package com.example.food.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.food.R;
import com.example.food.databinding.FragmentProfileScreenBinding;
import com.example.food.Domain.User;
import com.example.food.util.AppUtils;
import com.example.food.viewmodel.UserViewModel;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.disposables.CompositeDisposable;

public class ProfileActivity extends AppCompatActivity {

    private FragmentProfileScreenBinding binding;
    private User user;
    private boolean checkUser = false;
    private UserViewModel userViewModel;
    private CompositeDisposable disposable;
    private TextView txtName, txtUsername;
    private CircleImageView imgAvt;
    String LOCALE_VIETNAM = "vi";
    String LOCALE_ENGLISH = "en";
    Locale mLocale;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentProfileScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mLocale=getResources().getConfiguration().locale;
        Locale.setDefault(mLocale);
        Configuration config = new Configuration();
        config.locale = mLocale;
        this.getResources().updateConfiguration(config,
                this.getResources().getDisplayMetrics());
        setControls();
        setEvents();
    }
    private void setEvents() {
        binding.btnBackProfile.setOnClickListener(view -> {
            finish();
        });

        binding.btnEditProfileScreen.setOnClickListener(view -> {
            startActivity(new Intent(this, EditProfileActivity.class));

        });

        binding.btnChangePasswordProfileScreen.setOnClickListener(view -> {
            startActivity(new Intent(this, ChangePasswordActivity.class));

        });
        binding.btnLogOutProfileScreen.setOnClickListener(view -> {
            AppUtils.deleteAccount2(this);
            AppUtils.deletePassword(this);
            startActivity(new Intent(this, SigninActivity.class));
        });

    }


    private void setControls() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        txtName = binding.txtNameProfileScreen;
        txtUsername = binding.txtUserNameProfileScreen;
        imgAvt = binding.imgAvtProfileScreen;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadInfoUser(AppUtils.getAccount2(this));
    }

    private void loadUser() {
        user = AppUtils.getAccount2(this);
        if(user==null){
            binding.imgAvtProfileScreen.setImageResource(R.drawable.hashimoto);
            binding.txtNameProfileScreen.setText("Arina Hashimoto");
            binding.txtUserNameProfileScreen.setText("uid:0988958279");
            binding.btnEditProfileScreen.setText("Sign in");
            checkUser=false;
            return;
        }else{
            checkUser = true;
            int userId = user.getId();
            userViewModel.getUser(userId);
            userViewModel.getuserMultable().observe(this, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    loadInfoUser(user);
                }
            });
        }
    }

    private void loadInfoUser(User user) {
        String name = user.getName();
        String username = user.getUsername();
        String linkImageAvt="";
        if(user.getImageUser()!=null) {
            linkImageAvt = user.getImageUser().getLink();
        }
        if(name!=null && !name.equals("")){
            txtName.setText(name);
        }

        if(username!=null && !username.equals("")){
            txtUsername.setText("uid: " + username);
        }

        if(linkImageAvt!=null && !linkImageAvt.equals("")){
            Glide.with(this).load(linkImageAvt).into(imgAvt);
        }else{
            Glide.with(this).load(R.drawable.user_icon).into(imgAvt);

        }

    }


}