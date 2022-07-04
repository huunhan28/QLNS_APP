package com.example.food.Domain.Response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ResponseMessage {

    @SerializedName("status")
    @Expose
    protected String status;

    @SerializedName("message")
    @Expose
    protected String message;

    public ResponseMessage(){

    }



    public ResponseMessage(String status, String message) {
        this.status = status;
        this.message = message;
    }

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


}
