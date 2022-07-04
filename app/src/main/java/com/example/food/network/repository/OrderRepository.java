package com.example.food.network.repository;

import com.example.food.Domain.Order;
import com.example.food.Domain.Response.ResponseObject;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OrderRepository {
    @GET("v1/Orders/param/state?")
    Single<Response<ResponseObject<List<Order>>>> getOrdersByState(@Query("state") String state);

    @GET("v1/Orders/param/state/date?")
    Single<Response<ResponseObject<List<Order>>>> getOrdersByStateAndCreateAtBetween(@Query("state") String state,
                                                            @Query("startDate") String startDate,
                                                            @Query("endDate") String endDate);

    @GET("v1/Orders/{id}")
    Single<Response<ResponseObject<Order>>> getOrderById(@Path("id") int id);

    @PUT("v1/Orders/updateState/{id}")
    Single<Response<ResponseObject<Order>>> updateStateOrder(@Path("id") int id,
                                                             @Query("state") String state);

    @POST("v1/Orders/{id}/comment/insert?")
    Single<Response<ResponseObject<Order>>> insertCommentOfOrder(@Path("id") int id,
                                                             @Query("userId") int userId,
                                                             @Query("rate") int rate,
                                                             @Query("comment") String comment);
}
