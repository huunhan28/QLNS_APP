package com.example.food.feature.signin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.food.Activity.OTPActivity;
import com.example.food.Api.Api;
import com.example.food.Domain.Response.OTPResponse;
import com.example.food.network.Listener.OTPResponseListener;
import com.example.food.databinding.FragmentSigninBinding;
import com.example.food.feature.adminhome.AdminActivity;
import com.example.food.Domain.User;
import com.example.food.util.AppUtils;
import com.example.food.viewmodel.UserViewModel;

public class SigninFragment extends Fragment {

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
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSigninBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("CheckResult")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        user = AppUtils.getAccount2(requireContext());
        password=AppUtils.getPassword(requireContext());
//        checkPass();

        if(user!=null) {
            if (AppUtils.PASS_LOGIN == 1) {// if has account
                userViewModel.callUpdateTokenFireBaseUser(user.getId(), AppUtils.getTokenFireBase(requireActivity()));
                if (user.getRoles().size() >= 0) {
                    if (user.getRoles().stream().filter(role -> role.getName().equals(AppUtils.ROLES[1])).findFirst().isPresent()) {
                        startActivity(new Intent(requireActivity(), AdminActivity.class));
                    } else {
                        navigateToHomeFragment();
                    }
                }
            }
        }


        setEvents();
        observer();
    }

    private void updateTokenFireBaseUser() {
        if(user!=null){
            String token = AppUtils.getTokenFireBase(requireContext());
            if(user.getTokenFireBase()!=token){
                userViewModel.callUpdateTokenFireBaseUser(user.getId(),
                        token);
                System.out.println("Token_firebase:" + token);
            }
        }
    }

    private void observer() {
//        userViewModel.getMessage().observe(requireActivity(), s -> {
//            Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show();
//        });
    }



    @SuppressLint("CheckResult")
    private void checkPass() {
        if(user!=null && !password.equalsIgnoreCase("")) {
            userViewModel.makeApiCallSignIn(user.getUsername(), password)
                    .subscribe(userDTO -> {
                        if(userDTO.code()==200){
                            if(user.getRoles().size()>=0){
                                if(user.getRoles().stream().filter(role -> role.getName().equals(AppUtils.ROLES[1])).findFirst().isPresent()){
                                    startActivity(new Intent(requireActivity(), AdminActivity.class));
                                }else{
                                    navigateToHomeFragment();
                                }
                            }

                        }else {
                            Toast.makeText(requireContext(),
                                    AppUtils.getStringFromJsonObject("message", userDTO.errorBody().string()),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }, throwable -> Toast.makeText(requireContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }


    private void setEvents() {
        binding.btnSignIn.setOnClickListener(view -> {
            singinProcess(binding.editTextUsernameSignIn.getText().toString(),
                    binding.editTextPasswordSignIn.getText().toString());
        });
        binding.btnSignUp.setOnClickListener(view -> navigateToSignUp());
//        binding.tvForgotPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                gmail=binding.editTextUsernameSignIn.getText().toString().trim();
//                if(gmail.equals("")){
//                    Toast.makeText(requireContext(),"Email can't empty",Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if(!gmail.matches("^(.+)@(\\S+)$")){
//                    Toast.makeText(requireContext(),"Email khong dung dinh dang",Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                api.sendOTP(otpResponseListener,gmail);
//            }
//        });

        binding.tvForgotPassword.setOnClickListener(view -> navigateToForgotPassword());



    }

    private void navigateToForgotPassword() {
        NavDirections action = SigninFragmentDirections.actionSigninFragmentToForgotPasswordFragment();
        Navigation.findNavController(requireView()).navigate(action);
    }

    private void navigateToSignUp() {
        NavDirections action = SigninFragmentDirections.actionSigninFragmentToSignupFragment();
        Navigation.findNavController(requireView()).navigate(action);
    }

    @SuppressLint("CheckResult")
    private void singinProcess(String username, String password) {
        userViewModel.makeApiCallSignIn(username, password).subscribe(userDTO -> {
            if(userDTO.code()==200){
                user = userDTO.body().getUser();
                AppUtils.saveAccount2(requireContext(), user);
                AppUtils.savePassword(requireContext(), password);
                updateTokenFireBaseUser();
                if(user.getRoles().size()>=0){
                    if(user.getRoles().stream().filter(role -> role.getName().equals(AppUtils.ROLES[1])).findFirst().isPresent()){
//                        navigateToAdminHome();
                        Intent i = new Intent(requireActivity(), AdminActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }else{
                        navigateToHomeFragment();
                    }
                }
            }else{
                Toast.makeText(requireActivity(), "Vui lòng kiểm tra lại thông tin đăng nhập", Toast.LENGTH_SHORT).show();
//                Toast.makeText(requireContext(),
//                                AppUtils.getStringFromJsonObject("message", userDTO.errorBody().string()),
//                        Toast.LENGTH_SHORT).show();
            }
        }, throwable -> {
//            Toast.makeText(requireContext(), throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();;

        });

    }

    private void navigateToAdminHome() {
       NavDirections action = SigninFragmentDirections.actionSigninFragmentToAdminActivity();
       Navigation.findNavController(requireView()).navigate(action);
    }

    private void navigateToHomeFragment() {
        NavDirections action = SigninFragmentDirections.actionSigninFragmentToHomeScreenFragment();
        Navigation.findNavController(requireView()).navigate(action);
    }

    private final OTPResponseListener otpResponseListener = new OTPResponseListener() {
        @Override
        public void didFetch(OTPResponse response, String message) {
            Intent intent=new Intent(requireActivity(), OTPActivity.class);
            intent.putExtra("otp",response.getOtp());
            intent.putExtra("gmail",gmail);

            startActivity(intent);
        }

        @Override
        public void didError(String message) {

        }
    };


}
