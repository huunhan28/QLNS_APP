package com.example.food.network.Listener;

public interface DeleteCartResponseListener {
    void didFetch(String response, String message);
    void didError(String message);
}
