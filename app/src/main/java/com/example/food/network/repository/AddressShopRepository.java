package com.example.food.network.repository;


import com.example.food.Domain.AddressShop;
import com.example.food.Domain.Response.ResponseObject;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AddressShopRepository {

    @GET("v1/AddressShop/STT")
    Single<Response<ResponseObject<AddressShop>>> getAddressShopBySTT(@Query("stt") int stt);

    @POST("v1/AddressShop/save")
    Single<Response<ResponseObject<AddressShop>>> update(@Body AddressShop addressShop);
}
