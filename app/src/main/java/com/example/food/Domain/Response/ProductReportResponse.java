package com.example.food.Domain.Response;

import com.example.food.Domain.ProductReport;

import java.util.List;

public class ProductReportResponse {
    String status;
    String message;
    List<ProductReport> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ProductReport> getDate() {
        return data;
    }

    public void setDate(List<ProductReport> date) {
        this.data = date;
    }
}
