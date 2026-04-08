package com.hotel.management.services;

import com.hotel.management.models.Room;
import javafx.application.Platform;

import java.util.function.Consumer;

/**
 * Simulates the post-checkout room preparation process on a background thread.
 * Synchronized on the Room object to prevent concurrent checkouts of the same room.
 */
public class CheckoutService implements Runnable {

    private final Room room;
    private final Consumer<String> onStep;
    private final Runnable onComplete;

    public CheckoutService(Room room, Consumer<String> onStep, Runnable onComplete) {
        this.room = room;
        this.onStep = onStep;
        this.onComplete = onComplete;
    }

    @Override
    public void run() {
        synchronized (room) {
            try {
                step("🧹  Cleaning Room " + room.getRoomNumber() + " ...", 2000);
                step("🗝️  Deactivating & collecting key card ...", 1500);
                step("🔍  Performing final room inspection ...", 1500);
                step("🧺  Sending used linen to laundry ...", 1000);
                step("✅  Room " + room.getRoomNumber() + " is clean and ready for the next guest!", 600);
                Platform.runLater(onComplete);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Platform.runLater(() -> onStep.accept("⚠️  Checkout process was interrupted."));
            }
        }
    }

    private void step(String message, long delayMs) throws InterruptedException {
        Platform.runLater(() -> onStep.accept(message));
        Thread.sleep(delayMs);
    }
}
