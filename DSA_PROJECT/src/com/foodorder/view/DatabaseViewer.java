package com.foodorder.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class DatabaseViewer extends JFrame {
    private static final String DB_URL = "jdbc:sqlite:food_ordering_new.db";

    public DatabaseViewer() {
        // Set the title and size
        setTitle("📊 Food Ordering System - Database Viewer");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set a native Windows Look and Feel
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        // Create the main panel and layout manager
        JPanel mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);

        // Create and setup the tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Add tables for Users, Admins, and Menu Items
        tabbedPane.addTab("Users", new JScrollPane(createTable("users")));
        tabbedPane.addTab("Admins", new JScrollPane(createTable("admins")));
        tabbedPane.addTab("Menu Items", new JScrollPane(createTable("menu_items")));

        // Add the tabbed pane to the main panel
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Add control buttons at the bottom
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton refreshButton = new JButton("🔄 Refresh Data");
        JButton exitButton = new JButton("❌ Exit");

        // Add listeners for buttons
        refreshButton.addActionListener(e -> refreshData(tabbedPane));
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(refreshButton);
        buttonPanel.add(exitButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Center the window on the screen
        setLocationRelativeTo(null);
    }

    private JTable createTable(String tableName) {
        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);

        // Set the font and row height for better readability
        table.setFont(new Font("Tahoma", Font.PLAIN, 14));
        table.setRowHeight(30);

        // Set column headers in a bold style
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(0, 120, 215));  // Windows blue color
        table.getTableHeader().setForeground(Color.BLACK);
        table.setGridColor(new Color(222, 220, 220));

        // Customize table appearance (cell rendering)
        table.setDefaultRenderer(Object.class, new CustomTableCellRenderer());

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Add column names
            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(metaData.getColumnName(i));
            }

            // Add rows
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

    // Custom renderer to alternate row colors and enhance readability
    class CustomTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Alternate row color for better readability
            if (row % 2 == 0) {
                c.setBackground(new Color(245, 245, 245));  // Light Gray for even rows
            } else {
                c.setBackground(Color.WHITE);  // White for odd rows
            }

            // If selected, change background to blue
            if (isSelected) {
                c.setBackground(new Color(0, 120, 215));  // Windows blue for selected rows
                c.setForeground(Color.WHITE);  // White text on selection
            }

            return c;
        }
    }

    private void refreshData(JTabbedPane tabbedPane) {
        // Refresh the data in the tabbed panes by removing old tables and adding new ones
        tabbedPane.removeAll();
        tabbedPane.addTab("Users", new JScrollPane(createTable("users")));
        tabbedPane.addTab("Admins", new JScrollPane(createTable("admins")));
        tabbedPane.addTab("Menu Items", new JScrollPane(createTable("menu_items")));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DatabaseViewer viewer = new DatabaseViewer();
            viewer.setVisible(true);
        });
    }
}
