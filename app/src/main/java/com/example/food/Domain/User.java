package com.example.food.Domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class User implements Serializable {

    private String token;
    private String type;

    @SerializedName("id") // anhs xa
    @Expose
    private int id;


    @Expose
    private String name;

    @Expose
    private String email;


    @Expose
    private String address;

    private String rememberToken;
    private Date createdAt;


    private Date updatedAt;


    private String username;


    private String password;


    private Set<Role> roles = new HashSet<>();

    private Image imageUser;

    private String tokenFireBase;


    public User() {
    }

    public User(String token, String type, int id, String name
            , String email,String address
            , String rememberToken, Date createdAt, Date updatedAt
            , String username, String password, Set<Role> roles,
                Image image, String tokenFireBase) {
        this.token = token;
        this.type = type;
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.rememberToken = rememberToken;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.imageUser = image;
        this.tokenFireBase = tokenFireBase;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getTokenFireBase() {
        return tokenFireBase;
    }

    public void setTokenFireBase(String tokenFireBase) {
        this.tokenFireBase = tokenFireBase;
    }

    public Image getImageUser() {
        return imageUser;
    }

    public void setImageUser(Image imageUser) {
        this.imageUser = imageUser;
    }

    public User(String username, String name, String password) {
        this.name = name;
    }

    public User(int id,String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRememberToken() {
        return rememberToken;
    }

    public void setRememberToken(String rememberToken) {
        this.rememberToken = rememberToken;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "token='" + token + '\'' +
                ", type='" + type + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", rememberToken='" + rememberToken + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }
}
