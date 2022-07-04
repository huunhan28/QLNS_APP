package com.example.food.dto;

import okhttp3.MultipartBody;

public class UserRequestForUpdate {
    private String name;
    private String email;
    private String address;

    public UserRequestForUpdate(String name, String email, String address) {
        this.name = name;
        this.email = email;
        this.address = address;
    }
}
