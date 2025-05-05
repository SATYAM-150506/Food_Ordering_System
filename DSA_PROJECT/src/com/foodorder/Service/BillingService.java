package com.foodorder.Service;

import com.foodorder.model.MenuItem;
import com.foodorder.model.Order;

import java.util.List;

public class BillingService {

    private static final double DISCOUNT_THRESHOLD = 300.0;
    private static final double DISCOUNT_RATE = 0.10;

    // Calculates the total cost of items in the order
    public double calculateTotal(List<MenuItem> items) {
        double total = 0;
        for (MenuItem item : items) {
            total += item.getPrice();
        }
        return total;
    }

    // Calculates discount based on a threshold
    public double calculateDiscount(double total) {
        if (total >= DISCOUNT_THRESHOLD) {
            return total * DISCOUNT_RATE;
        }
        return 0;
    }

    // Generates a formatted bill summary for display
    public String generateBill(Order order) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n========== BILL SUMMARY ==========\n");
        sb.append("Order ID   : ").append(order.getOrderId()).append("\n");
        sb.append("Username   : ").append(order.getUser()).append("\n");
        sb.append("Items Ordered:\n");

        for (MenuItem item : order.getItems()) {
            sb.append(" - ").append(item.getName())
                    .append(" (₹").append(String.format("%.2f", item.getPrice())).append(")\n");
        }

        sb.append("----------------------------------\n");
        sb.append(String.format("Subtotal    : ₹%.2f\n", order.getTotal()));
        sb.append(String.format("Discount    : ₹%.2f\n", order.getDiscount()));
        sb.append(String.format("Total Due   : ₹%.2f\n", order.getFinalAmount()));
        sb.append("==================================\n");

        return sb.toString();
    }
}
