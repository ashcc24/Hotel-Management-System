package com.hotel.management.controllers;

import com.hotel.management.models.*;
import com.hotel.management.services.BillingService;
import com.hotel.management.services.CheckoutService;
import com.hotel.management.services.HotelService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DashboardController {

    // ── Header ────────────────────────────────────────────────────────────────
    @FXML private Label headerUserLabel;
    @FXML private Label headerRoleBadge;

    // ── Sidebar nav buttons ───────────────────────────────────────────────────
    @FXML private Button btnNavDashboard;
    @FXML private Button btnNavRooms;
    @FXML private Button btnNavCustomers;
    @FXML private Button btnNavBookings;
    @FXML private Button btnNavFood;
    @FXML private Button btnNavLaundry;
    @FXML private Button btnNavCheckout;
    @FXML private Button btnNavAudit;
    @FXML private Button btnNavStaff;

    // ── Content panes ─────────────────────────────────────────────────────────
    @FXML private VBox paneDashboard;
    @FXML private VBox paneRooms;
    @FXML private VBox paneCustomers;
    @FXML private VBox paneBookings;
    @FXML private VBox paneFood;
    @FXML private VBox paneLaundry;
    @FXML private VBox paneCheckout;
    @FXML private VBox paneAudit;
    @FXML private VBox paneStaff;

    // ── Dashboard ─────────────────────────────────────────────────────────────
    @FXML private Label     statTotalRooms;
    @FXML private Label     statAvailRooms;
    @FXML private Label     statOccupied;
    @FXML private Label     statCustomers;
    @FXML private Label     statBookings;
    @FXML private ListView<String> recentActivityList;

    // ── Rooms ─────────────────────────────────────────────────────────────────
    @FXML private TextField             roomNumInput;
    @FXML private ComboBox<String>      roomTypeCombo;
    @FXML private TextField             roomPriceInput;
    @FXML private ComboBox<Room>        removeRoomCombo;
    @FXML private TableView<Room>       roomTable;
    @FXML private TableColumn<Room, Integer> roomColNum;
    @FXML private TableColumn<Room, String>  roomColType;
    @FXML private TableColumn<Room, Double>  roomColPrice;
    @FXML private TableColumn<Room, String>  roomColAvail;

    // ── Customers ─────────────────────────────────────────────────────────────
    @FXML private TextField                  custNameInput;
    @FXML private TextField                  custPhoneInput;
    @FXML private TextField                  custEmailInput;
    @FXML private ComboBox<Customer>         removeCustomerCombo;
    @FXML private TableView<Customer>        customerTable;
    @FXML private TableColumn<Customer, Integer> custColId;
    @FXML private TableColumn<Customer, String>  custColName;
    @FXML private TableColumn<Customer, String>  custColPhone;
    @FXML private TableColumn<Customer, String>  custColEmail;

    // ── Bookings ──────────────────────────────────────────────────────────────
    @FXML private ComboBox<Customer>         bookCustCombo;
    @FXML private ComboBox<Room>             bookRoomCombo;
    @FXML private DatePicker                 checkInPicker;
    @FXML private TableView<Booking>         bookingTable;
    @FXML private TableColumn<Booking, String> bkColId;
    @FXML private TableColumn<Booking, Integer> bkColRoom;
    @FXML private TableColumn<Booking, String> bkColGuest;
    @FXML private TableColumn<Booking, String> bkColCheckIn;
    @FXML private TableColumn<Booking, String> bkColStatus;

    // ── Food ──────────────────────────────────────────────────────────────────
    @FXML private ComboBox<Booking>          foodBookingCombo;
    @FXML private ComboBox<String>           foodItemCombo;
    @FXML private Spinner<Integer>           foodQtySpinner;
    @FXML private ListView<String>           foodOrderList;
    @FXML private Label                      foodTotalLabel;

    // ── Laundry ───────────────────────────────────────────────────────────────
    @FXML private ComboBox<Booking>          laundryBookingCombo;
    @FXML private ComboBox<String>           laundryItemCombo;
    @FXML private Spinner<Integer>           laundryQtySpinner;
    @FXML private ListView<String>           laundryOrderList;
    @FXML private Label                      laundryTotalLabel;

    // ── Checkout ──────────────────────────────────────────────────────────────
    @FXML private ComboBox<Booking>          checkoutBookingCombo;
    @FXML private DatePicker                 checkOutPicker;
    @FXML private TextArea                   checkoutStepsArea;
    @FXML private TextArea                   billDisplayArea;
    @FXML private Button                     checkoutBtn;

    // ── Audit ─────────────────────────────────────────────────────────────────
    @FXML private TableView<AuditLog>        auditTable;
    @FXML private TableColumn<AuditLog, String> auditColTime;
    @FXML private TableColumn<AuditLog, String> auditColUser;
    @FXML private TableColumn<AuditLog, String> auditColAction;
    @FXML private TableColumn<AuditLog, String> auditColDetail;

    // ── Staff ─────────────────────────────────────────────────────────────────
    @FXML private TextField                  staffNameInput;
    @FXML private TextField                  staffUserInput;
    @FXML private PasswordField              staffPassInput;
    @FXML private ComboBox<String>           staffRoleCombo;
    @FXML private ComboBox<User>             removeStaffCombo;
    @FXML private TableView<User>            staffTable;
    @FXML private TableColumn<User, String>  staffColName;
    @FXML private TableColumn<User, String>  staffColUsername;
    @FXML private TableColumn<User, String>  staffColRole;

    // ── State ─────────────────────────────────────────────────────────────────
    private HotelService  hotelService;
    private BillingService billingService;
    private User          currentUser;
    private Button        activeNavBtn;

    private ObservableList<Room>     roomOList;
    private ObservableList<Customer> custOList;
    private ObservableList<Booking>  bookOList;
    private ObservableList<AuditLog> auditOList;
    private ObservableList<User>     userOList;

    // ── Entry point called by LoginController ──────────────────────────────────
    public void initWithUser(User user, HotelService service) {
        this.currentUser   = user;
        this.hotelService  = service;
        this.billingService = new BillingService();

        // Header
        headerUserLabel.setText("Welcome, " + user.getDisplayName());
        if (user.getRole() == User.Role.ADMIN) {
            headerRoleBadge.setText("ADMIN");
            headerRoleBadge.getStyleClass().setAll("header-role-badge-admin");
        } else {
            headerRoleBadge.setText("STAFF");
        }

        // Hide admin-only nav items for staff
        if (user.getRole() != User.Role.ADMIN) {
            btnNavRooms.setVisible(false);
            btnNavRooms.setManaged(false);
            btnNavStaff.setVisible(false);
            btnNavStaff.setManaged(false);
        }

        setupTables();
        setupCombos();
        refreshAllData();
        showPane(paneDashboard, btnNavDashboard);
    }

    // ── Table Setup ───────────────────────────────────────────────────────────
    private void setupTables() {
        // Rooms
        roomColNum.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        roomColType.setCellValueFactory(new PropertyValueFactory<>("type"));
        roomColPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        roomColAvail.setCellValueFactory(new PropertyValueFactory<>("availabilityStatus"));
        roomOList = FXCollections.observableArrayList();
        roomTable.setItems(roomOList);

        // Customers
        custColId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        custColName.setCellValueFactory(new PropertyValueFactory<>("name"));
        custColPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        custColEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        custOList = FXCollections.observableArrayList();
        customerTable.setItems(custOList);

        // Bookings
        bkColId.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        bkColRoom.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        bkColGuest.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        bkColCheckIn.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));
        bkColStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        bookOList = FXCollections.observableArrayList();
        bookingTable.setItems(bookOList);

        // Audit
        auditColTime.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        auditColUser.setCellValueFactory(new PropertyValueFactory<>("performedBy"));
        auditColAction.setCellValueFactory(new PropertyValueFactory<>("action"));
        auditColDetail.setCellValueFactory(new PropertyValueFactory<>("details"));
        auditOList = FXCollections.observableArrayList();
        auditTable.setItems(auditOList);

        // Staff
        staffColName.setCellValueFactory(new PropertyValueFactory<>("displayName"));
        staffColUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        staffColRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        userOList = FXCollections.observableArrayList();
        staffTable.setItems(userOList);
    }

    private void setupCombos() {
        roomTypeCombo.setItems(FXCollections.observableArrayList("Single", "Double", "Deluxe", "Suite"));

        // Food menu
        ObservableList<String> foodItems = FXCollections.observableArrayList();
        for (String[] item : FoodOrder.MENU)
            foodItems.add(item[0] + "  —  ₹" + item[1]);
        foodItemCombo.setItems(foodItems);

        // Laundry menu
        ObservableList<String> laundryItems = FXCollections.observableArrayList();
        for (String[] item : LaundryOrder.MENU)
            laundryItems.add(item[0] + "  —  ₹" + item[1]);
        laundryItemCombo.setItems(laundryItems);

        staffRoleCombo.setItems(FXCollections.observableArrayList("STAFF", "ADMIN"));

        checkInPicker.setValue(LocalDate.now());
        checkOutPicker.setValue(LocalDate.now());

        // Refresh food/laundry orders on booking combo change
        foodBookingCombo.setOnAction(e -> refreshFoodList());
        laundryBookingCombo.setOnAction(e -> refreshLaundryList());
    }

    // ── Data Refresh ──────────────────────────────────────────────────────────
    private void refreshAllData() {
        // Tables
        roomOList.setAll(hotelService.getRooms());
        custOList.setAll(hotelService.getCustomers());
        bookOList.setAll(hotelService.getActiveBookings());
        auditOList.setAll(hotelService.getAuditLogs());
        userOList.setAll(hotelService.getUsers());

        // Remove combo boxes
        removeRoomCombo.setItems(FXCollections.observableArrayList(hotelService.getRooms()));
        removeCustomerCombo.setItems(FXCollections.observableArrayList(hotelService.getCustomers()));
        removeStaffCombo.setItems(FXCollections.observableArrayList(hotelService.getUsers()));

        // Booking combos
        ObservableList<Booking> active = FXCollections.observableArrayList(hotelService.getActiveBookings());
        bookCustCombo.setItems(FXCollections.observableArrayList(hotelService.getCustomers()));
        bookRoomCombo.setItems(FXCollections.observableArrayList(hotelService.getAvailableRooms()));
        foodBookingCombo.setItems(active);
        laundryBookingCombo.setItems(active);
        checkoutBookingCombo.setItems(active);

        // Dashboard stats
        List<Room> rooms = hotelService.getRooms();
        long avail = rooms.stream().filter(Room::isAvailable).count();
        statTotalRooms.setText(String.valueOf(rooms.size()));
        statAvailRooms.setText(String.valueOf(avail));
        statOccupied.setText(String.valueOf(rooms.size() - avail));
        statCustomers.setText(String.valueOf(hotelService.getCustomers().size()));
        statBookings.setText(String.valueOf(hotelService.getActiveBookings().size()));

        // Recent activity (last 10 audit entries reversed)
        ArrayList<AuditLog> logs = hotelService.getAuditLogs();
        ObservableList<String> recent = FXCollections.observableArrayList();
        for (int i = logs.size() - 1; i >= Math.max(0, logs.size() - 10); i--) {
            AuditLog l = logs.get(i);
            recent.add("[" + l.getTimestamp() + "]  " + l.getAction() + "  •  " + l.getDetails());
        }
        recentActivityList.setItems(recent);
    }

    // ── Navigation ────────────────────────────────────────────────────────────
    private VBox[] allPanes() {
        return new VBox[]{paneDashboard, paneRooms, paneCustomers, paneBookings,
                          paneFood, paneLaundry, paneCheckout, paneAudit, paneStaff};
    }

    private void showPane(VBox pane, Button navBtn) {
        for (VBox p : allPanes()) {
            p.setVisible(false);
            p.setManaged(false);
        }
        pane.setVisible(true);
        pane.setManaged(true);

        if (activeNavBtn != null) {
            activeNavBtn.getStyleClass().remove("sidebar-btn-active");
            if (!activeNavBtn.getStyleClass().contains("sidebar-btn"))
                activeNavBtn.getStyleClass().add("sidebar-btn");
        }
        navBtn.getStyleClass().remove("sidebar-btn");
        navBtn.getStyleClass().add("sidebar-btn-active");
        activeNavBtn = navBtn;
    }

    @FXML private void navDashboard() { refreshAllData();  showPane(paneDashboard, btnNavDashboard); }
    @FXML private void navRooms()     { refreshAllData();  showPane(paneRooms,     btnNavRooms);    }
    @FXML private void navCustomers() { refreshAllData();  showPane(paneCustomers, btnNavCustomers);}
    @FXML private void navBookings()  { refreshAllData();  showPane(paneBookings,  btnNavBookings); }
    @FXML private void navFood()      { refreshAllData();  showPane(paneFood,      btnNavFood);     }
    @FXML private void navLaundry()   { refreshAllData();  showPane(paneLaundry,   btnNavLaundry);  }
    @FXML private void navCheckout()  { refreshAllData();  showPane(paneCheckout,  btnNavCheckout); }
    @FXML private void navAudit()     { refreshAllData();  showPane(paneAudit,     btnNavAudit);    }
    @FXML private void navStaff()     { refreshAllData();  showPane(paneStaff,     btnNavStaff);    }

    // ── Rooms ─────────────────────────────────────────────────────────────────
    @FXML private void handleAddRoom() {
        try {
            int    num   = Integer.parseInt(roomNumInput.getText().trim());
            String type  = roomTypeCombo.getValue();
            double price = Double.parseDouble(roomPriceInput.getText().trim());
            if (type == null) { alert(Alert.AlertType.WARNING, "Please select a room type."); return; }

            hotelService.addRoom(new Room(num, type, price, true), currentUser.getUsername());
            roomNumInput.clear(); roomPriceInput.clear();
            refreshAllData();
            alert(Alert.AlertType.INFORMATION, "Room " + num + " added successfully.");
        } catch (NumberFormatException e) {
            alert(Alert.AlertType.ERROR, "Invalid input — enter numeric values for Room Number and Price.");
        } catch (IllegalArgumentException e) {
            alert(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    @FXML private void handleRemoveRoom() {
        Room r = removeRoomCombo.getValue();
        if (r == null) { alert(Alert.AlertType.WARNING, "Select a room to remove."); return; }
        try {
            hotelService.removeRoom(r.getRoomNumber(), currentUser.getUsername());
            refreshAllData();
            alert(Alert.AlertType.INFORMATION, "Room " + r.getRoomNumber() + " removed.");
        } catch (IllegalArgumentException e) {
            alert(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    // ── Customers ─────────────────────────────────────────────────────────────
    @FXML private void handleAddCustomer() {
        String name  = custNameInput.getText().trim();
        String phone = custPhoneInput.getText().trim();
        String email = custEmailInput.getText().trim();
        if (name.isEmpty() || phone.isEmpty()) {
            alert(Alert.AlertType.WARNING, "Name and Phone are required."); return;
        }
        try {
            int newId = hotelService.getNextCustomerId();
            hotelService.addCustomer(new Customer(newId, name, phone, email),
                                     currentUser.getUsername());
            custNameInput.clear(); custPhoneInput.clear(); custEmailInput.clear();
            refreshAllData();
            alert(Alert.AlertType.INFORMATION, "Customer \"" + name + "\" added (ID: " + newId + ").");
        } catch (IllegalArgumentException e) {
            alert(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    @FXML private void handleRemoveCustomer() {
        Customer c = removeCustomerCombo.getValue();
        if (c == null) { alert(Alert.AlertType.WARNING, "Select a customer to remove."); return; }
        try {
            hotelService.removeCustomer(c.getCustomerId(), currentUser.getUsername());
            refreshAllData();
            alert(Alert.AlertType.INFORMATION, "Customer removed.");
        } catch (IllegalArgumentException e) {
            alert(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    // ── Bookings ──────────────────────────────────────────────────────────────
    @FXML private void handleBookRoom() {
        Customer c = bookCustCombo.getValue();
        Room     r = bookRoomCombo.getValue();
        LocalDate ci = checkInPicker.getValue();
        if (c == null || r == null || ci == null) {
            alert(Alert.AlertType.WARNING, "Please select Customer, Room and Check-In date."); return;
        }
        if (ci.isBefore(LocalDate.now())) {
            alert(Alert.AlertType.WARNING, "Check-in date cannot be in the past."); return;
        }
        try {
            Booking b = hotelService.bookRoom(r.getRoomNumber(), c.getCustomerId(),
                                              ci.toString(), currentUser.getUsername());
            refreshAllData();
            alert(Alert.AlertType.INFORMATION, "Booking confirmed!\nID: " + b.getBookingId());
        } catch (IllegalArgumentException e) {
            alert(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    // ── Food ──────────────────────────────────────────────────────────────────
    @FXML private void handleAddFood() {
        Booking b    = foodBookingCombo.getValue();
        String  item = foodItemCombo.getValue();
        if (b == null || item == null) {
            alert(Alert.AlertType.WARNING, "Select a booking and a menu item."); return;
        }
        int qty = foodQtySpinner.getValue();
        // Parse name and price from format "Name  —  ₹Price"
        String[] parts = item.split("  —  ₹");
        String itemName = parts[0].trim();
        double price    = Double.parseDouble(parts[1].trim());
        try {
            hotelService.addFoodOrder(b.getBookingId(),
                                      new FoodOrder(itemName, price, qty),
                                      currentUser.getUsername());
            refreshFoodList();
            alert(Alert.AlertType.INFORMATION, "Food order placed: " + itemName + " x" + qty);
        } catch (IllegalArgumentException e) {
            alert(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    private void refreshFoodList() {
        Booking b = foodBookingCombo.getValue();
        if (b == null) return;
        // Reload from service (booking object in list may be stale)
        Booking fresh = hotelService.findBookingById(b.getBookingId());
        if (fresh == null) return;
        ObservableList<String> orders = FXCollections.observableArrayList();
        double total = 0;
        for (FoodOrder fo : fresh.getFoodOrders()) {
            orders.add(fo.toString());
            total += fo.getTotal();
        }
        foodOrderList.setItems(orders);
        foodTotalLabel.setText(String.format("Food Total: ₹%.2f", total));
    }

    // ── Laundry ───────────────────────────────────────────────────────────────
    @FXML private void handleAddLaundry() {
        Booking b    = laundryBookingCombo.getValue();
        String  item = laundryItemCombo.getValue();
        if (b == null || item == null) {
            alert(Alert.AlertType.WARNING, "Select a booking and a laundry item."); return;
        }
        int qty = laundryQtySpinner.getValue();
        String[] parts = item.split("  —  ₹");
        String itemName = parts[0].trim();
        double price    = Double.parseDouble(parts[1].trim());
        try {
            hotelService.addLaundryOrder(b.getBookingId(),
                                         new LaundryOrder(itemName, price, qty),
                                         currentUser.getUsername());
            refreshLaundryList();
            alert(Alert.AlertType.INFORMATION, "Laundry added: " + itemName + " x" + qty);
        } catch (IllegalArgumentException e) {
            alert(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    private void refreshLaundryList() {
        Booking b = laundryBookingCombo.getValue();
        if (b == null) return;
        Booking fresh = hotelService.findBookingById(b.getBookingId());
        if (fresh == null) return;
        ObservableList<String> orders = FXCollections.observableArrayList();
        double total = 0;
        for (LaundryOrder lo : fresh.getLaundryOrders()) {
            orders.add(lo.toString());
            total += lo.getTotal();
        }
        laundryOrderList.setItems(orders);
        laundryTotalLabel.setText(String.format("Laundry Total: ₹%.2f", total));
    }

    // ── Checkout ──────────────────────────────────────────────────────────────
    @FXML private void handleCheckout() {
        Booking b  = checkoutBookingCombo.getValue();
        LocalDate checkOut = checkOutPicker.getValue();
        if (b == null) { alert(Alert.AlertType.WARNING, "Select a booking to checkout."); return; }
        if (checkOut == null) { alert(Alert.AlertType.WARNING, "Select a checkout date."); return; }
        if (checkOut.isBefore(LocalDate.parse(b.getCheckInDate()))) {
            alert(Alert.AlertType.WARNING, "Check-out date cannot be before check-in date."); return;
        }

        Room room = hotelService.findRoom(b.getRoomNumber());
        if (room == null) { alert(Alert.AlertType.ERROR, "Room not found."); return; }

        double totalBill = billingService.calculateTotal(b, room, checkOut.toString());
        String bill      = billingService.generateBill(b, room, checkOut.toString());

        // Disable button to prevent double-click
        checkoutBtn.setDisable(true);
        checkoutStepsArea.clear();
        billDisplayArea.clear();

        // Commit checkout in service (marks room available, saves)
        hotelService.checkout(b.getBookingId(), checkOut.toString(), totalBill, currentUser.getUsername());

        // Run threaded room-cleaning steps
        CheckoutService svc = new CheckoutService(room,
            msg -> checkoutStepsArea.appendText(msg + "\n"),
            () -> {
                billDisplayArea.setText(bill);
                checkoutBtn.setDisable(false);
                refreshAllData();
                alert(Alert.AlertType.INFORMATION,
                    String.format("✅ Checkout complete!\nTotal Bill: ₹%.2f", totalBill));
            }
        );
        Thread t = new Thread(svc, "checkout-room-" + room.getRoomNumber());
        t.setDaemon(true);
        t.start();
    }

    // ── Audit ─────────────────────────────────────────────────────────────────
    @FXML private void refreshAudit() {
        auditOList.setAll(hotelService.getAuditLogs());
    }

    // ── Staff ─────────────────────────────────────────────────────────────────
    @FXML private void handleAddStaff() {
        String name     = staffNameInput.getText().trim();
        String username = staffUserInput.getText().trim();
        String password = staffPassInput.getText();
        String roleStr  = staffRoleCombo.getValue();
        if (name.isEmpty() || username.isEmpty() || password.isEmpty() || roleStr == null) {
            alert(Alert.AlertType.WARNING, "All fields are required."); return;
        }
        User.Role role = User.Role.valueOf(roleStr);
        try {
            hotelService.addUser(new User(username, password, role, name), currentUser.getUsername());
            staffNameInput.clear(); staffUserInput.clear(); staffPassInput.clear();
            refreshAllData();
            alert(Alert.AlertType.INFORMATION, "Staff account \"" + username + "\" created.");
        } catch (IllegalArgumentException e) {
            alert(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    @FXML private void handleRemoveStaff() {
        User u = removeStaffCombo.getValue();
        if (u == null) { alert(Alert.AlertType.WARNING, "Select a user to remove."); return; }
        try {
            hotelService.removeUser(u.getUsername(), currentUser.getUsername());
            refreshAllData();
            alert(Alert.AlertType.INFORMATION, "User \"" + u.getUsername() + "\" removed.");
        } catch (IllegalArgumentException e) {
            alert(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    // ── Logout ────────────────────────────────────────────────────────────────
    @FXML private void handleLogout() {
        hotelService.log(currentUser.getUsername(), "LOGOUT", currentUser.getDisplayName() + " logged out");
        hotelService.saveAll();
        try {
            URL loc = getClass().getResource("/com/hotel/management/view/login.fxml");
            FXMLLoader loader = new FXMLLoader(loc);
            Parent root = loader.load();
            Stage stage = (Stage) headerUserLabel.getScene().getWindow();
            Scene scene = new Scene(root, 900, 600);
            URL css = getClass().getResource("/com/hotel/management/css/style.css");
            if (css != null) scene.getStylesheets().add(css.toExternalForm());
            stage.setTitle("The Sapphire Hotel — Login");
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ── Helper ────────────────────────────────────────────────────────────────
    private void alert(Alert.AlertType type, String msg) {
        Alert a = new Alert(type);
        a.setHeaderText(null);
        a.setContentText(msg);
        URL css = getClass().getResource("/com/hotel/management/css/style.css");
        if (css != null) a.getDialogPane().getStylesheets().add(css.toExternalForm());
        a.showAndWait();
    }
}
