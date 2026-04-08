package com.hotel.management.models;

import java.io.Serializable;

public class Customer implements Serializable {
    private static final long serialVersionUID = 2L;

    private int customerId;
    private String name;
    private String phone;
    private String email;

    public Customer(int customerId, String name, String phone, String email) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public int    getCustomerId()           { return customerId; }
    public void   setCustomerId(int id)     { this.customerId = id; }

    public String getName()                 { return name; }
    public void   setName(String name)      { this.name = name; }

    public String getPhone()                { return phone; }
    public void   setPhone(String phone)    { this.phone = phone; }

    public String getEmail()                { return email; }
    public void   setEmail(String email)    { this.email = email; }

    @Override
    public String toString() {
        return name + "  (ID: " + customerId + ")";
    }
}
