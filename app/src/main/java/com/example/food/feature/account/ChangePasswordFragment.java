package com.example.food.feature.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.food.Domain.request.RequestChangePassword;
import com.example.food.databinding.FragmentChangePasswordScreenBinding;
import com.example.food.Domain.User;
import com.example.food.util.AppUtils;
import com.example.food.viewmodel.UserViewModel;

public class ChangePasswordFragment extends Fragment {

    private FragmentChangePasswordScreenBinding binding;
    private UserViewModel userViewModel;
    private String navigateTo = "Profile";
    private String phoneNumber = "";
    String password="";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChangePasswordScreenBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        setControls();
        setEvents();
    }

    private void setEvents() {
        binding.btnBackSignUp.setOnClickListener(view -> navigateToProfile(view));
        binding.btnEditProfileScreen.setOnClickListener(view -> {
            if(navigateTo.equals("Home"))
                resetPassword();
            else
            changePassword();
        } );


        // observer user change
        userViewModel.getuserMultable().observe(requireActivity(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                AppUtils.saveAccount2(requireContext(), user);
                AppUtils.savePassword(requireContext(), password);
                Toast.makeText(requireContext(), "Change password success", Toast.LENGTH_SHORT).show();
                if(navigateTo.equals("Profile"))
                    navigateToProfile(requireView());
                else {

                    navigateToHome();
                }
            }


        });

        userViewModel.getMessage().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetPassword() {
        RequestChangePassword request = getRequest();
        if(request!=null)
            // call api change
            userViewModel.callResetPassword(request);
    }

    private void navigateToHome() {
        NavDirections action = ChangePasswordFragmentDirections.actionChangePasswordFragmentToHomeScreenFragment();
        Navigation.findNavController(requireView()).navigate(action);
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
        User user = AppUtils.getAccount2(requireContext());
        String username ="";
        if(user!=null){
            username = user.getUsername();
        }else {
            username = getArguments().getString("phoneNumber");
        }


        RequestChangePassword request = new RequestChangePassword(username, oldPass, newPass, confirmPass);
        if(navigateTo.equals("Profile")){
            if(!request.isValidOldPassword()){
                binding.textInputCurrentPass.setError("Not empty.");
                binding.editTextCurrentPassword.requestFocus();
                return null;
            }else{
                binding.textInputCurrentPass.setErrorEnabled(false);
            }
        }


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

    private void navigateToProfile(View view) {
        NavDirections action = ChangePasswordFragmentDirections.actionChangePasswordFragmentToProfileScreenFragment();
        Navigation.findNavController(view).navigate(action);
    }

    private void setControls() {
        // initialize data
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        // call api

        if(getArguments()!=null){
            phoneNumber = getArguments().getString("phoneNumber", "");
            navigateTo = getArguments().getString("navigateTo","Profile");
            if(navigateTo.equals("Home")){
                binding.textInputCurrentPass.setVisibility(View.GONE);
                binding.btnBackSignUp.setVisibility(View.GONE);
            }
        }
    }
}
