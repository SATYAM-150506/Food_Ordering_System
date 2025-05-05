package com.foodorder.Service;

import com.foodorder.dsa.OrderHistoryStack;
import com.foodorder.dsa.OrderQueue;
import com.foodorder.model.MenuItem;
import com.foodorder.model.Order;
import com.foodorder.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OrderService {

    private final MenuService menuService;
    private final BillingService billingService;
    private final OrderQueue orderQueue;
    private final OrderHistoryStack orderHistoryStack;
    private int orderIdCounter = 1;

    public OrderService(MenuService menuService) {
        this.menuService = menuService;
        this.billingService = new BillingService();
        this.orderQueue = new OrderQueue();
        this.orderHistoryStack = new OrderHistoryStack();
    }

    // Place a new order
    public void placeOrder(User user) {
        Scanner scanner = new Scanner(System.in);
        List<MenuItem> orderedItems = new ArrayList<>();

        menuService.displayMenuForUser();  // Display menu to user

        while (true) {
            System.out.print("Enter item ID to add to order (or 0 to finish): ");
            int id = scanner.nextInt();
            if (id == 0) break;

            // Fetch the item from in-memory MenuManager instead of MenuService
            MenuItem item = menuService.getMenuItemById(id);  // Assuming a getMenuItemById method
            if (item != null) {
                orderedItems.add(item);
                System.out.println("✅ Added: " + item.getName());
            } else {
                System.out.println("❌ Item not found.");
            }
        }

        if (orderedItems.isEmpty()) {
            System.out.println("⚠️ No items selected. Order canceled.");
            return;
        }

        double total = billingService.calculateTotal(orderedItems);
        double discount = billingService.calculateDiscount(total);
        double finalAmount = total - discount;

        // Create a new Order object
        Order order = new Order(
                orderIdCounter++,
                user,
                orderedItems,
                total,
                LocalDateTime.now(),
                discount,
                finalAmount
        );

        // Store in queue and stack
        orderQueue.enqueue(order);
        orderHistoryStack.push(order);

        // Print bill
        System.out.println("\n✅ Order placed successfully!");
        System.out.println(billingService.generateBill(order));
    }

    // Show current pending orders (FIFO)
    public void showPendingOrders() {
        System.out.println("\n📦 Pending Orders (FIFO):");
        orderQueue.displayQueue();
    }

    // Show full order history (LIFO)
    public void showOrderHistory() {
        System.out.println("\n🧾 Order History (LIFO):");
        orderHistoryStack.displayHistory();
    }

    // Mark the next order as completed
    public void completeNextOrder() {
        Order completed = orderQueue.dequeue();
        if (completed != null) {
            System.out.println("✅ Order completed: " + completed.getOrderId());
        } else {
            System.out.println("No pending orders.");
        }
    }
}
