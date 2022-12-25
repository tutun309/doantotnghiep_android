package com.nmt.minhtu.doan.model;

import java.io.Serializable;

public class BookedTour implements Serializable {
    private int img;
    private String name;
    private double price;
    private String status;

    public BookedTour() {
    }

    public BookedTour(int img, String name, double price, String status) {
        this.img = img;
        this.name = name;
        this.price = price;
        this.status = status;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
