package com.example.food.network.Listener;


import com.example.food.Domain.Response.ResponseObject;

public interface GetOtpWithEmailResponseListener {
    void didFetch(ResponseObject response, String message);
    void didError(String message);
}
