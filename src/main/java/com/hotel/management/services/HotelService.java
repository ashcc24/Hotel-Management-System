package com.hotel.management.services;

import com.hotel.management.models.*;

import java.util.ArrayList;

public class HotelService {

    private ArrayList<Room>      rooms;
    private ArrayList<Customer>  customers;
    private ArrayList<Booking>   bookings;
    private ArrayList<User>      users;
    private ArrayList<AuditLog>  auditLogs;
    private ArrayList<Review>    reviews;

    public HotelService() {
        rooms     = new ArrayList<>();
        customers = new ArrayList<>();
        bookings  = new ArrayList<>();
        users     = new ArrayList<>();
        auditLogs = new ArrayList<>();
        reviews   = new ArrayList<>();
        FileHandler.loadAll(this);
        if (users.isEmpty()) {
            users.add(new User("admin", "admin123", User.Role.ADMIN, "Administrator"));
            saveAll();
        }
    }

    public void saveAll() {
        FileHandler.saveAll(rooms, customers, bookings, users, auditLogs, reviews);
    }

    // ── Getters / Setters (used by FileHandler) ──────────────────────────────
    public ArrayList<Room>     getRooms()       { return rooms; }
    public void setRooms(ArrayList<Room> r)     { rooms = r; }

    public ArrayList<Customer> getCustomers()   { return customers; }
    public void setCustomers(ArrayList<Customer> c) { customers = c; }

    public ArrayList<Booking>  getBookings()    { return bookings; }
    public void setBookings(ArrayList<Booking> b)   { bookings = b; }

    public ArrayList<User>     getUsers()       { return users; }
    public void setUsers(ArrayList<User> u)     { users = u; }

    public ArrayList<AuditLog> getAuditLogs()   { return auditLogs; }
    public void setAuditLogs(ArrayList<AuditLog> a) { auditLogs = a; }

    public ArrayList<Review>   getReviews()     { return reviews; }
    public void setReviews(ArrayList<Review> r) { reviews = r; }

    // ── Room Operations ───────────────────────────────────────────────────────
    public void addRoom(Room room, String by) {
        for (Room r : rooms)
            if (r.getRoomNumber() == room.getRoomNumber())
                throw new IllegalArgumentException("Room " + room.getRoomNumber() + " already exists!");
        rooms.add(room);
        log(by, "ADD ROOM", "Room " + room.getRoomNumber() + " (" + room.getType() +
                ") added at ₹" + room.getPrice() + "/night");
        saveAll();
    }

    public void removeRoom(int roomNumber, String by) {
        for (Booking b : bookings)
            if (b.getRoomNumber() == roomNumber && b.isActive())
                throw new IllegalArgumentException("Room " + roomNumber + " is currently occupied!");
        if (!rooms.removeIf(r -> r.getRoomNumber() == roomNumber))
            throw new IllegalArgumentException("Room " + roomNumber + " not found!");
        log(by, "REMOVE ROOM", "Room " + roomNumber + " removed from system");
        saveAll();
    }

    public Room findRoom(int roomNumber) {
        for (Room r : rooms) if (r.getRoomNumber() == roomNumber) return r;
        return null;
    }

    public ArrayList<Room> getAvailableRooms() {
        ArrayList<Room> list = new ArrayList<>();
        for (Room r : rooms) if (r.isAvailable()) list.add(r);
        return list;
    }

    // ── Customer Operations ───────────────────────────────────────────────────
    public void addCustomer(Customer customer, String by) {
        for (Customer c : customers)
            if (c.getCustomerId() == customer.getCustomerId())
                throw new IllegalArgumentException("Customer ID " + customer.getCustomerId() + " already exists!");
        customers.add(customer);
        log(by, "ADD CUSTOMER", "Customer " + customer.getName() + " (ID: " + customer.getCustomerId() + ") added");
        saveAll();
    }

    public void removeCustomer(int id, String by) {
        for (Booking b : bookings)
            if (b.getCustomerId() == id && b.isActive())
                throw new IllegalArgumentException("Customer has an active booking — checkout first!");
        if (!customers.removeIf(c -> c.getCustomerId() == id))
            throw new IllegalArgumentException("Customer not found!");
        log(by, "REMOVE CUSTOMER", "Customer ID " + id + " removed");
        saveAll();
    }

    public Customer findCustomer(int id) {
        for (Customer c : customers) if (c.getCustomerId() == id) return c;
        return null;
    }

