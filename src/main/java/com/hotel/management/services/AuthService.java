package com.hotel.management.services;

import com.hotel.management.models.User;

import java.util.ArrayList;

public class AuthService {

    private final ArrayList<User> users;

    public AuthService(ArrayList<User> users) {
        this.users = users;
    }

    /** Returns the matched User or null if credentials are invalid. */
    public User login(String username, String password) {
        for (User u : users)
            if (u.getUsername().equals(username) && u.getPassword().equals(password))
                return u;
        return null;
    }
}
