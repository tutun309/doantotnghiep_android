package com.nmt.minhtu.doan.model;

import java.io.Serializable;
import java.util.Date;

public class Booking implements Serializable {
    private int id;
    private User user;
    private Tour tour;
    private Date date;
    private String status;

    public Booking() {
    }

    public Booking(User user, Tour tour, Date date, String status) {
        this.user = user;
        this.tour = tour;
        this.date = date;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", user=" + user +
                ", tour=" + tour +
                ", date=" + date +
                ", status='" + status + '\'' +
                '}';
    }
}