    public int getNextCustomerId() {
        int max = 0;
        for (Customer c : customers) if (c.getCustomerId() > max) max = c.getCustomerId();
        return max + 1;
    }

    // ── Booking Operations ────────────────────────────────────────────────────
    public Booking bookRoom(int roomNumber, int customerId, String checkInDate, String by) {
        Room room = findRoom(roomNumber);
        if (room == null)         throw new IllegalArgumentException("Room " + roomNumber + " not found!");
        if (!room.isAvailable())  throw new IllegalArgumentException("Room " + roomNumber + " is not available!");

        Customer customer = findCustomer(customerId);
        if (customer == null)     throw new IllegalArgumentException("Customer not found!");

        for (Booking b : bookings)
            if (b.getCustomerId() == customerId && b.isActive())
                throw new IllegalArgumentException(customer.getName() + " already has an active booking!");

        String bookingId = "BK" + System.currentTimeMillis();
        Booking booking  = new Booking(bookingId, roomNumber, customerId, customer.getName(), checkInDate);
        room.setAvailable(false);
        bookings.add(booking);
        log(by, "BOOK ROOM", String.format("Room %d booked for %s (Check-in: %s)", roomNumber, customer.getName(), checkInDate));
        saveAll();
        return booking;
    }

    public ArrayList<Booking> getActiveBookings() {
        ArrayList<Booking> list = new ArrayList<>();
        for (Booking b : bookings) if (b.isActive()) list.add(b);
        return list;
    }

    public Booking findBookingById(String id) {
        for (Booking b : bookings) if (b.getBookingId().equals(id)) return b;
        return null;
    }

    // ── Food & Laundry ────────────────────────────────────────────────────────
    public void addFoodOrder(String bookingId, FoodOrder order, String by) {
        Booking b = getActiveBookingOrThrow(bookingId);
        b.addFoodOrder(order);
        log(by, "FOOD ORDER", "Room " + b.getRoomNumber() + ": " + order.getItemName() + " x" + order.getQuantity());
        saveAll();
    }

    public void addLaundryOrder(String bookingId, LaundryOrder order, String by) {
        Booking b = getActiveBookingOrThrow(bookingId);
        b.addLaundryOrder(order);
        log(by, "LAUNDRY ORDER", "Room " + b.getRoomNumber() + ": " + order.getItemName() + " x" + order.getQuantity());
        saveAll();
    }

    // ── Checkout ──────────────────────────────────────────────────────────────
    public void checkout(String bookingId, String checkOutDate, double totalBill, String by) {
        Booking booking = getActiveBookingOrThrow(bookingId);
        booking.setCheckOutDate(checkOutDate);
        booking.setActive(false);
        booking.setTotalBill(totalBill);
        Room room = findRoom(booking.getRoomNumber());
        if (room != null) room.setAvailable(true);
        log(by, "CHECKOUT", String.format("Room %d — %s checked out. Bill: ₹%.2f",
                booking.getRoomNumber(), booking.getCustomerName(), totalBill));
        saveAll();
    }

    // ── User / Staff Management ───────────────────────────────────────────────
    public void addUser(User user, String by) {
        for (User u : users)
            if (u.getUsername().equals(user.getUsername()))
                throw new IllegalArgumentException("Username '" + user.getUsername() + "' already exists!");
        users.add(user);
        log(by, "ADD STAFF", "New " + user.getRole() + " user '" + user.getUsername() + "' created");
        saveAll();
    }

    public void removeUser(String username, String by) {
        if ("admin".equals(username))
            throw new IllegalArgumentException("Cannot remove the default admin account!");
        if (!users.removeIf(u -> u.getUsername().equals(username)))
            throw new IllegalArgumentException("User not found!");
        log(by, "REMOVE STAFF", "User '" + username + "' removed");
        saveAll();
    }

    // ── Audit ─────────────────────────────────────────────────────────────────
    public void log(String by, String action, String details) {
        auditLogs.add(new AuditLog(by, action, details));
    }

    // ── Private Helpers ───────────────────────────────────────────────────────
    private Booking getActiveBookingOrThrow(String bookingId) {
        Booking b = findBookingById(bookingId);
        if (b == null)      throw new IllegalArgumentException("Booking not found!");
        if (!b.isActive())  throw new IllegalArgumentException("This booking is no longer active!");
        return b;
    }
}
