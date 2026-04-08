package com.hotel.management.models;

import java.io.Serializable;

public class FoodOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String[][] MENU = {
        {"Continental Breakfast", "150"},
        {"Club Sandwich",         "200"},
        {"Pasta Arrabiata",       "250"},
        {"Veggie Burger",         "180"},
        {"Iced Coffee",           "80"},
        {"Fresh Juice",           "120"},
        {"Dinner Buffet",         "400"},
        {"Chocolate Dessert",     "160"}
    };

    private String itemName;
    private double pricePerUnit;
    private int quantity;

    public FoodOrder(String itemName, double pricePerUnit, int quantity) {
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
