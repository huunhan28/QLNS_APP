package com.example.food.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.LocaleList;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.food.Domain.Discount;
import com.example.food.Domain.LocationDomain;
import com.example.food.Domain.Product;
import com.example.food.R;
import com.example.food.databinding.FragmentHomeSceenBinding;
import com.example.food.feature.category.CategoryAdapter;
import com.example.food.feature.discount.DiscountAdapter;
import com.example.food.feature.home.HomeViewModel;
import com.example.food.feature.map.MapViewActivity;
import com.example.food.feature.map.MapViewModel;
import com.example.food.feature.product.ProductAdapter;
import com.example.food.Domain.User;
import com.example.food.util.AppUtils;
import com.example.food.util.ItemMargin;

import com.example.food.viewmodel.UserViewModel;
//import com.pusher.client.Pusher;
//import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
//import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class HomeActivity extends AppCompatActivity implements DiscountAdapter.ClickItem {
    private FragmentHomeSceenBinding binding;
    private HomeViewModel homeViewModel;
    private CategoryAdapter adapterCate;
    private ProductAdapter productAdapter;
    private DiscountAdapter discountAdapter;
    private RecyclerView rvCate, rvPopular, rvDiscount;
    private SliderView slideDiscount;
    private User user;
    private TextView txtName,txtWelcome;
    private CircleImageView imgAvt;
    private UserViewModel userViewModel;
    private MapViewModel mapViewModel;
    String location = "10.84898,106.78736";
    int countUpdateAddress = 1;
    List<Product> products;
    List<Product> productsTop10;
    private List<Discount> discounts;
    int heightScroll = 0;
    int countClickSearch = 0;

    ProgressDialog progressDialog;
    private AlertDialog progressBarDialog;
    private TextView btnSeeAllCategoriesHomeScreen;
    private FloatingActionButton cartBtn;
    LocationManager locationManager;
    AlertDialog alertDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentHomeSceenBinding.inflate(getLayoutInflater());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(binding.getRoot());



        initData();
        setControls();
        if(user != null){
            setEvents();
        }

        setEvents2();

        progressBarDialog.show();
        loadDiscount();
        loadCategories();
        loadProducts();
