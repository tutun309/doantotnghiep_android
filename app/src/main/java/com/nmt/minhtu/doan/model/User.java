package com.nmt.minhtu.doan.model;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String email = "";
    private String password = "";
    private String name = "";
    private String address = "";
    private String img = "";
    private int vaitroId = 1;
    private String phoneNumber = "";

    public static final int  NGUOI_DUNG = 1;
    public static final int  ADMIN = 0;

    public User() {
    }

    public User(String email, String password, String name, String address, String img, int vaitroId, String phone) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.address = address;
        this.img = img;
        this.vaitroId = vaitroId;
        this.phoneNumber = phone;
    }

    public User(String email, String password, String name, String address, String img, int vaitroId) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.address = address;
        this.img = img;
        this.vaitroId = vaitroId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVaitroId() {
        return vaitroId;
    }

    public void setVaitroId(int vaitroId) {
        this.vaitroId = vaitroId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", img='" + img + '\'' +
                ", vaitroId=" + vaitroId +
                '}';
    }
}
