package com.superstore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * WarehouseAdminGUI for warehouse admin operations.
 */
public class WarehouseAdminGUI extends JFrame {
    private Server server;
    private WarehouseAdmin user;
    private JLabel statusLabel;

    public WarehouseAdminGUI(Server server, WarehouseAdmin user) {
        this.server = server;
        this.user = user;
        initializeUI();
        showAlerts();
    }

    private void initializeUI() {
        setTitle("Warehouse Admin Dashboard - " + user.getWarehouseId());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // Menu Bar
        setJMenuBar(createMenuBar());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180)); // Steel Blue
        JLabel titleLabel = new JLabel("Warehouse Admin Dashboard - " + user.getWarehouseId(), JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Center Panel with Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));

        // Inventory Tab
        JPanel inventoryPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inventoryPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        inventoryPanel.setBackground(new Color(245, 245, 245));

        JButton manageCategoriesBtn = createStyledButton("Manage Categories", "Add, remove, or update categories, subcategories, and items");
        manageCategoriesBtn.addActionListener(e -> manageCategories());
        inventoryPanel.add(manageCategoriesBtn);

        JButton viewDataBtn = createStyledButton("View Warehouse Data", "View current warehouse inventory and details");
        viewDataBtn.addActionListener(e -> viewData());
        inventoryPanel.add(viewDataBtn);

        JButton checkAlertsBtn = createStyledButton("Check Alerts", "View reorder alerts");
        checkAlertsBtn.addActionListener(e -> showAlerts());
        inventoryPanel.add(checkAlertsBtn);

        tabbedPane.addTab("Inventory", inventoryPanel);

        // Orders Tab
        JPanel ordersPanel = new JPanel(new FlowLayout());
        ordersPanel.setBackground(new Color(245, 245, 245));

        JButton handleOrdersBtn = createStyledButton("Handle Orders", "Fulfill pending orders from stores");
        handleOrdersBtn.addActionListener(e -> handleOrders());
        ordersPanel.add(handleOrdersBtn);

        JButton forwardOrdersBtn = createStyledButton("Forward Orders", "Forward orders to other warehouses if needed");
        forwardOrdersBtn.addActionListener(e -> forwardOrders());
        ordersPanel.add(forwardOrdersBtn);

        JButton viewMessagesBtn = createStyledButton("View Messages", "View messages from stores");
        viewMessagesBtn.addActionListener(e -> viewMessages());
        ordersPanel.add(viewMessagesBtn);

        tabbedPane.addTab("Orders", ordersPanel);

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
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Warehouse Admin Dashboard\nManage warehouse inventory and orders."));
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

    private void showAlerts() {
        java.util.List<Alert> alerts = server.getAlertsForEntity(user.getWarehouseId());
        if (!alerts.isEmpty()) {
            StringBuilder alertMsg = new StringBuilder("Alerts:\n");
            for (Alert alert : alerts) {
                alertMsg.append(alert.toString()).append("\n");
            }
            JOptionPane.showMessageDialog(this, alertMsg.toString(), "Reorder Alerts", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void manageCategories() {
        String[] options = {"Add Category", "Add Subcategory", "Add Item", "Remove Category", "Remove Subcategory", "Remove Item", "Update Item Quantity"};
        String choice = (String) JOptionPane.showInputDialog(this, "Choose action:", "Manage Categories", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (choice == null) return;

        Warehouse warehouse = server.getWarehouse(user.getWarehouseId());
        if (warehouse == null) {
            JOptionPane.showMessageDialog(this, "Warehouse not found.");
            return;
        }

        switch (choice) {
            case "Add Category":
                String catId = JOptionPane.showInputDialog("Category ID:");
                String catName = JOptionPane.showInputDialog("Category Name:");
                if (catId != null && catName != null) {
                    Category cat = new Category(catId, catName);
                    warehouse.addCategory(cat);
                    JOptionPane.showMessageDialog(this, "Category added.");
                }
                break;
            case "Add Subcategory":
                String catNameForSub = JOptionPane.showInputDialog("Category Name:");
                String subId = JOptionPane.showInputDialog("Subcategory ID:");
                String subName = JOptionPane.showInputDialog("Subcategory Name:");
                if (catNameForSub != null && subId != null && subName != null) {
                    for (Category c : warehouse.getCategories()) {
                        if (c.getName().equals(catNameForSub)) {
                            Subcategory sub = new Subcategory(subId, subName);
                            c.addSubcategory(sub);
                            JOptionPane.showMessageDialog(this, "Subcategory added.");
                            break;
                        }
                    }
                }
                break;
            case "Add Item":
                String catNameForItem = JOptionPane.showInputDialog("Category Name:");
                String subNameForItem = JOptionPane.showInputDialog("Subcategory Name:");
                String itemId = JOptionPane.showInputDialog("Item ID:");
                String itemName = JOptionPane.showInputDialog("Item Name:");
                int qty = Integer.parseInt(JOptionPane.showInputDialog("Quantity:"));
                double cost = Double.parseDouble(JOptionPane.showInputDialog("Cost:"));
                double fixedCost = Double.parseDouble(JOptionPane.showInputDialog("Fixed Cost per Quarter:"));
                double carryingCost = Double.parseDouble(JOptionPane.showInputDialog("Carrying Cost per Unit per Quarter:"));
                double demand = Double.parseDouble(JOptionPane.showInputDialog("Demand per Quarter:"));
                double maxUsage = Double.parseDouble(JOptionPane.showInputDialog("Max Daily Usage:"));
                double avgUsage = Double.parseDouble(JOptionPane.showInputDialog("Avg Daily Usage:"));
                double maxLead = Double.parseDouble(JOptionPane.showInputDialog("Max Lead Time Days:"));
                double avgLead = Double.parseDouble(JOptionPane.showInputDialog("Avg Lead Time Days:"));
                if (itemId != null && itemName != null) {
                    Item item = new Item(itemId, itemName, qty, cost, fixedCost, carryingCost, demand, maxUsage, avgUsage, maxLead, avgLead);
                    for (Category c : warehouse.getCategories()) {
                        if (c.getName().equals(catNameForItem)) {
                            for (Subcategory s : c.getSubcategories()) {
                                if (s.getName().equals(subNameForItem)) {
                                    s.addItem(item);
                                    JOptionPane.showMessageDialog(this, "Item added.");
                                    server.checkAndGenerateAlerts();
                                    Logger.logInfo("Item added to warehouse: " + itemId);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }
                break;
            case "Remove Category":
                String remCatName = JOptionPane.showInputDialog("Category Name:");
                if (remCatName != null) {
                    warehouse.removeCategory(remCatName);
                    JOptionPane.showMessageDialog(this, "Category removed.");
                }
                break;
            case "Remove Subcategory":
                String remCatName2 = JOptionPane.showInputDialog("Category Name:");
                String remSubName = JOptionPane.showInputDialog("Subcategory Name:");
                if (remCatName2 != null && remSubName != null) {
                    for (Category c : warehouse.getCategories()) {
                        if (c.getName().equals(remCatName2)) {
                            c.removeSubcategory(remSubName);
                            JOptionPane.showMessageDialog(this, "Subcategory removed.");
                            break;
                        }
                    }
                }
                break;
            case "Remove Item":
                String remCatName3 = JOptionPane.showInputDialog("Category Name:");
                String remSubName2 = JOptionPane.showInputDialog("Subcategory Name:");
                String remItemId = JOptionPane.showInputDialog("Item ID:");
                if (remCatName3 != null && remSubName2 != null && remItemId != null) {
                    for (Category c : warehouse.getCategories()) {
                        if (c.getName().equals(remCatName3)) {
                            for (Subcategory s : c.getSubcategories()) {
                                if (s.getName().equals(remSubName2)) {
                                    s.removeItem(remItemId);
                                    JOptionPane.showMessageDialog(this, "Item removed.");
                                    server.checkAndGenerateAlerts();
                                    Logger.logInfo("Item removed from warehouse: " + remItemId);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }
                break;
            case "Update Item Quantity":
                String updCatName = JOptionPane.showInputDialog("Category Name:");
                String updSubName = JOptionPane.showInputDialog("Subcategory Name:");
                String updItemId = JOptionPane.showInputDialog("Item ID:");
                int newQty = Integer.parseInt(JOptionPane.showInputDialog("New Quantity:"));
                if (updCatName != null && updSubName != null && updItemId != null) {
                    for (Category c : warehouse.getCategories()) {
                        if (c.getName().equals(updCatName)) {
                            for (Subcategory s : c.getSubcategories()) {
                                if (s.getName().equals(updSubName)) {
                                    for (Item i : s.getItems()) {
                                        if (i.getId().equals(updItemId)) {
                                            i.setQuantity(newQty);
                                            JOptionPane.showMessageDialog(this, "Quantity updated.");
                                            server.checkAndGenerateAlerts();
                                            Logger.logInfo("Item quantity updated: " + updItemId + " to " + newQty);
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }
                break;
        }
    }

    private void handleOrders() {
        StringBuilder ordersText = new StringBuilder("Pending Orders:\n");
        boolean hasOrders = false;
        for (Order o : server.getOrders()) {
            if (o.getToEntityId().equals(user.getWarehouseId()) && o.getStatus() == Order.OrderStatus.PENDING) {
                ordersText.append(o.toString()).append("\n");
                hasOrders = true;
            }
        }
        if (!hasOrders) {
            JOptionPane.showMessageDialog(this, "No pending orders.");
            return;
        }
        String orderId = JOptionPane.showInputDialog(this, ordersText.toString() + "\nEnter Order ID to fulfill:");
        if (orderId != null) {
            for (Order o : server.getOrders()) {
                if (o.getId().equals(orderId) && o.getToEntityId().equals(user.getWarehouseId())) {
                    o.setStatus(Order.OrderStatus.RECEIVED);
                    // Add quantity to warehouse stock
                    Warehouse warehouse = server.getWarehouse(user.getWarehouseId());
                    for (Category c : warehouse.getCategories()) {
                        for (Subcategory s : c.getSubcategories()) {
                            for (Item i : s.getItems()) {
                                if (i.getId().equals(o.getItemId())) {
                                    i.setQuantity(i.getQuantity() + o.getQuantity());
                                    JOptionPane.showMessageDialog(this, "Order fulfilled. Stock updated.");
                                    server.checkAndGenerateAlerts();
                                    Logger.logInfo("Order fulfilled: " + orderId);
                                    return;
                                }
                            }
                        }
                    }
                    JOptionPane.showMessageDialog(this, "Item not found in warehouse.");
                    break;
                }
            }
            JOptionPane.showMessageDialog(this, "Order not found.");
        }
    }

    private void viewMessages() {
        java.util.List<Message> messages = server.getMessagesForWarehouse(user.getWarehouseId());
        if (messages.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No messages.");
        } else {
            StringBuilder msg = new StringBuilder("Messages:\n");
            for (Message m : messages) {
                msg.append(m.toString()).append("\n");
            }
            JOptionPane.showMessageDialog(this, msg.toString());
        }
    }

    private void viewData() {
        Warehouse warehouse = server.getWarehouse(user.getWarehouseId());
        if (warehouse != null) {
            JOptionPane.showMessageDialog(this, warehouse.toString());
        } else {
            JOptionPane.showMessageDialog(this, "Warehouse not found.");
        }
    }

    private void forwardOrders() {
        StringBuilder pendingOrders = new StringBuilder("Pending Orders:\n");
        boolean hasOrders = false;
        for (Order o : server.getOrders()) {
            if (o.getToEntityId().equals(user.getWarehouseId()) && o.getStatus() == Order.OrderStatus.PENDING) {
                pendingOrders.append(o.toString()).append("\n");
                hasOrders = true;
            }
        }

        if (!hasOrders) {
            JOptionPane.showMessageDialog(this, "No pending orders to forward.");
            return;
        }

        String orderId = JOptionPane.showInputDialog(this, pendingOrders.toString() + "\nEnter Order ID to forward:");
        if (orderId != null) {
            for (Order o : server.getOrders()) {
                if (o.getId().equals(orderId) && o.getToEntityId().equals(user.getWarehouseId())) {
                    // Show available warehouses to forward to
                    StringBuilder warehouses = new StringBuilder("Available Warehouses:\n");
                    for (Warehouse w : server.getWarehouses().values()) {
                        if (!w.getId().equals(user.getWarehouseId())) {
                            warehouses.append(w.getId()).append(" - ").append(w.getName()).append("\n");
                        }
                    }

                    String targetWarehouseId = JOptionPane.showInputDialog(this, warehouses.toString() + "\nEnter target warehouse ID:");
                    if (targetWarehouseId != null && server.getWarehouse(targetWarehouseId) != null) {
                        // Create new order to target warehouse
                        String newOrderId = "ORD" + System.currentTimeMillis();
                        Order forwardedOrder = new Order(newOrderId, o.getItemId(), o.getQuantity(), new java.util.Date(), user.getWarehouseId(), targetWarehouseId);
                        server.addOrder(forwardedOrder);

                        // Mark original order as forwarded
                        o.setStatus(Order.OrderStatus.RECEIVED);

                        JOptionPane.showMessageDialog(this, "Order forwarded to warehouse " + targetWarehouseId);
                        Logger.logInfo("Order forwarded: " + orderId + " to " + targetWarehouseId);
                        return;
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid warehouse ID.");
                        return;
                    }
                }
            }
            JOptionPane.showMessageDialog(this, "Order not found.");
        }
    }
}