//        setUpPusher();


    }

    private void initData() {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        alertDialog = new SpotsDialog.Builder().setContext(this).setTheme(R.style.CustomProgressBarDialog).build();
    }


    @SuppressLint("CheckResult")
    private void loadDiscount() {
        homeViewModel.getDiscounts()
                .subscribe(
                        response -> {
                            if (response.code() == 200) {
                                discounts = response.body();
                                discountAdapter.setDiscounts(discounts);
                                int duration = discounts.size() * 200;
                                slideDiscount.setSliderAnimationDuration(duration);
                                slideDiscount.setIndicatorAnimationDuration(duration);
                                slideDiscount.setScrollTimeInSec(4);
                                slideDiscount.startAutoCycle();
                            }
                        }
                        , throwable -> {
                            System.out.println("Load discount failed:" + throwable.getLocalizedMessage());
                        }
                );
    }

    @SuppressLint("CheckResult")
    private void loadProducts() {
        homeViewModel.getTop10Products().subscribe(
                response -> {
                    if (response.code() == 200) {
                        System.out.println("size of products:" + response.body().size());
                        productsTop10 = new ArrayList<>(response.body());
                        productAdapter.submitList(response.body());
                        heightScroll = binding.scrollView3.getHeight() + 500;
                        progressBarDialog.dismiss();
                    }
                }
                , throwable -> {
                    System.out.println("throw get products:" + throwable.getMessage());
                }
        );

        homeViewModel.getProducts().subscribe(listResponse -> {
            if (listResponse.code() == 200) {
                products = new ArrayList<>(listResponse.body());
            } else {
                Log.d("Home", "get products");
            }
        }, throwable -> Log.d("Home", throwable.getMessage()));
    }

    private void setEvents() {

        binding.supportBtn.setOnClickListener(view -> {
            LocationDomain location = AppUtils.getLocation(this);
            String url = "https://www.google.com/maps/place/97+Đ.+Man+Thiện,+Hiệp+Phú,+Quận+9,+Thành+phố+Hồ+Chí+Minh,+Vietnam/@10.8466863,106.7775641,16z/data=!4m5!3m4!1s0x317527131ae8b249:0x4d2d3c8fab7d3c2e!8m2!3d10.8473787!4d106.7857602";
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            startActivity(intent);

//            String url = "http://maps.google.com/maps?saddr=10.848082,106.786687&daddr=10.848354,106.774406&mode=driving";
//            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
//            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
//            startActivity(intent);
//            startActivity(new Intent(HomeActivity.this, DirectionActivity.class));
//            startActivity(new Intent(this, MapViewActivity.class));
//            double latitude1 = 10.390991;
//            double longitude1 = -254.268241;
//            double latitude2 = 10.395641;
//            double longitude2 = -254.273787;
//
//            float[] distance = new float[2];
//            Location.distanceBetween(latitude1, longitude1, latitude2, longitude2, distance);
//            System.out.println("distance:" + distance[0]/1000);
//            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            alertDialog.show();
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
        });


        binding.imageUserHomeScreen.setOnClickListener(view -> {
            startActivity(new Intent(this, ProfileActivity.class));
            overridePendingTransition(0, 0);
        });

        binding.settingBtn.setOnClickListener(view -> {
            startActivity(new Intent(this, ProfileActivity.class));
            overridePendingTransition(0, 0);
        });

        binding.cartBtn.setOnClickListener(view -> {
            startActivity(new Intent(this, CartListActivity.class));
            overridePendingTransition(0, 0);

        });
        binding.orderedBtn.setOnClickListener(view -> {
            startActivity(new Intent(this, OrderedListActivity.class));
            overridePendingTransition(0, 0);

        });



    }
    private void setEvents2() {
        binding.editTextSearchHomeScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        binding.editTextSearchHomeScreen.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String strSearch = charSequence.toString().replaceAll(" +", " ");


                binding.scrollView3.scrollTo(0, heightScroll);
                if (charSequence != null && charSequence.length() != 0) {
                    if (products.size() != 0) {
                        productAdapter.submitList(Collections.emptyList());
                        productAdapter.submitList(products.stream()
                                .filter(product -> product.getName().contains(charSequence))
                                .collect(Collectors.toList()));
                    }
                } else {
                    if (countClickSearch != 0)
                        productAdapter.submitList(productsTop10);
                    countClickSearch++;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });

    }

    private void setControls() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang tải");
        progressBarDialog = new SpotsDialog.Builder().setContext(this).setTheme(R.style.CustomProgressBarDialog).build();
        products = new ArrayList<>();
        user = AppUtils.getAccount(getSharedPreferences(AppUtils.ACCOUNT, 0));
        rvCate = binding.recyclerViewCategoriesHomeScreen;
        rvPopular = binding.recyclerViewPopularHomeScreen;
        rvDiscount = binding.recyclerViewDiscountHomeScreen;
        txtName = binding.txtNameUserHomeScreen;
        txtWelcome = binding.textViewWelcomeLabel;
        imgAvt = binding.imageUserHomeScreen;
        slideDiscount = binding.slideDiscountHomeScreen;
        countClickSearch = 0;
        countUpdateAddress = 1;
//        mapViewModel.callGetPlaceFromGeocode(this.location, "vi-VN", getString(R.string.apikey_here_dot_com));
//        getMyLocation();


        // load information user
