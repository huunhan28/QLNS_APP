package com.example.food.Domain.Response;


import com.example.food.Domain.Product;

public class ProductResponse {
    private String status;
    private String message;
    private Product data;

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

    public Product getData() {
        return data;
    }

    public void setData(Product data) {
        this.data = data;
    }
}
