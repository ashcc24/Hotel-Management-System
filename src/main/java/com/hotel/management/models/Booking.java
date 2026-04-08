package com.hotel.management.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;

    private String bookingId;
    private int roomNumber;
    private int customerId;
    private String customerName;
    private String checkInDate;
    private String checkOutDate;
    private ArrayList<FoodOrder> foodOrders;
    private ArrayList<LaundryOrder> laundryOrders;
    private boolean active;
    private double totalBill;

    public Booking(String bookingId, int roomNumber, int customerId,
                   String customerName, String checkInDate) {
        this.bookingId = bookingId;
        this.roomNumber = roomNumber;
        this.customerId = customerId;
        this.customerName = customerName;
        this.checkInDate = checkInDate;
        this.foodOrders = new ArrayList<>();
        this.laundryOrders = new ArrayList<>();
        this.active = true;
        this.totalBill = 0;
    }

    public String getBookingId()   { return bookingId; }
    public int getRoomNumber()     { return roomNumber; }
    public int getCustomerId()     { return customerId; }
    public String getCustomerName(){ return customerName; }
    public String getCheckInDate() { return checkInDate; }

    public String getCheckOutDate()              { return checkOutDate; }
    public void   setCheckOutDate(String d)      { this.checkOutDate = d; }

    public ArrayList<FoodOrder>    getFoodOrders()    { return foodOrders; }
    public ArrayList<LaundryOrder> getLaundryOrders() { return laundryOrders; }

    public boolean isActive()              { return active; }
    public void    setActive(boolean a)    { this.active = a; }

    public double getTotalBill()           { return totalBill; }
    public void   setTotalBill(double b)   { this.totalBill = b; }

    public void addFoodOrder(FoodOrder o)     { foodOrders.add(o); }
    public void addLaundryOrder(LaundryOrder o){ laundryOrders.add(o); }

    public String getStatus() { return active ? "Active" : "Checked Out"; }

    @Override
    public String toString() {
        return "Room " + roomNumber + " — " + customerName + "  (In: " + checkInDate + ")";
    }
}
