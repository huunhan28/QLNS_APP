package com.example.food.Domain.Response;
import com.example.food.dto.CartDTO;

public class CartResponse {
    private String status;
    private String message;
    private CartDTO data;

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

    public CartDTO getData() {
        return data;
    }

    public void setData(CartDTO data) {
        this.data = data;
    }
}
