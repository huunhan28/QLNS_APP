package com.example.food.feature.product;

import com.example.food.Domain.Product;
import com.example.food.Domain.Product;
import com.example.food.Domain.ProductReport;
import com.example.food.Domain.Response.ProductReportResponse;

import java.util.List;


import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductRepository {

    @GET("v1/Products/top10")
    Observable<Response<List<Product>>> getTop10Products();

    @GET("v1/Products/category/{category_id}")
    Observable<Response<List<Product>>> getProductsByCategoryId(@Path("category_id") int id);

    @GET("v1/Reports/ProductRevenue/param4?")
    Single<Response<ProductReportResponse>> getProductRevenue(@Query("startDate") String startDate,
                                                              @Query("endDate") String endDate,
                                                              @Query("limit") int limit,
                                                              @Query("offset") int offset);

    @GET("v1/Products")
    Observable<Response<List<Product>>> getProducts();
}
