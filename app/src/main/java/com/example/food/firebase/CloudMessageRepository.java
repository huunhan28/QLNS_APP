package com.example.food.firebase;


import com.example.food.Domain.Response.ResponseObject;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

interface  CloudMessageRepository {
//   https://fcm.googleapis.com
    @Headers({
            "Accept: application/json",
            "User-Agent: Your-App-Name",
            "Cache-Control: max-age=640000"
    })
    @POST("/fcm/send")
    Single<Response<ResponseObject>> sendToCloudMessage(@Header("Authorization") String authorization,
                                                        @Body CloudMessageBody body);
}
