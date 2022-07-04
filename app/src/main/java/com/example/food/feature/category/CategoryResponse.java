package com.example.food.feature.category;


import com.example.food.Domain.Category;
import com.example.food.Domain.Response.ResponseMessage;

public class CategoryResponse extends ResponseMessage {
    private Category data;

    public CategoryResponse() {
    }

    public CategoryResponse(String status, String message, Category data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

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

    public Category getData() {
        return data;
    }

    public void setData(Category data) {
        this.data = data;
    }
}
