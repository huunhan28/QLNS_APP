package com.example.food.feature.adminhome;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.food.Activity.EditProfileActivity;
import com.example.food.Activity.SigninActivity;
import com.example.food.R;
import com.example.food.databinding.FragmentProfileScreenBinding;
import com.example.food.Domain.User;
import com.example.food.util.AppUtils;
import com.example.food.viewmodel.UserViewModel;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.disposables.CompositeDisposable;

public class ProfileScreenFragmentAdmin extends Fragment {

    public static String TAG = ProfileScreenFragmentAdmin.class.getName();
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

        binding.coordinator.setVisibility(View.GONE);
        setControls();
        setEvents();
//        loadInfoUser(AppUtils.getAccount2(requireContext()));

    }


    private void setEvents() {


        binding.btnEditProfileScreen.setOnClickListener(view -> {
            startActivity(new Intent(requireActivity(), EditProfileActivity.class));
        });

        binding.btnChangePasswordProfileScreen.setOnClickListener(view -> {
           startActivity(new Intent(requireActivity(), ChangePasswordFragmentAdmin.class));

        });

        binding.btnLogOutProfileScreen.setOnClickListener(view -> {
            AppUtils.deleteAccount2(requireContext());
            Intent i = new Intent(requireActivity(), SigninActivity.class);
           startActivity(i);
           requireActivity().finish();
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        loadInfoUser(AppUtils.getAccount2(requireContext()));


    }

    private void setControls() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        txtName = binding.txtNameProfileScreen;
        txtUsername = binding.txtUserNameProfileScreen;
        imgAvt = binding.imgAvtProfileScreen;
//        binding.btnBackProfile.setVisibility(View.GONE);
//        binding.btnEditProfileScreen.setVisibility(View.GONE);
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
            Glide.with(requireContext()).load(linkImageAvt).into(imgAvt);
        }else{
            imgAvt.setImageResource(R.drawable.ic_user);
        }

    }


}
