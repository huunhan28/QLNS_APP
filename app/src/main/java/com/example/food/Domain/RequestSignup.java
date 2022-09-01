package com.example.food.Domain;

public class RequestSignup {
    private String username;
    private String phone;
    private String name;
    private String password;
    private String address;

    public RequestSignup() {
    }

    public RequestSignup(String username,String phone, String name, String password, String address) {
        this.username = username;
        this.phone = phone;
        this.name = name;
        this.password = password;
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
