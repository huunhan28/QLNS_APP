package com.example.food.dto;


public class CartDTO {
    private int userId;
    private int productId;
    private int quantity;

    public CartDTO(int userId, int productId, int quantity) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }
}
