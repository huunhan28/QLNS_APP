package com.example.food.network.repository;

import com.example.food.Domain.Cart;
import com.example.food.Domain.Response.ResponseObject;
import com.example.food.dto.CartDTO;
import com.example.food.dto.CartForOrderDetail;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CartRepository {

    @POST("api/v1/Carts/minusQuantity")
    Single<Response<ResponseObject<Cart>>> minusQuantity(@Body CartDTO cartDTO);

    @POST("api/v1/Carts/plusQuantity")
    Single<Response<ResponseObject<Cart>>> plusQuantity(@Body CartDTO cartDTO);


    @GET("api/v1/Carts/user/v2/{id}")
    Single<Response<List<CartForOrderDetail>>> getCartsByUserId(@Path("id") int id);

    @POST("api/v1/OrderDetails/insert")
    Single<Response<ResponseObject<CartForOrderDetail>>> insertOrderDetails(@Body CartForOrderDetail cart);


}
