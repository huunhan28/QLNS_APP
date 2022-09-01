package com.example.food.feature.adminhome;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.food.R;
import com.example.food.databinding.FragmentEditProfileScreenBinding;
import com.example.food.dto.UserRequestForUpdate;
import com.example.food.Domain.User;
import com.example.food.util.AppUtils;
import com.example.food.util.ChooseImageUtil;
import com.example.food.viewmodel.UserViewModel;
import com.google.gson.Gson;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditProfileFragmentAdmin extends AppCompatActivity {

    private String KEY_AVT = "file";
    private User userTemp;

    private FragmentEditProfileScreenBinding binding;
    private String CODE = "choose_image_avt";
    private ProgressDialog mProgressDialog;
    private UserViewModel userViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentEditProfileScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initProgressBar();
        setControls();
        setEvents();

    }
   
    private void initProgressBar() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait...");
    }

    private void setEvents() {
        binding.btnChooseImageAvtEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseImageUtil.chooseImage(EditProfileFragmentAdmin.this, (ImageView) binding.imgAvtEditProfileScreen, CODE);
            }
        });

        binding.btnSaveEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser();

                startActivity(new Intent(EditProfileFragmentAdmin.this, ProfileScreenFragmentAdmin.class));
            }
        });

        binding.btnBackSignUp.setOnClickListener(view -> {
            finish();
//            startActivity(new Intent(EditProfileFragmentAdmin.this, ProfileScreenFragmentAdmin.class));
        });
    }

    private void updateUser() {
        mProgressDialog.show();
        setUpParameters();

    }

    @SuppressLint("CheckResult")
    private void setUpParameters() {
        UserRequestForUpdate user = getUserRequestForUpdateFromView();
        RequestBody requestBodyUser = RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(user));
        String imageChoosed = ChooseImageUtil.getImageString(CODE);
        int id = AppUtils.getAccount(this.getSharedPreferences(AppUtils.ACCOUNT, 0)).getId();
        if(imageChoosed==null || imageChoosed.equals("")){
            userViewModel.apiUpdateUserNotImage(id, requestBodyUser)
                    .subscribe(userResponse -> {
                        if(userResponse.code()==200){
                            System.out.println("Upload file success");
                        }else{
                            Toast.makeText(this,
                                    AppUtils.getErrorMessage(userResponse.errorBody().string()),
                                    Toast.LENGTH_SHORT).show();
                        }
                        mProgressDialog.dismiss();
                    }, throwable -> {
                        Toast.makeText(this,
                                throwable.getLocalizedMessage(),
                                Toast.LENGTH_SHORT).show();
                    });
        }else{
            String realPath = ChooseImageUtil.getImageString(CODE).split("//")[1].trim();
            File file  = new File(realPath);
            RequestBody requestBodyFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part multipartAvt = MultipartBody.Part.createFormData(KEY_AVT, file.getName(), requestBodyFile);
            userViewModel.apiUpdateUser(id, requestBodyUser, multipartAvt)
                    .subscribe(userResponse -> {
                        if(userResponse.code()==200){
                            System.out.println("Upload file success");
                        }else{
                            Toast.makeText(this,
                                    AppUtils.getErrorMessage(userResponse.errorBody().string()),
                                    Toast.LENGTH_SHORT).show();
                        }
                        mProgressDialog.dismiss();
                    }, throwable -> {
                        Toast.makeText(this,
                                throwable.getLocalizedMessage(),
                                Toast.LENGTH_SHORT).show();
                    });
        }







    }

    private void setControls() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.getUser(AppUtils.getAccount2(this).getId());
        userViewModel.getuserMultable().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                loadInfoUser(user);
            }
        });


//        File file = new File();

    }

    private void loadInfoUser(User user) {
        if(user.getImageUser()!=null)
            Glide.with(this).load(user.getImageUser().getLink()).into(binding.imgAvtEditProfileScreen);
        else{
            Glide.with(this).load(R.drawable.user_icon).into(binding.imgAvtEditProfileScreen);

        }
//        Picasso.get()
//                .load(user.getImageUser().getLink())
//                .into(binding.imgAvtEditProfileScreen);
        binding.editTextNameEditProfile.setText(user.getName());
        userTemp = user;
        binding.editTextAddressEditProfile.setText(user.getAddress());
    }

    private UserRequestForUpdate getUserRequestForUpdateFromView() {
        if (checkInput()) {
            return new UserRequestForUpdate(
                    binding.editTextNameEditProfile.getText().toString(),
                    userTemp.getEmail().toString(),
                    binding.editTextAddressEditProfile.getText().toString()
            );
        } else return null;

    }

    private boolean checkInput() {
        return true;
    }
}
