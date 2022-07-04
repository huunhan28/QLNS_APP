package com.example.food.network.Listener;


import com.example.food.Domain.Response.OrderDetailResponse;

public interface InsertOrderDetailResponseListener {
    void didFetch(OrderDetailResponse response, String message);
    void didError(String message);
}
