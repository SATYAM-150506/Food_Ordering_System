package com.foodorder.Service;

import com.foodorder.model.MenuItem;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

public class MenuManager implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<Integer, MenuItem> menuMap;

    public MenuManager() {
        this.menuMap = new HashMap<>();
        initializeDefaultMenu(); // Add default items on startup
    }

    // Add default Indian menu items
    private void initializeDefaultMenu() {
        menuMap.put(1, new MenuItem(1, "Paneer Butter Masala", 180.0));
        menuMap.put(2, new MenuItem(2, "Chicken Biryani", 220.0));
        menuMap.put(3, new MenuItem(3, "Masala Dosa", 90.0));
        menuMap.put(4, new MenuItem(4, "Chole Bhature", 100.0));
        menuMap.put(5, new MenuItem(5, "Rajma Chawal", 110.0));
        menuMap.put(6, new MenuItem(6, "Veg Thali", 150.0));
        menuMap.put(7, new MenuItem(7, "Idli Sambar", 70.0));
        menuMap.put(8, new MenuItem(8, "Aloo Paratha", 60.0));
        menuMap.put(9, new MenuItem(9, "Hyderabadi Dum Biryani", 250.0));
        menuMap.put(10, new MenuItem(10, "Tandoori Roti", 20.0));
    }

    public void addMenuItem(MenuItem item) {
        if (menuMap.containsKey(item.getId())) {
            System.out.println("Item with ID " + item.getId() + " already exists.");
            return;
        }
        menuMap.put(item.getId(), item);
        System.out.println("✅ Added menu item: " + item.getName());
    }

    public void updateMenuItem(int id, String newName, double newPrice) {
        MenuItem item = menuMap.get(id);
        if (item == null) {
            System.out.println("❌ Item not found.");
            return;
        }
        item.setName(newName);
        item.setPrice(newPrice);
        System.out.println("✅ Updated item ID " + id + " to: " + newName + " ($" + newPrice + ")");
    }

    public void removeMenuItem(int id) {
        if (menuMap.remove(id) != null) {
            System.out.println("✅ Removed item ID: " + id);
        } else {
            System.out.println("❌ Item ID not found.");
        }
    }

    public Collection<MenuItem> getAllMenuItems() {
        return menuMap.values();
    }

    public MenuItem getMenuItemById(int id) {
        return menuMap.get(id);
    }

    public void displayMenu() {
        if (menuMap.isEmpty()) {
            System.out.println("📭 Menu is empty.");
            return;
        }
        System.out.println("\n📋 ---- MENU ----");
        for (MenuItem item : menuMap.values()) {
            System.out.println(item);
        }
    }
}
