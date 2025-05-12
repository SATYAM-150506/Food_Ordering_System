package com.foodorder.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class DatabaseViewer extends JFrame {
    private static final String DB_URL = "jdbc:sqlite:food_ordering_new.db";

    private DefaultTableModel menuModel;
    private JTable menuTable;
    private JTextField nameField;
    private JTextField priceField;

    public DatabaseViewer() {
        setTitle("📊 Food Ordering System - Database Viewer");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        JPanel mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Users", new JScrollPane(createTable("users")));
        tabbedPane.addTab("Admins", new JScrollPane(createTable("admins")));
        tabbedPane.addTab("Menu Items", createMenuPanel());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton refreshButton = new JButton("🔄 Refresh Data");
        JButton exitButton = new JButton("❌ Exit");

        refreshButton.addActionListener(e -> refreshData(tabbedPane));
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(refreshButton);
        buttonPanel.add(exitButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    private JTable createTable(String tableName) {
        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);

        table.setFont(new Font("Tahoma", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(0, 120, 215));
        table.getTableHeader().setForeground(Color.BLACK);
        table.setGridColor(new Color(222, 220, 220));
        table.setDefaultRenderer(Object.class, new CustomTableCellRenderer());

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(metaData.getColumnName(i));
            }

            while (rs.next()) {
                Vector<Object> rowData = new Vector<>();
                for (int i = 0; i < columnCount; i++) {
                    rowData.add(rs.getObject(i + 1));
                }
                model.addRow(rowData);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "❌ Error loading data from table: " + tableName + "\n" + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        return table;
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        menuModel = new DefaultTableModel(new String[]{"id", "name", "price"}, 0);
        menuTable = new JTable(menuModel);

        menuTable.setFont(new Font("Tahoma", Font.PLAIN, 14));
        menuTable.setRowHeight(30);
        menuTable.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 14));
        menuTable.getTableHeader().setBackground(new Color(0, 120, 215));
        menuTable.getTableHeader().setForeground(Color.BLACK);
        menuTable.setGridColor(new Color(222, 220, 220));
        menuTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer());

        JScrollPane scrollPane = new JScrollPane(menuTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        nameField = new JTextField();
        priceField = new JTextField();

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Price:"));
        formPanel.add(priceField);

        panel.add(formPanel, BorderLayout.NORTH);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton insertBtn = new JButton("➕ Add Item");
        JButton updateBtn = new JButton("✏️ Update Item");
        JButton deleteBtn = new JButton("🗑️ Delete Item");

        insertBtn.addActionListener(e -> insertMenuItem());
        updateBtn.addActionListener(e -> updateMenuItem());
        deleteBtn.addActionListener(e -> deleteMenuItem());

        controlPanel.add(insertBtn);
        controlPanel.add(updateBtn);
        controlPanel.add(deleteBtn);

        panel.add(controlPanel, BorderLayout.SOUTH);

        menuTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && menuTable.getSelectedRow() != -1) {
                nameField.setText(menuTable.getValueAt(menuTable.getSelectedRow(), 1).toString());
                priceField.setText(menuTable.getValueAt(menuTable.getSelectedRow(), 2).toString());
            }
        });

        refreshMenuTable();
        return panel;
    }

    private void insertMenuItem() {
        String name = nameField.getText();
        String price = priceField.getText();

        if (name.isEmpty() || price.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String query = "INSERT INTO menu_items (name, price) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setDouble(2, Double.parseDouble(price));
            stmt.executeUpdate();
            refreshMenuTable();
            clearFields();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Insert failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateMenuItem() {
        int row = menuTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to update.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = menuTable.getValueAt(row, 0).toString();
        String name = nameField.getText();
        String price = priceField.getText();

        String query = "UPDATE menu_items SET name=?, price=? WHERE id=?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setDouble(2, Double.parseDouble(price));
            stmt.setInt(3, Integer.parseInt(id));
            stmt.executeUpdate();
            refreshMenuTable();
            clearFields();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Update failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteMenuItem() {
        int row = menuTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = menuTable.getValueAt(row, 0).toString();

        String query = "DELETE FROM menu_items WHERE id=?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(id));
            stmt.executeUpdate();
            refreshMenuTable();
            clearFields();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Delete failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshMenuTable() {
        menuModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, name, price FROM menu_items")) {
            while (rs.next()) {
                menuModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load menu_items: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        nameField.setText("");
        priceField.setText("");
    }

    private void refreshData(JTabbedPane tabbedPane) {
        tabbedPane.removeAll();
        tabbedPane.addTab("Users", new JScrollPane(createTable("users")));
        tabbedPane.addTab("Admins", new JScrollPane(createTable("admins")));
        tabbedPane.addTab("Menu Items", createMenuPanel());
    }

    class CustomTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (row % 2 == 0) {
                c.setBackground(new Color(245, 245, 245));
            } else {
                c.setBackground(Color.WHITE);
            }

            if (isSelected) {
                c.setBackground(new Color(0, 120, 215));
                c.setForeground(Color.WHITE);
            } else {
                c.setForeground(Color.BLACK);
            }

            return c;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DatabaseViewer viewer = new DatabaseViewer();
            viewer.setVisible(true);
        });
    }
}
