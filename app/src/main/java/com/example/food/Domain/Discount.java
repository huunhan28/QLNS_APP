package com.example.food.Domain;

import java.io.Serializable;
import java.util.Date;

public class Discount implements Serializable {
    private String id;
    private int quantity;
    private float percent;
    private Date startDate;
    private Date endDate;
    private Image imageDiscount;

    public Discount(String id, int quantity, float percent, Date startDate, Date endDate, Image imageDiscount) {
        this.id = id;
        this.quantity = quantity;
        this.percent = percent;
        this.startDate = startDate;
        this.endDate = endDate;
        this.imageDiscount = imageDiscount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Image getImageDiscount() {
        return imageDiscount;
    }

    public void setImageDiscount(Image imageDiscount) {
        this.imageDiscount = imageDiscount;
    }
}
