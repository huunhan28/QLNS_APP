package com.example.food.feature.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.food.R;
import com.example.food.databinding.FragmentProfileScreenBinding;
import com.example.food.Domain.User;
import com.example.food.util.AppUtils;
import com.example.food.viewmodel.UserViewModel;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.disposables.CompositeDisposable;

public class ProfileScreenFragment extends Fragment {

    private FragmentProfileScreenBinding binding;
    private User user;
    private boolean checkUser = false;
    private UserViewModel userViewModel;
    private CompositeDisposable disposable;
    private TextView txtName, txtUsername;
    private CircleImageView imgAvt;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileScreenBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        setControls();
        loadUser();
        setEvents();
        onChangeUser();

    }

    private void onChangeUser() {

    }

    private void setEvents() {
        binding.btnBackProfile.setOnClickListener(view -> {
            NavDirections action = ProfileScreenFragmentDirections.actionProfileScreenFragmentToHomeScreenFragment();
            Navigation.findNavController(view).navigate(action);
        });

        binding.btnEditProfileScreen.setOnClickListener(view -> {
            NavDirections action = ProfileScreenFragmentDirections.actionProfileScreenFragmentToEditProfileFragment();
            Navigation.findNavController(view).navigate(action);
        });

        binding.btnChangePasswordProfileScreen.setOnClickListener(view -> {
            NavDirections action = ProfileScreenFragmentDirections.actionProfileScreenFragmentToChangePasswordFragment();
            Navigation.findNavController(view).navigate(action);

        });

        binding.btnLogOutProfileScreen.setOnClickListener(view -> {
            AppUtils.deleteAccount2(requireContext());
            navigateToSignin(view);
        });
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navigateToHome(requireView());
            }
        });

    }

    private void navigateToHome(View requireView) {
        NavDirections navDirections = ProfileScreenFragmentDirections.actionProfileScreenFragmentToHomeScreenFragment();
        Navigation.findNavController(requireView).navigate(navDirections);
    }

    private void navigateToSignin(View view) {
        NavDirections action = ProfileScreenFragmentDirections.actionProfileScreenFragmentToSigninFragment();
        Navigation.findNavController(view).navigate(action);
    }

    private void setControls() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        txtName = binding.txtNameProfileScreen;
        txtUsername = binding.txtUserNameProfileScreen;
        imgAvt = binding.imgAvtProfileScreen;
    }

    private void loadUser() {
        user = AppUtils.getAccount2(requireContext());
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
            userViewModel.getuserMultable().observe(requireActivity(), new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    loadInfoUser(user);
                }
            });

//            Picasso.get().load(user)binding.imgAvtProfileScreen



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
