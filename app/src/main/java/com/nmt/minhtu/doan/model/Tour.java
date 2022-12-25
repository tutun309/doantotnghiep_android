package com.nmt.minhtu.doan.model;

import java.io.Serializable;

public class Tour implements Serializable {
    private int id;
    private String name = "";
    private String img = "";
    private float price = 0;
    private String desc  = "";
    private Category Category = new Category();

    public Tour() {
    }

    public Tour(int id) {
        this.id = id;
    }

    public Tour(int id, String name, String img, float price, String desc, Category category) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.price = price;
        this.desc = desc;
        this.Category = category;
    }

    public Tour(String name, String img, float price, String desc, Category category) {
        this.name = name;
        this.img = img;
        this.price = price;
        this.desc = desc;
        this.Category = category;
    }

    public Category getCategory() {
        return Category;
    }

    public void setCategory(Category category) {
        this.Category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Tour{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", img='" + img + '\'' +
                ", price=" + price +
                ", desc='" + desc + '\'' +
                ", Category=" + Category +
                '}';
    }
}
