package com.example.food.feature.discount;


import com.example.food.Domain.Discount;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;

public interface DiscountRepository {

    @GET("v1/Discounts")
    Observable<Response<List<Discount>>> getDiscounts();

}
