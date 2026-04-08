package com.hotel.management.models;

import java.io.Serializable;

public class Review implements Serializable {
    private static final long serialVersionUID = 1L;

    private int customerId;
    private String customerName;
    private int rating; // e.g. 1 to 5
    private String comments;

    public Review(int customerId, String customerName, int rating, String comments) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.rating = rating;
        this.comments = comments;
    }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }

    @Override
    public String toString() {
        return customerName + " [" + rating + "/5]: " + comments;
    }
}
