package com.hotel.management;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL fxmlLocation = getClass().getResource("/com/hotel/management/view/login.fxml");
        if (fxmlLocation == null) {
            System.err.println("Cannot find login.fxml");
            System.exit(1);
        }

        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        Parent root = loader.load();

        Scene scene = new Scene(root, 900, 600);

        URL css = getClass().getResource("/com/hotel/management/css/style.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());

        primaryStage.setTitle("The Sapphire Hotel — Login");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(700);
        primaryStage.setMinHeight(500);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
