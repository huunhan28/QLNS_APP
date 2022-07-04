package com.example.food.network.repository;

import com.example.food.Domain.Comment;
import com.example.food.Domain.Response.ResponseObject;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CommentRepository {

    @GET("v1/Comment/productId/{productId}")
    Single<Response<List<Comment>>> getCommentsOfProduct(@Path("productId") int productId);

    @POST("v1/Comment/save")
    Single<Response<ResponseObject<Comment>>> saveComment(@Body Comment comment);

    @GET("v1/Comment/orderId/{orderId}")
    Single<Response<ResponseObject<Comment>>> getCommentsOfOrder(@Path("orderId") int orderId);

}
