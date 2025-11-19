package com.superstore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * StoreKeeperGUI for store keeper operations.
 */
public class StoreKeeperGUI extends JFrame {
    private Server server;
    private StoreKeeper user;
    private JLabel statusLabel;

    public StoreKeeperGUI(Server server, StoreKeeper user) {
        this.server = server;
        this.user = user;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Store Keeper Dashboard - " + user.getStoreId());
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // Menu Bar
        setJMenuBar(createMenuBar());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180)); // Steel Blue
        JLabel titleLabel = new JLabel("Store Keeper Dashboard - " + user.getStoreId(), JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Center Panel with Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));

        // Inventory Tab
        JPanel inventoryPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inventoryPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        inventoryPanel.setBackground(new Color(245, 245, 245));

        JButton updateStockBtn = createStyledButton("Update Stock Levels", "Update item quantities in assigned categories");
        updateStockBtn.addActionListener(e -> updateStock());
        inventoryPanel.add(updateStockBtn);

        JButton viewAssignedCategoriesBtn = createStyledButton("View Assigned Categories", "View categories/subcategories assigned to you");
        viewAssignedCategoriesBtn.addActionListener(e -> viewAssignedCategories());
        inventoryPanel.add(viewAssignedCategoriesBtn);

        JButton viewDataBtn = createStyledButton("View Store Data", "View current store inventory");
        viewDataBtn.addActionListener(e -> viewData());
        inventoryPanel.add(viewDataBtn);

        JButton changePasswordBtn = createStyledButton("Change Password", "Update your account password");
        changePasswordBtn.addActionListener(e -> changePassword());
        inventoryPanel.add(changePasswordBtn);

        tabbedPane.addTab("Inventory", inventoryPanel);

        add(tabbedPane, BorderLayout.CENTER);

        // Status Bar
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        statusPanel.add(statusLabel, BorderLayout.WEST);
        add(statusPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File Menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> {
            dispose();
            new LoginGUI(server);
        });
        fileMenu.add(logoutItem);
        menuBar.add(fileMenu);

        // View Menu
        JMenu viewMenu = new JMenu("View");
        JMenuItem refreshItem = new JMenuItem("Refresh");
        refreshItem.addActionListener(e -> statusLabel.setText("Refreshed"));
        viewMenu.add(refreshItem);
        menuBar.add(viewMenu);

        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Store Keeper Dashboard\nManage assigned inventory categories."));
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);

        return menuBar;
    }

    private JButton createStyledButton(String text, String tooltip) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBackground(new Color(100, 149, 237)); // Cornflower Blue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setToolTipText(tooltip);
        return button;
    }

    private void updateStock() {
        // Show assigned categories first
        StringBuilder assigned = new StringBuilder("Your Assigned Categories:\n");
        Store store = server.getStore(user.getStoreId());
        if (store != null) {
            for (Category c : store.getCategories()) {
                for (Subcategory s : c.getSubcategories()) {
                    // For now, assume all categories are assigned - in real implementation, check assignment
                    assigned.append(c.getName()).append(" > ").append(s.getName()).append("\n");
                }
            }
        }

        JOptionPane.showMessageDialog(this, assigned.toString());

        // Allow updating stock for assigned categories
        String catName = JOptionPane.showInputDialog("Category Name:");
        String subName = JOptionPane.showInputDialog("Subcategory Name:");
        String itemId = JOptionPane.showInputDialog("Item ID:");
        int newQty = Integer.parseInt(JOptionPane.showInputDialog("New Quantity:"));

        if (catName != null && subName != null && itemId != null) {
            Store st = server.getStore(user.getStoreId());
            if (st != null) {
                for (Category c : st.getCategories()) {
                    if (c.getName().equals(catName)) {
                        for (Subcategory s : c.getSubcategories()) {
                            if (s.getName().equals(subName)) {
                                for (Item i : s.getItems()) {
                                    if (i.getId().equals(itemId)) {
                                        i.setQuantity(newQty);
                                        JOptionPane.showMessageDialog(this, "Stock updated successfully.");
                                        server.checkAndGenerateAlerts();
                                        Logger.logInfo("Keeper updated stock: " + itemId + " to " + newQty);
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            JOptionPane.showMessageDialog(this, "Item not found in assigned categories.");
        }
    }

    private void viewAssignedCategories() {
        StringBuilder assigned = new StringBuilder("Your Assigned Categories:\n");
        Store store = server.getStore(user.getStoreId());
        if (store != null) {
            for (Category c : store.getCategories()) {
                for (Subcategory s : c.getSubcategories()) {
                    // For now, assume all categories are assigned - in real implementation, check assignment
                    assigned.append(c.getName()).append(" > ").append(s.getName()).append("\n");
                    for (Item i : s.getItems()) {
                        assigned.append("  - ").append(i.getName()).append(" (").append(i.getQuantity()).append(")\n");
                    }
                }
            }
        }
        JOptionPane.showMessageDialog(this, assigned.toString());
    }

    private void changePassword() {
        String currentPassword = JOptionPane.showInputDialog("Current Password:");
        if (!user.getPassword().equals(currentPassword)) {
            JOptionPane.showMessageDialog(this, "Incorrect current password.");
            return;
        }

        String newPassword = JOptionPane.showInputDialog("New Password:");
        String confirmPassword = JOptionPane.showInputDialog("Confirm New Password:");

        if (newPassword != null && newPassword.equals(confirmPassword)) {
            user.setPassword(newPassword);
            JOptionPane.showMessageDialog(this, "Password changed successfully.");
            Logger.logInfo("Keeper password changed: " + user.getLoginId());
        } else {
            JOptionPane.showMessageDialog(this, "Passwords do not match.");
        }
    }

    private void viewData() {
        Store store = server.getStore(user.getStoreId());
        if (store != null) {
            JOptionPane.showMessageDialog(this, store.toString());
        } else {
            JOptionPane.showMessageDialog(this, "Store not found.");
        }
    }
}
