package com.hotel.management.services;

import com.hotel.management.models.*;

import java.io.*;
import java.util.ArrayList;

public class FileHandler {

    private static final String DATA_DIR  = System.getProperty("user.home") + "/hotel_azure_data/";
    private static final String ROOMS     = DATA_DIR + "rooms.dat";
    private static final String CUSTOMERS = DATA_DIR + "customers.dat";
    private static final String BOOKINGS  = DATA_DIR + "bookings.dat";
    private static final String USERS     = DATA_DIR + "users.dat";
    private static final String AUDIT     = DATA_DIR + "audit.dat";
    private static final String REVIEWS   = DATA_DIR + "reviews.dat";

    static {
        new File(DATA_DIR).mkdirs();
    }

    @SuppressWarnings("unchecked")
    private static <T> ArrayList<T> load(String path) {
        File f = new File(path);
        if (!f.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return (ArrayList<T>) ois.readObject();
        } catch (Exception e) {
            System.err.println("FileHandler: could not load " + path + " — " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private static <T> void save(String path, ArrayList<T> list) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(list);
        } catch (IOException e) {
            System.err.println("FileHandler: could not save " + path + " — " + e.getMessage());
        }
    }

    public static void loadAll(HotelService service) {
        service.setRooms(load(ROOMS));
        service.setCustomers(load(CUSTOMERS));
        service.setBookings(load(BOOKINGS));
        service.setUsers(load(USERS));
        service.setAuditLogs(load(AUDIT));
        service.setReviews(load(REVIEWS));
    }

    public static void saveAll(ArrayList<Room> rooms, ArrayList<Customer> customers,
                               ArrayList<Booking> bookings, ArrayList<User> users,
                               ArrayList<AuditLog> auditLogs, ArrayList<Review> reviews) {
        save(ROOMS,     rooms);
        save(CUSTOMERS, customers);
        save(BOOKINGS,  bookings);
        save(USERS,     users);
        save(AUDIT,     auditLogs);
        save(REVIEWS,   reviews);
    }
}
