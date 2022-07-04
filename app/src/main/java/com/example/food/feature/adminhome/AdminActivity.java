package com.example.food.feature.adminhome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;


import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.food.Adapter.ViewPagerAdapter;
import com.example.food.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;



public class AdminActivity extends AppCompatActivity {

    BottomNavigationView navigationView;
    BottomAppBar bottomAppBar;
    ViewPager2 viewPager2;
    ViewPagerAdapter viewPagerAdapter;
    FloatingActionButton fabCart;
    AppBarLayout appBarLayout;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        setUpViewPager();
        fabCart = findViewById(R.id.fab_cart);
        appBarLayout = findViewById(R.id.app_bar_layout);
        toolbar = findViewById(R.id.tool_bar_admin_main);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.bottom_navigation);
        bottomAppBar = findViewById(R.id.bottom_bar);

        navigationView.getMenu().findItem(R.id.menu_cart_item).setEnabled(false);
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                appBarLayout.setVisibility(View.VISIBLE);
                bottomAppBar.setVisibility(View.VISIBLE);
                fabCart.setVisibility(View.VISIBLE);
                toolbar.getMenu().findItem(R.id.menu_item_profile).setVisible(true);
                toolbar.getMenu().findItem(R.id.menu_item_pdf_admin).setVisible(false);
                switch (item.getItemId()){
                    case R.id.menu_home_item:
                        viewPager2.setCurrentItem(0);
                        break;
                    case R.id.menu_setting_item:
                        toolbar.getMenu().findItem(R.id.menu_item_profile).setVisible(false);

                        viewPager2.setCurrentItem(2);
                        break;
                    case R.id.menu_cart_item:

                        toolbar.getMenu().findItem(R.id.menu_item_pdf_admin).setVisible(false);
                        viewPager2.setCurrentItem(1);
                        break;
                }
                return false;
            }
        });

//        viewPager2.setOffscreenPageLimit(2);// attach number of fragment
//        viewPager2.setPageTransformer(new DepthPageTransformer());
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
//                appBarLayout.setVisibility(View.VISIBLE);
                bottomAppBar.setVisibility(View.VISIBLE);
                fabCart.setVisibility(View.VISIBLE);
                toolbar.getMenu().findItem(R.id.menu_item_profile).setVisible(true);
                toolbar.getMenu().findItem(R.id.menu_item_pdf_admin).setVisible(false);
                switch (position){
                    case 0:
                        navigationView.getMenu().findItem(R.id.menu_home_item).setChecked(true);
                        break;
                    case 1:
                        toolbar.getMenu().findItem(R.id.menu_item_pdf_admin).setVisible(true);
                        navigationView.getMenu().findItem(R.id.menu_cart_item).setChecked(true);
                        break;
                    case 2:
                        toolbar.getMenu().findItem(R.id.menu_item_profile).setVisible(false);
//                        appBarLayout.setVisibility(View.GONE);
                        navigationView.getMenu().findItem(R.id.menu_setting_item).setChecked(true);
                        break;
                }
            }
        });
        fabCart.setOnClickListener(view ->{
            viewPager2.setCurrentItem(1);
            navigationView.getMenu().findItem(R.id.menu_cart_item).setChecked(true);
//            appBarLayout.setVisibility(View.GONE);
        });


    }


    private void setUpViewPager() {
        viewPager2 = findViewById(R.id.view_pager);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        menu.findItem(R.id.menu_item_pdf_admin).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_log_out_admin:
//                AppUtils.deleteAccount2(this);
//                Intent i = new Intent(this, HomeActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(i);
                break;
            case R.id.menu_item_profile:
                toolbar.getMenu().findItem(R.id.menu_item_profile).setVisible(false);
                viewPager2.setCurrentItem(2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        //Checking for fragment count on backstack
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
//            finish();
        } else if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this,"Please click BACK again to exit.", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
            finish();
            return;
        }
    }


}