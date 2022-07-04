package com.example.food.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food.Adapter.PopularAdapter;
import com.example.food.Api.Api;
import com.example.food.Domain.Category;
import com.example.food.Domain.Product;
import com.example.food.network.Listener.CategoryResponseListener;
import com.example.food.network.Listener.ProductResponseListener;
import com.example.food.R;
import com.example.food.Domain.User;
import com.example.food.util.AppUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
private RecyclerView.Adapter adapter,adapter2;
private RecyclerView recyclerViewCategoryList,recyclerViewPopularList;

private User user;
private TextView txtName;
LinearLayout btnSetting, btnSupport;

TextView textViewSeeAllCategory,textViewSeeAllProduct;
Api api;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);



        addControls();
        addEvents();

        setControl();
        setEvent();

        recyclerViewCategory();
        recyclerViewPopular();
        bottomNavigation();
    }


    @Override
    protected void onStart() {
        super.onStart();
        user = AppUtils.getAccount(getSharedPreferences(AppUtils.ACCOUNT, MODE_PRIVATE));
        if(user!=null)
            txtName.setText(user.getUsername());
        else txtName.setText("Hello world");

    }

    private void addEvents() {
        btnSupport.setOnClickListener(view -> {
            AppUtils.deleteAccount(getSharedPreferences(AppUtils.ACCOUNT, MODE_PRIVATE));
            getSharedPreferences("username", MODE_PRIVATE).edit().putString("username", user.getUsername()).apply();
//            startActivity(new Intent(MainActivity.this, SigninActivity.class));


        });
    }

    private void addControls() {
        txtName = findViewById(R.id.txt_name_main);
        btnSupport = findViewById(R.id.supportBtn);
    }




    private void setEvent() {
        textViewSeeAllProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ProductListActivity.class));
            }
        });
        textViewSeeAllCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CategoryListActivity.class));
            }
        });
    }

    private void setControl() {
        textViewSeeAllCategory=findViewById(R.id.textViewSeeAllCategory);
        textViewSeeAllProduct=findViewById(R.id.textViewSeeAllProduct);
    }

    private void bottomNavigation() {
        FloatingActionButton floatingActionButton=findViewById(R.id.cart_btn);
        LinearLayout homeBtn=findViewById(R.id.homeBtn);
        btnSetting = findViewById(R.id.settingBtn);

//        btnSetting.setOnClickListener(view -> startActivity(
////                new Intent(MainActivity.this, SigninActivity.class)));

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CartListActivity.class));
            }
        });
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, CartListActivity.class));
            }
        });

    }

    private void recyclerViewPopular() {
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerViewPopularList=findViewById(R.id.recyclerView2);
        recyclerViewPopularList.setLayoutManager(linearLayoutManager);
        api = new Api(MainActivity.this);
        api.getProducts(productResponseListener);
    }

    private void recyclerViewCategory() {
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerViewCategoryList=findViewById(R.id.recyclerView);
        recyclerViewCategoryList.setLayoutManager(linearLayoutManager);
        api = new Api(MainActivity.this);
        api.getCategories(categoryResponseListener);

    }
    private final CategoryResponseListener categoryResponseListener = new CategoryResponseListener() {
        @Override
        public void didFetch(ArrayList<Category> response, String message) {
//            adapter=new CategoryAdapter(response);
//            recyclerViewCategoryList.setAdapter(adapter);
        }

        @Override
        public void didError(String message) {
            Log.d("api", message);
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
        }
    };

    private final ProductResponseListener productResponseListener = new ProductResponseListener(){
        @Override
        public void didFetch(ArrayList<Product> response, String message) {
            adapter2 = new PopularAdapter(response);
            recyclerViewPopularList.setAdapter(adapter2);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(MainActivity.this,"Call api error"+message.toString(),Toast.LENGTH_SHORT).show();
            Log.d("zzz",message.toString());
        }
    } ;
}