package com.hotel.management.controllers;

import com.hotel.management.models.User;
import com.hotel.management.services.AuthService;
import com.hotel.management.services.HotelService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class LoginController {

    @FXML private TextField     usernameInput;
    @FXML private PasswordField passwordInput;
    @FXML private Label         errorLabel;
    @FXML private Button        loginBtn;

    private HotelService hotelService;
    private AuthService  authService;

    @FXML
    public void initialize() {
        hotelService = new HotelService();
        authService  = new AuthService(hotelService.getUsers());
        errorLabel.setText("");
        // Allow Enter key to submit
        passwordInput.setOnAction(e -> handleLogin());
        usernameInput.setOnAction(e -> passwordInput.requestFocus());
    }

    @FXML
    private void handleLogin() {
        String username = usernameInput.getText().trim();
        String password = passwordInput.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("⚠  Please enter your username and password.");
            return;
        }

        User user = authService.login(username, password);
        if (user == null) {
            errorLabel.setText("✕  Invalid username or password.");
            passwordInput.clear();
            return;
        }

        hotelService.log(user.getUsername(), "LOGIN",
                user.getDisplayName() + " logged in as " + user.getRole());
        hotelService.saveAll();

        openDashboard(user);
    }

    private void openDashboard(User user) {
        try {
            URL location = getClass().getResource("/com/hotel/management/view/dashboard.fxml");
            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load();

            DashboardController ctrl = loader.getController();
            ctrl.initWithUser(user, hotelService);

            Stage stage = (Stage) loginBtn.getScene().getWindow();
            Scene scene = new Scene(root, 1280, 780);
            URL css = getClass().getResource("/com/hotel/management/css/style.css");
            if (css != null) scene.getStylesheets().add(css.toExternalForm());

            stage.setTitle("The Sapphire Hotel — " + user.getDisplayName());
            stage.setScene(scene);
            stage.setMinWidth(1050);
            stage.setMinHeight(680);
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Error loading dashboard: " + e.getMessage());
        }
    }
}
