package com.example.food.network.Listener;

import com.example.food.Domain.Response.DiscountResponse;

public interface DiscountResponseListener {
    void didFetch(DiscountResponse response, String message);
    void didError(String message);
}
