package com.hotel.management.models;

import java.io.Serializable;

public class LaundryOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String[][] MENU = {
        {"Shirt / Top",           "50"},
        {"Trousers / Jeans",      "70"},
        {"Dress / Saree",         "100"},
        {"Bedsheet / Blanket",    "150"},
        {"Towel",                 "40"},
        {"Jacket / Coat",         "200"}
    };

    private String itemName;
    private double pricePerUnit;
    private int quantity;

    public LaundryOrder(String itemName, double pricePerUnit, int quantity) {
        this.itemName = itemName;
        this.pricePerUnit = pricePerUnit;
        this.quantity = quantity;
    }

    public String getItemName() { return itemName; }
    public double getPricePerUnit() { return pricePerUnit; }
    public int getQuantity() { return quantity; }
    public double getTotal() { return pricePerUnit * quantity; }

    @Override
    public String toString() {
        return String.format("%s x%d = ₹%.0f", itemName, quantity, getTotal());
    }
}
