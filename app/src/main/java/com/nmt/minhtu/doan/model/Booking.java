package com.nmt.minhtu.doan.model;

import java.io.Serializable;
import java.util.Date;

public class Booking implements Serializable {
    private int id;
    private User user;
    private Tour tour;
    private Date date;
    private String status;

    private String timeStart;
    private String timeBooking;
    private int statusId;
    private int priceTotal;
    private TicketBooking ticketBooking;

    public Booking() {
    }

    public Booking(User user, Tour tour, String timeStart, String timeBooking, int statusId, int priceTotal, TicketBooking ticketBooking) {
        this.user = user;
        this.tour = tour;
        this.timeStart = timeStart;
        this.timeBooking = timeBooking;
        this.statusId = statusId;
        this.priceTotal = priceTotal;
        this.ticketBooking = ticketBooking;
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


    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeBooking() {
        return timeBooking;
    }

    public void setTimeBooking(String timeBooking) {
        this.timeBooking = timeBooking;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(int priceTotal) {
        this.priceTotal = priceTotal;
    }

    public TicketBooking getTicketBooking() {
        return ticketBooking;
    }

    public void setTicketBooking(TicketBooking ticketBooking) {
        this.ticketBooking = ticketBooking;
    }

    public String getBookingStatus() {
        switch (statusId) {
            case 1:
                return "Chờ thanh toán";
            case 2:
                return "Đã thanh toán";
            case 3:
                return "Hoàn thành";
            case 4:
                return "Yêu cầu hủy";
            case 5:
                return "Đã hủy";
            default:
                return "";
        }
    }
}
