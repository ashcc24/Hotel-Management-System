package com.hotel.management.services;

import com.hotel.management.models.Room;
import com.hotel.management.models.Booking;
import com.hotel.management.models.FoodOrder;
import com.hotel.management.models.LaundryOrder;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BillingService {

    public double calculateTotal(Booking booking, Room room, String checkOutDate) {
        long days = getDays(booking.getCheckInDate(), checkOutDate);
        double roomCost    = days * room.getPrice();
        double foodTotal   = booking.getFoodOrders().stream().mapToDouble(FoodOrder::getTotal).sum();
        double laundryTotalAmt = booking.getLaundryOrders().stream().mapToDouble(LaundryOrder::getTotal).sum();
        return roomCost + foodTotal + laundryTotalAmt;
    }

    public String generateBill(Booking booking, Room room, String checkOutDate) {
        long days       = getDays(booking.getCheckInDate(), checkOutDate);
        double roomCost = days * room.getPrice();
        double foodTotal = 0, laundryTotal = 0;

        StringBuilder sb = new StringBuilder();
        sb.append("╔══════════════════════════════════════════╗\n");
        sb.append("║         ★  THE SAPPHIRE HOTEL  ★         ║\n");
        sb.append("║              FINAL BILL                  ║\n");
        sb.append("╚══════════════════════════════════════════╝\n\n");
        sb.append(String.format("  Booking ID  : %s%n",  booking.getBookingId()));
        sb.append(String.format("  Guest Name  : %s%n",  booking.getCustomerName()));
        sb.append(String.format("  Room        : %d (%s)%n", room.getRoomNumber(), room.getType()));
        sb.append(String.format("  Check-In    : %s%n",  booking.getCheckInDate()));
        sb.append(String.format("  Check-Out   : %s%n",  checkOutDate));
        sb.append(String.format("  Duration    : %d night(s)%n%n", days));

        sb.append("  ──────────────────────────────────────────\n");
        sb.append("  ROOM CHARGES:\n");
        sb.append(String.format("    ₹%.0f/night × %d = ₹%.2f%n%n", room.getPrice(), days, roomCost));

        if (!booking.getFoodOrders().isEmpty()) {
            sb.append("  FOOD & BEVERAGE:\n");
            for (FoodOrder fo : booking.getFoodOrders()) {
                sb.append(String.format("    %-24s x%-3d ₹%.2f%n",
                        fo.getItemName(), fo.getQuantity(), fo.getTotal()));
                foodTotal += fo.getTotal();
            }
            sb.append(String.format("    Food Subtotal:              ₹%.2f%n%n", foodTotal));
        }

        if (!booking.getLaundryOrders().isEmpty()) {
            sb.append("  LAUNDRY SERVICES:\n");
            for (LaundryOrder lo : booking.getLaundryOrders()) {
                sb.append(String.format("    %-24s x%-3d ₹%.2f%n",
                        lo.getItemName(), lo.getQuantity(), lo.getTotal()));
                laundryTotal += lo.getTotal();
            }
            sb.append(String.format("    Laundry Subtotal:           ₹%.2f%n%n", laundryTotal));
        }

        double total = roomCost + foodTotal + laundryTotal;
        sb.append("  ══════════════════════════════════════════\n");
        sb.append(String.format("  TOTAL AMOUNT DUE:       ₹%.2f%n", total));
        sb.append("  ══════════════════════════════════════════\n\n");
        sb.append("  Thank you for staying with The Sapphire Hotel!\n");
        sb.append("  We hope to welcome you again soon.\n");
        return sb.toString();
    }

    private long getDays(String checkIn, String checkOut) {
        long d = ChronoUnit.DAYS.between(LocalDate.parse(checkIn), LocalDate.parse(checkOut));
        return d <= 0 ? 1 : d;
    }
}
