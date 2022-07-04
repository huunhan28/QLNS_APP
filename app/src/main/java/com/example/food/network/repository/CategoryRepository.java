package com.example.food.network.repository;

import com.example.food.Domain.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryRepository {

    @GET("v1/Categories")
    Call<List<Category>> getCategoryDomains();
}
