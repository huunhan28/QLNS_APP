package com.example.food.Domain.Response;


public class OTPResponse {
    private String status;
    private String message;
    private String data;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getOtp() {
        return data;
    }
}
