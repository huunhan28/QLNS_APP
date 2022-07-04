package com.example.food.dto;

import java.util.Date;

public class OrdersDTO {
    private long userId;
    private String createAt;
    private String discountId;
    private String state;

    public OrdersDTO(long userId, String createAt, String discountId, String state) {
        this.userId = userId;
        this.createAt = createAt;
        this.discountId = discountId;
        this.state = state;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getDiscountId() {
        return discountId;
    }

    public void setDiscountId(String discountId) {
        this.discountId = discountId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
