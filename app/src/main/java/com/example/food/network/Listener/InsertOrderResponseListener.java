package com.example.food.network.Listener;

import com.example.food.Domain.Response.OrderResponse;

public interface InsertOrderResponseListener {
    void didFetch(OrderResponse response, String message);
    void didError(String message);
}
