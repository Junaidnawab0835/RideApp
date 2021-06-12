package com.example.rideapp;

public class Model {

    String title,image_url,price;

    public Model(String title, String image_url, String price) {
        this.title = title;
        this.image_url = image_url;
        this.price = price;
    }

    //constructor
    public Model(){}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
