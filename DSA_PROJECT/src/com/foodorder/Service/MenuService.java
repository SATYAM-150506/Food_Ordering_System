package com.foodorder.Service;

import com.foodorder.model.MenuItem;
import com.foodorder.util.DBUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class MenuService {

    private final MenuManager menuManager;
    private final Scanner scanner = new Scanner(System.in);
    private static int nextId = 11;

    public MenuService(MenuManager menuManager) {
        this.menuManager = menuManager;
    }

    // Display menu for user
    public void displayMenuForUser() {
        // Convert Collection to List if getAllMenuItems() returns a Collection
        Collection<MenuItem> menuItemsCollection = menuManager.getAllMenuItems();
        List<MenuItem> menuItems = new ArrayList<>(menuItemsCollection);

        if (menuItems.isEmpty()) {
            System.out.println("📭 Menu is currently empty.");
            return;
        }

        System.out.println("\n📋 Menu Items:");
        for (MenuItem item : menuItems) {
            System.out.println(item);
        }
    }

    // Admin functionalities

    public void addMenuItemAsAdmin() {
        System.out.print("🔹 Enter item name: ");
        String name = scanner.nextLine();

        System.out.print("💰 Enter item price: ");
        double price = getValidPrice();
        if (price < 0) return;

        MenuItem newItem = new MenuItem(nextId++, name, price);
        menuManager.addMenuItem(newItem);
        DBUtil.addMenuItemToDB(newItem);
        System.out.println("✅ Item added: " + newItem);
    }

    public void updateMenuItemAsAdmin() {
        displayMenuForUser();
        System.out.print("✏️ Enter ID of item to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        MenuItem item = menuManager.getMenuItemById(id);
        if (item == null) {
            System.out.println("❌ Item not found.");
            return;
        }

        System.out.print("🔄 New name (leave blank to keep '" + item.getName() + "'): ");
        String newName = scanner.nextLine();

        System.out.print("💰 New price (or -1 to keep " + item.getPrice() + "): ");
        double newPrice = getValidPrice();

        if (!newName.isEmpty()) item.setName(newName);
        if (newPrice >= 0) item.setPrice(newPrice);

        menuManager.updateMenuItem(id, newName, newPrice);
        DBUtil.updateMenuItemInDB(item);
        System.out.println("✅ Item updated: " + item);
    }

    public void removeMenuAsAdmin() {
        displayMenuForUser();
        System.out.print("🗑️ Enter ID of item to remove: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        MenuItem item = menuManager.getMenuItemById(id);
        if (item == null) {
            System.out.println("❌ Item not found.");
            return;
        }

        menuManager.removeMenuItem(id);
        DBUtil.removeMenuItemFromDB(id);
        System.out.println("✅ Item removed: " + item);
    }

    public MenuItem getMenuItemById(int id) {
        return menuManager.getMenuItemById(id);  // Delegating to MenuManager
    }
    // Helper methods

    private double getValidPrice() {
        try {
            double price = scanner.nextDouble();
            scanner.nextLine();
            if (price < 0 && price != -1) {
                System.out.println("❌ Price must be non-negative.");
                return -1;
            }
            return price;
        } catch (Exception e) {
            scanner.nextLine(); // Clear buffer
            System.out.println("❌ Invalid input. Please enter a number.");
            return -1;
        }
    }
}
