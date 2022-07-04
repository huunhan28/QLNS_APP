package com.example.food.Domain.Response;

import com.example.food.Domain.Order;
import com.example.food.Domain.Product;

public class OrderDetail {
    private Order order;
    private Product product;
    private int quantity;
    private float price;
    private float discount;

    public OrderDetail(Order order, Product product, int quantity, float price, float discount) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }
}
