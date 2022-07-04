package com.example.food.Activity;


import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.food.Domain.Discount;
import com.example.food.R;
import com.example.food.databinding.FragmentDiscountDetailsBinding;
import com.example.food.feature.discountdetails.DiscountDetailsFragmentArgs;
import com.example.food.feature.discountdetails.DiscountDetailsFragmentDirections;
import com.example.food.feature.signin.SigninFragmentDirections;
import com.example.food.util.AppUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link com.example.food.feature.discountdetails.DiscountDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscountDetailsActivity extends AppCompatActivity {
    private View view;
    private FragmentDiscountDetailsBinding binding;
    private String voucherId;
    private Discount discount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentDiscountDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if(getIntent()!=null){
            Bundle bundle = getIntent().getBundleExtra("discount");
            if(bundle!=null){
                discount = (Discount) bundle.getSerializable("discount");
                if(discount!=null){
                    voucherId = discount.getId();
                    loadInfoDiscount(discount);
                }
            }
        }
        setControls();
        setEvents();
    }



    private void setEvents() {
        binding.btnBackDiscountDetails.setOnClickListener(view -> finish());
    }


    private void setControls() {
        binding.buttonUserVoucher.setOnClickListener(view -> {
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData data = ClipData.newPlainText("discountId", discount.getId());
            clipboardManager.setPrimaryClip(data);
            Toast.makeText(this, "Đã copy mã khuyến mãi vào bộ nhớ tạm", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadInfoDiscount(Discount discount) {
        int percent = (int)(discount.getPercent()*100);
        binding.textViewVoucherName.setText(discount.getId() +" | " +percent + "%");
        binding.textViewEndDateVoucher.setText(AppUtils.formatDate(discount.getEndDate(), "dd-MM-yyyy"));
        binding.textViewDescriptionInfoVoucher.setText("Voucher khuyến mãi "+percent+"% cho đơn hàng từ 50k tối đa giảm 20k cho 1 đơn hàng. Số lượng có hạn. Mại dô mại dô");

        Glide.with(this)
                .load(discount.getImageDiscount().getLink())
                .into(binding.imageViewDiscountImage);
    }
}