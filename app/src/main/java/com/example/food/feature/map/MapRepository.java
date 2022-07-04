package com.example.food.feature.map;

import com.example.food.Domain.Response.DistanceResponse;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface MapRepository {
    @GET("/v1/revgeocode?")
    Single<Response<ResponsePlace>> getPlaceFromGeocode(@Query("at") String at,
                                                        @Query("lang") String lang,
                                                        @Query("apikey") String apikey);
    @GET("/v1/geocode?")
    Single<Response<ResponsePlace>> getGeoCode(@Query("q") String q,
                                                        @Query("apikey") String apikey);

    @GET("directions/v5/mapbox/driving/{lon1},{lat1};{lon2},{lat2}" +
            "?")
    Single<Response<DistanceResponse>> getDistance(

            @Path("lon1") double lon1,
            @Path("lat1") double lat1,

            @Path("lon2") double lon2,
            @Path("lat2") double lat2,
            @Query("access_token") String accessToken
    );
}
