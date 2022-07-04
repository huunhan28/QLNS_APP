package com.example.food.Domain.Response;


import com.example.food.dto.CartDTO;
import com.example.food.dto.DiscountDTO;

public class DiscountResponse {
    private String status;
    private String message;
    private DiscountDTO data;

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

    public DiscountDTO getData() {
        return data;
    }

    public void setData(DiscountDTO data) {
        this.data = data;
    }
}
