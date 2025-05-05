package com.foodorder.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MenuItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private double price;

    // Constructor
    public MenuItem(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return id + " - " + name + " ($" + price + ")";
    }

    // Default Indian Menu items
    public static List<MenuItem> getDefaultMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();

        // Indian Dishes
        menuItems.add(new MenuItem(1, "Butter Chicken", 7.99));
        menuItems.add(new MenuItem(2, "Chicken Biryani", 8.49));
        menuItems.add(new MenuItem(3, "Paneer Tikka", 6.99));
        menuItems.add(new MenuItem(4, "Chole Bhature", 5.99));
        menuItems.add(new MenuItem(5, "Aloo Paratha", 3.99));
        menuItems.add(new MenuItem(6, "Samosa", 2.49));
        menuItems.add(new MenuItem(7, "Dal Makhani", 6.49));
        menuItems.add(new MenuItem(8, "Pav Bhaji", 5.49));
        menuItems.add(new MenuItem(9, "Masala Dosa", 4.99));
        menuItems.add(new MenuItem(10, "Gulab Jamun", 3.49));

        return menuItems;
    }
}
