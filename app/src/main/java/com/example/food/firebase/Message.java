package com.example.food.firebase;

public class Message {
    String title;
    String description;
    int orderId;
    int userId;// check only send to this user

    public Message(String title, String description, int orderId, int userId) {
        this.title = title;
        this.description = description;
        this.orderId = orderId;
        this.userId = userId;
    }
}
