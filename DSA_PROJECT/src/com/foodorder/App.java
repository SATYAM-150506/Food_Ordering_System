package com.foodorder;

import com.foodorder.model.User;
import com.foodorder.Service.MenuService;
import com.foodorder.Service.OrderService;
import com.foodorder.Service.MenuManager;
import com.foodorder.util.DBUtil;

import java.util.InputMismatchException;
import java.util.Scanner;

public class App {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            // Initialize the database
            DBUtil.initDatabase();

            // Initialize MenuManager and MenuService
            MenuManager menuManager = new MenuManager();
            MenuService menuService = new MenuService(menuManager);

            // Initialize OrderService
            OrderService orderService = new OrderService(menuService);

            System.out.println("========== Welcome to Java Food Ordering System ==========");

            boolean exit = false;
            while (!exit) {
                System.out.println("\n1. Login");
                System.out.println("2. Register");
                System.out.println("3. Exit");
                System.out.print("Select option: ");
                try {
                    int option = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    switch (option) {
                        case 1 -> login(menuService, orderService);
                        case 2 -> register();
                        case 3 -> exit = true;
                        default -> System.out.println("❌ Invalid option.");
                    }
                } catch (InputMismatchException ime) {
                    System.out.println("❌ Invalid input. Please enter a valid number.");
                    scanner.nextLine(); // Clear the invalid input
                }
            }
        } catch (Exception e) {
            System.out.println("❌ A critical error occurred: " + e.getMessage());
        }
    }

    private static void login(MenuService menuService, OrderService orderService) {
        try {
            System.out.print("Are you an admin? (yes/no): ");
            boolean isAdmin = scanner.nextLine().equalsIgnoreCase("yes");

            if (isAdmin) {
                // Admin Login
                System.out.print("Enter username: ");
                String username = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();

                if (DBUtil.validateAdmin(username, password)) {
                    System.out.println("✅ Admin login successful.");
                    adminMenu(menuService, orderService);
                } else {
                    System.out.println("❌ Invalid credentials.");
                }
            } else {
                // User Login
                System.out.print("Enter email: ");
                String email = scanner.nextLine();

                User user = DBUtil.getUserByEmail(email);
                if (user == null) {
                    System.out.println("❌ User not found.");
                    return;
                }
                userMenu(user, menuService, orderService);
            }
        } catch (Exception e) {
            System.out.println("❌ An error occurred during login: " + e.getMessage());
        }
    }

    private static void register() {
        try {
            System.out.print("Are you an admin? (yes/no): ");
            boolean isAdmin = scanner.nextLine().equalsIgnoreCase("yes");

            if (isAdmin) {
                System.out.println("❌ Admin registration not allowed from UI.");
            } else {
                // User Registration
                System.out.print("Enter user ID: ");
                String id = scanner.nextLine();
                System.out.print("Enter name: ");
                String name = scanner.nextLine();
                System.out.print("Enter email: ");
                String email = scanner.nextLine();

                if (DBUtil.userExists(email)) {
                    System.out.println("⚠️ Email already registered.");
                } else {
                    DBUtil.insertUser(new User(id, name, email, false));
                    System.out.println("✅ User registered successfully.");
                }
            }
        } catch (Exception e) {
            System.out.println("❌ An error occurred during registration: " + e.getMessage());
        }
    }

    private static void adminMenu(MenuService menuService, OrderService orderService) {
        boolean back = false;
        while (!back) {
            System.out.println("\n1. View Menu");
            System.out.println("2. Add Menu Item");
            System.out.println("3. Update Menu Item");
            System.out.println("4. Delete Menu Item");
            System.out.println("5. View Orders");
            System.out.println("6. Complete Order");
            System.out.println("7. Back");
            System.out.print("Select option: ");
            try {
                int opt = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                switch (opt) {
                    case 1 -> menuService.displayMenuForUser();
                    case 2 -> menuService.addMenuItemAsAdmin();
                    case 3 -> menuService.updateMenuItemAsAdmin();
                    case 4 -> menuService.removeMenuAsAdmin();
                    case 5 -> orderService.showPendingOrders();
                    case 6 -> orderService.completeNextOrder();
                    case 7 -> back = true;
                    default -> System.out.println("❌ Invalid option.");
                }
            } catch (InputMismatchException ime) {
                System.out.println("❌ Invalid input. Please enter a valid number.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    private static void userMenu(User user, MenuService menuService, OrderService orderService) {
        boolean back = false;
        while (!back) {
            System.out.println("\n1. View Menu");
            System.out.println("2. Place Order");
            System.out.println("3. Back");
            System.out.print("Select option: ");
            try {
                int opt = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                switch (opt) {
                    case 1 -> menuService.displayMenuForUser();
                    case 2 -> orderService.placeOrder(user);
                    case 3 -> back = true;
                    default -> System.out.println("❌ Invalid option.");
                }
            } catch (InputMismatchException ime) {
                System.out.println("❌ Invalid input. Please enter a valid number.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
}