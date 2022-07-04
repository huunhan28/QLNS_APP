package com.example.food.Domain;


import java.io.Serializable;

public class Image implements Serializable {
    private int id;
//    private String data;
    private String name;
    private String type;
    private String link;

    public Image() {
    }


    public Image(int id, String name, String type, String link) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.link = link;
    }

//    public Image(int id, String data, String name, String type, String link) {
//        this.id = id;
//        this.data = data;
//        this.name = name;
//        this.type = type;
//        this.link = link;
//    }


    public String getLink() {
        if(link==null) return "";
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public String getData() {
//        return data;
//    }
//
//    public void setData(String data) {
//        this.data = data;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
