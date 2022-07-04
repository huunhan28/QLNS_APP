package com.example.food.network.Listener;

import com.example.food.Domain.Product;

import java.util.ArrayList;

public interface ProductResponseListener {
    void didFetch(ArrayList<Product> response, String message);
    void didError(String message);
}
