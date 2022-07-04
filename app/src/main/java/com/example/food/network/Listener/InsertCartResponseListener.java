package com.example.food.network.Listener;

import com.example.food.Domain.Response.CartResponse;

public interface InsertCartResponseListener {
    void didFetch(CartResponse response, String message);
    void didError(String message);
}
