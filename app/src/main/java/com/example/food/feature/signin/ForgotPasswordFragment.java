package com.example.food.feature.signin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.food.R;
import com.example.food.databinding.FragmentForgotPasswordBinding;
import com.example.food.viewmodel.UserViewModel;

public class ForgotPasswordFragment extends Fragment {
    FragmentForgotPasswordBinding binding;
    UserViewModel userViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        setEvents();
        observer();
    }

    private void setEvents() {
        binding.btnConfirm.setOnClickListener(view -> {
            String sdt = binding.editTextPhone.getText().toString();
            checkSDT(sdt);
        });
        binding.btnBack.setOnClickListener(view ->requireActivity().onBackPressed());
    }

    private void observer() {
        userViewModel.getMessage().observe(requireActivity(), s -> {
            Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show();
        });

        userViewModel.getExistUser().observe(requireActivity(), aBoolean -> {
            if(aBoolean){
                String phoneNumber = binding.editTextPhone.getText().toString();
                if(phoneNumber!=null)
                navigatoToOTPFragment(phoneNumber);
            }
        });
    }

    private void navigatoToOTPFragment(String phoneNumber) {
        Bundle bundle = new Bundle();
        bundle.putString("phoneNumber", phoneNumber );
        Navigation.findNavController(requireView()).
                navigate(R.id.action_forgotPasswordFragment_to_OTPPhoneFragment, bundle);
    }

    private void checkSDT(String sdt) {
        userViewModel.checkUserByPhoneNumber(sdt);
    }
}
