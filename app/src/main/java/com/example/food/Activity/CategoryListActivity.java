package com.example.food.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.example.food.Api.Api;
import com.example.food.Domain.Category;
import com.example.food.network.Listener.CategoryResponseListener;
import com.example.food.R;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;

public class CategoryListActivity extends AppCompatActivity {
    RecyclerView recyclerViewCategory;
    Api api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setControl();

        api = new Api(CategoryListActivity.this);
        api.getCategories(categoryResponseListener);
    }

    private void setControl() {
        recyclerViewCategory= findViewById(R.id.recyclerViewCategory);
    }

    private final CategoryResponseListener categoryResponseListener = new CategoryResponseListener() {
        @Override
        public void didFetch(ArrayList<Category> response, String message) {
            //gridView.setAdapter(new CustomCategoryGridAdapter(CategoryListActivity.this, response));
//            CategoryAdapter adapter=new CategoryAdapter();
//            recyclerViewCategory.setLayoutManager(new GridLayoutManager(CategoryListActivity.this, 2, LinearLayoutManager.HORIZONTAL, false));
//            recyclerViewCategory.setAdapter(adapter);
        }

        @Override
        public void didError(String message) {
            Log.d("api", message);
            Toast.makeText(CategoryListActivity.this, message, Toast.LENGTH_LONG).show();
        }
    };
}