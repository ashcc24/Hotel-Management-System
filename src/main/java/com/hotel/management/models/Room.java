package com.hotel.management.models;

import java.io.Serializable;

public class Room implements Serializable {
    private static final long serialVersionUID = 2L;

    private int roomNumber;
    private String type; // Single, Double, Deluxe, Suite
    private double price;
    private boolean isAvailable;

    public Room(int roomNumber, String type, double price, boolean isAvailable) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;
        this.isAvailable = isAvailable;
    }

    public int    getRoomNumber()              { return roomNumber; }
    public void   setRoomNumber(int n)         { this.roomNumber = n; }

    public String getType()                    { return type; }
    public void   setType(String t)            { this.type = t; }

    public double getPrice()                   { return price; }
    public void   setPrice(double p)           { this.price = p; }

    public boolean isAvailable()               { return isAvailable; }
    public void    setAvailable(boolean a)     { this.isAvailable = a; }

    public String getAvailabilityStatus()      { return isAvailable ? "✓ Available" : "✗ Occupied"; }

    @Override
    public String toString() {
        return String.format("Room %d — %s  (₹%.0f/night)", roomNumber, type, price);
    }
}
