package com.example.food.firebase;

import com.example.food.dto.OrdersDTO;

public class CloudMessageBody {
    Message data;
    String to;

    public CloudMessageBody(Message data, String to) {
        this.data = data;
        this.to = to;
    }
}
