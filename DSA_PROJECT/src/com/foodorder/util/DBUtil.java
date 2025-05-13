package com.foodorder.util;

import com.foodorder.model.MenuItem;
import com.foodorder.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBUtil {
    private static final String DB_URL = "jdbc:sqlite:food_ordering_new.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("⚠️ SQLite JDBC driver not found.");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    // Initializes DB tables and default admin
    public static void initDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Create users table
            String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    user_id TEXT PRIMARY KEY,
                    name TEXT NOT NULL,
                    email TEXT NOT NULL UNIQUE
                );
            """;

            // Create admins table
            String createAdminsTable = """
                CREATE TABLE IF NOT EXISTS admins (
                    admin_id TEXT PRIMARY KEY,
                    username TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL
                );
            """;

            // Create menu_items table
            String createMenuItemsTable = """
                CREATE TABLE IF NOT EXISTS menu_items (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    price REAL NOT NULL
                );
            """;

            stmt.execute(createUsersTable);
            stmt.execute(createAdminsTable);
            stmt.execute(createMenuItemsTable);

            // Insert default admin if not exists
            String checkAdminExists = "SELECT * FROM admins WHERE username = ?";
            try (PreparedStatement ps = conn.prepareStatement(checkAdminExists)) {
                ps.setString(1, "satyam");
                ResultSet rs = ps.executeQuery();

                if (!rs.next()) {
                    String insertAdmin = "INSERT INTO admins (admin_id, username, password) VALUES (?, ?, ?)";
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertAdmin)) {
                        insertStmt.setString(1, "admin001");
                        insertStmt.setString(2, "satyam");
                        insertStmt.setString(3, "Satyam@1207");
                        insertStmt.executeUpdate();
                        System.out.println("✅ Default admin created: username='satyam'");
                    }
                } else {
                    System.out.println("ℹ️ Admin 'satyam' already exists.");
                }
            }

            System.out.println("✅ Database initialized successfully.");
        } catch (SQLException e) {
            System.err.println("❌ Error initializing database.");
            e.printStackTrace();
        }
    }

    // Validate admin login
    public static boolean validateAdmin(String username, String password) {
        String query = "SELECT * FROM admins WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("❌ Error validating admin.");
            e.printStackTrace();
            return false;
        }
    }

    // Check if user exists
    public static boolean userExists(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("❌ Error checking user.");
            e.printStackTrace();
            return false;
        }
    }

    // Insert a new user
    public static void insertUser(User user) {
        String query = "INSERT INTO users (user_id, name, email) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, user.getUserId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getEmail());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Error inserting user.");
            e.printStackTrace();
        }
    }

    // Get user by email
    public static User getUserByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getString("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        false
                );
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching user.");
            e.printStackTrace();
        }
        return null;
    }

    // ========== Menu Item Operations (For Database) ==========
    // Fetching all menu items from DB (this can be used when required)
    public static List<MenuItem> getAllMenuItemsFromDB() {
        List<MenuItem> menuItems = new ArrayList<>();
        String query = "SELECT * FROM menu_items";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                menuItems.add(new MenuItem(id, name, price));
            }

        } catch (SQLException e) {
            System.err.println("❌ Error fetching menu items.");
            e.printStackTrace();
        }
        return menuItems;
    }

    // Add a menu item to DB
    public static void addMenuItemToDB(MenuItem item) {
        String query = "INSERT INTO menu_items (name, price) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, item.getName());
            ps.setDouble(2, item.getPrice());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Error adding menu item.");
            e.printStackTrace();
        }
    }

    // Update a menu item in DB
    public static void updateMenuItemInDB(MenuItem item) {
        String query = "UPDATE menu_items SET name = ?, price = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, item.getName());
            ps.setDouble(2, item.getPrice());
            ps.setInt(3, item.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Error updating menu item.");
            e.printStackTrace();
        }
    }

    // Delete a menu item from DB
    public static void removeMenuItemFromDB(int id) {
        String query = "DELETE FROM menu_items WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Error deleting menu item.");
            e.printStackTrace();
        }
    }
}
