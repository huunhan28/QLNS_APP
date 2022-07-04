package com.example.food.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food.Domain.Category;
import com.example.food.R;
import com.example.food.databinding.FragmentProductListByCategoryBinding;
import com.example.food.feature.category.CategoryDTO;
import com.example.food.feature.home.HomeViewModel;
import com.example.food.feature.product.ProductAdapter;
import com.example.food.feature.product.ProductScreenFragmentDirections;
import com.example.food.util.AppUtils;
import com.example.food.util.ItemMargin;

import dmax.dialog.SpotsDialog;

public class ProductListActivity extends AppCompatActivity {

    private FragmentProductListByCategoryBinding binding;
    private RecyclerView rvProduct;
    private ProductAdapter productAdapter;
    private HomeViewModel homeViewModel;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentProductListByCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        alertDialog = new SpotsDialog.Builder().setContext(this).setTheme(R.style.CustomProgressBarDialog).build();

        rvProduct = binding.recyclerViewProductsByCategory;
        rvProduct.addItemDecoration(new ItemMargin(0,0, 0, 10));
        rvProduct.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);


        productAdapter = new ProductAdapter(homeViewModel,new ProductAdapter.ProductDiff(), R.layout.item_product_horizental);


        rvProduct.setAdapter(productAdapter);

        if(getIntent() != null) {
            // The getPrivacyPolicyLink() method will be created automatically.
            CategoryDTO categoryDTO = (CategoryDTO) getIntent().getSerializableExtra("category");
            if(categoryDTO==null){
                return;
            }
            binding.textViewNameOfCategory.setText(categoryDTO.getName());
            alertDialog.show();
            homeViewModel.getProductsByCategoryId(categoryDTO.getId())
                    .subscribe(categoryResponseResponse -> {
                        if(categoryResponseResponse.code()==200){
                            System.out.println("get product by category thanh cong");
                            productAdapter.submitList(categoryResponseResponse.body());
                            alertDialog.dismiss();
                        }
                    }, throwable -> {
                        System.out.println("throw get category:" + throwable.getMessage());
                    });
        }
        binding.btnBackProductList.setOnClickListener(view -> finish());
    }
}
