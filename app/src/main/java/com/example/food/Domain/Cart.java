package com.example.food.Domain;

import java.io.Serializable;

public class Cart implements Serializable {
    private User user;
    private Product product;
    private int quantity;

    public Cart(User user, Product product, int quantity) {
        this.user = user;
        this.product = product;
        this.quantity = quantity;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProductDomain() {
        return product;
    }

    public void setProductDomain(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "user=" + user +
                ", product=" + product +
                ", quantity=" + quantity +
                '}';
    }



}
