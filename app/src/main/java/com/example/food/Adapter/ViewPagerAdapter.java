package com.example.food.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.food.feature.account.ProfileScreenFragment;
import com.example.food.feature.adminhome.AdminOrderFragment;
import com.example.food.feature.adminhome.HomeAdminFragment;
import com.example.food.feature.adminhome.ProfileScreenFragmentAdmin;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new HomeAdminFragment();
            case 1: return new AdminOrderFragment();
            case 2: return new ProfileScreenFragmentAdmin();
            default:return new HomeAdminFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
