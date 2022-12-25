package com.nmt.minhtu.doan.model;

import java.io.Serializable;

public class SliderHomeFragment implements Serializable {
    private int img;
    private String ten;
    private Double danhGia;

    public SliderHomeFragment() {
    }

    public SliderHomeFragment(int img, String ten, Double danhGia) {
        this.img = img;
        this.ten = ten;
        this.danhGia = danhGia;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public Double getDanhGia() {
        return danhGia;
    }

    public void setDanhGia(Double danhGia) {
        this.danhGia = danhGia;
    }
}
