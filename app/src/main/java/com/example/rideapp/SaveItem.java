package com.example.rideapp;

public class SaveItem {
    String itemPrice;
    String itemName;

    public SaveItem(){}

    public SaveItem(String itemPrice, String itemName) {
        this.itemPrice = itemPrice;
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