//        userViewModel.getUser(user.getId());
//        userViewModel.getuserMultable().observe(this, new Observer<User>() {
//            @Override
//            public void onChanged(User user) {
//                loadInfoUser(user);
//            }
//        });
        if(user != null){
            loadInfoUser(AppUtils.getAccount2(this));
        }


        mapViewModel.getClickLocation().observe(this,
                new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {


                    }
                });

        discounts = new ArrayList<>();
        discountAdapter = new DiscountAdapter(discounts, this);
        slideDiscount.setSliderAdapter(discountAdapter);
        slideDiscount.setIndicatorAnimation(IndicatorAnimationType.WORM);
        slideDiscount.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);


        btnSeeAllCategoriesHomeScreen = binding.btnSeeAllCategoriesHomeScreen;
        cartBtn = binding.cartBtn;


        adapterCate = new CategoryAdapter(homeViewModel, new CategoryAdapter.CategoryDiff(), this);
        rvCate.addItemDecoration(
                new ItemMargin(10, 0, 30, 10));
        rvCate.setLayoutManager(new GridLayoutManager(this, 1, LinearLayoutManager.HORIZONTAL, false));
        rvCate.setAdapter(adapterCate);


        productAdapter = new ProductAdapter(homeViewModel, new ProductAdapter.ProductDiff(), R.layout.item_product_vertical);
        rvPopular.addItemDecoration(
                new ItemMargin(10, 0, 30, 10));
        rvPopular.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvPopular.setAdapter(productAdapter);


        rvDiscount.addItemDecoration(
                new ItemMargin(10, 0, 30, 10));
        rvDiscount.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvDiscount.setAdapter(productAdapter);

    }

//    private void getMyLocation() {
//        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
////            Toast.makeText(this, "Location not accept", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 500, this);
//    }

//    @Override
//    public void onLocationChanged(Location location) {
//        mapViewModel.callGetPlaceFromGeocode(location.getLatitude() + "," + location.getLongitude(), "vi-VN", getString(R.string.apikey_here_dot_com));
//    }


//    @Override
//    public void onProviderDisabled(String provider) {
//        Log.d("Latitude", "disable");
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//        Log.d("Latitude", "enable");
//    }
//
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//        Log.d("Latitude", "status");
//    }


    private void loadInfoUser(User user) {
        if (!user.getUsername().equals("") && user.getUsername() != null) {
            txtName.setText("Hi " + user.getUsername());
            txtWelcome.setText(getString(R.string.welcome) +", " + user.getName());
        }


        if (user.getImageUser() != null) {
//            Picasso.get()
//                    .load(user.getImageUser().getLink())
//                    .into(imgAvt);
//
//            RequestOptions options = new RequestOptions()
//                    .centerCrop()
//                    .placeholder(R.drawable.user_icon)
//                    .error(R.drawable.user_icon);

            Glide.with(this).load(user.getImageUser().getLink()).into(imgAvt);
        } else {
            Glide.with(this).load(R.drawable.user_icon).into(imgAvt);

        }
    }

    @SuppressLint("CheckResult")
    private void loadCategories() {

        homeViewModel.getCategories().subscribe(
                response -> {
                    if (response.code() == 200) {
                        System.out.println("size of categories:" + response.body().size());

                        adapterCate.submitList(response.body());
                    }
                }
                , throwable -> {
                    System.out.println("throw get categories:" + throwable.getMessage());
                }
        );

    }

    @Override
    public void showDiscountDetails(String id) {

//        Intent intent = new Intent(this, );
    }

    @Override
    public void showDiscountDetails(Discount discount) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("discount", discount);
        Intent intent = new Intent(this, DiscountDetailsActivity.class);
        intent.putExtra("discount", bundle);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        System.exit(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = AppUtils.getAccount2(this);
        if (user != null && user.getImageUser() != null)
            Glide.with(this).load(user.getImageUser().getLink()).into(binding.imageUserHomeScreen);
    }

//    private final LocationListener mLocationListener = location -> {
//        String url = "http://maps.google.com/maps?saddr="+location.getLatitude()+","+location.getLongitude()+"&daddr=10.848354,106.774406&mode=driving";
//        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
//        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
//        alertDialog.dismiss();
//        startActivity(intent);
//    };
}
