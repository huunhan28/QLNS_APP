package com.example.food.network.repository;

import com.example.food.Domain.Response.ResponseObject;
import com.example.food.Domain.request.RequestChangePassword;
import com.example.food.dto.UserDTO;
import com.example.food.Domain.RequestSignup;
import com.example.food.Domain.User;
import com.example.food.Domain.Response.ResponseMessage;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserRepository {



    @GET
    Observable<Response<User>> getUserByUserName(@Path("username") String username);

    @Multipart
    @PUT("v1/Users/update/{id}")
    Observable<Response<ResponseObject<User>>> updateUser(
            @Path("id") int id,
            @Part("user") RequestBody userRequestForUpdate,
            @Part MultipartBody.Part file
            );
    @Multipart
    @PUT("v1/Users/updateNotImage/{id}")
    Observable<Response<ResponseObject<User>>> updateUserNotImage(
            @Path("id") int id,
            @Part("user") RequestBody userRequestForUpdate
    );

    @POST("auth/signin")
    Observable<Response<UserDTO>> signin(@Body User user);

    @POST("auth/signup")
    Observable<Response<UserDTO>> signup(@Body RequestSignup user);


    @GET("auth/Users")
    Observable<ResponseMessage> getUsers();

    @GET("v1/Users/id/{id}")
    Single<Response<UserDTO>> getUserById(@Path("id") int id);

    @POST("auth/resetPassword?")
    Single<Response<ResponseObject<User>>> changePassword(@Body RequestChangePassword request,
                                                          @Query("state") boolean state);

    @GET("v1/Users/{phonenumber}")
    Single<Response<ResponseObject<UserDTO>>> checkUserByPhonenumber(@Path("phonenumber") String phonenumber);

    @GET("v1/Users/sendSMS?")
    Single<Response<ResponseObject<String>>> sendSMS(@Query("phone") String phone);

    @Multipart
    @PUT("v1/Users/updateTokenFireBase/{id}")
    Single<Response<ResponseObject<User>>> updateTokenFireBaseOfUser(@Path("id") int id,
                                                                     @Part("token") RequestBody token);

}
