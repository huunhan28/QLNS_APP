package com.example.food.Domain;


import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {
    private Long productId;
    private Category category;

    private String name;
    private float price;
    private String calculationUnit;
    private int total;
    private String description;
    private String slug;
    private boolean display;
    private float rate;
    private float discount;
    private int id;
    private String url;
    private int year;

    private Image image;

    private List<Image> images;


    public Product(Long productId, Category category, String name, float price, String calculationUnit, int total, String description, String slug, boolean display, float rate, float discount, int id, String url, int year, Image image, List<Image> images) {
        this.productId = productId;
        this.category = category;
        this.name = name;
        this.price = price;
        this.calculationUnit = calculationUnit;
        this.total = total;
        this.description = description;
        this.slug = slug;
        this.display = display;
        this.rate = rate;
        this.discount = discount;
        this.id = id;
        this.url = url;
        this.year = year;
        this.image = image;
        this.images = images;
    }

    public Image getImage() {
//        System.out.println("image data:" + image.getData());


//        System.out.println(bitmap.toString());
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

//    public Bitmap getImageBitmap()  {
//        byte [] encodeByte = Base64.decode(image.getData(),Base64.DEFAULT);
//        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
//
////        Bitmap bmp = BitmapFactory.decodeByteArray(byteArrray,0, byteArrray.length);
//        return bitmap;
//    }


    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCalculationUnit() {
        return calculationUnit;
    }

    public void setCalculationUnit(String calculationUnit) {
        this.calculationUnit = calculationUnit;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", categoryId=" + category.getId() +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", calculationUnit='" + calculationUnit + '\'' +
                ", total=" + total +
                ", description='" + description + '\'' +
                ", slug='" + slug + '\'' +
                ", display=" + display +
                ", rate=" + rate +
                ", discount=" + discount +
                ", id=" + id +
                ", url='" + url + '\'' +
                ", year=" + year +
                ", image=" + image +
                ", images=" + images +
                '}';
    }
}
