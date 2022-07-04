package com.example.food.Domain.Response;

import com.example.food.Domain.Order;

public class OrderResponse {
    private String status;
    private String message;
    private Order data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Order getData() {
        return data;
    }

    public void setData(Order data) {
        this.data = data;
    }
}
