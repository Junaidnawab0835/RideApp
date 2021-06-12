package com.example.rideapp.Fragment;

public class Item_Model {
    String name;
    String price;
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Item_Model() {
    }

    public Item_Model(String name, String price, String id) {
        this.name = name;
        this.price = price;
        this.id = id;
    }

    public Item_Model(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
